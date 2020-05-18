package com.example.medidor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ObterCurvaActivity extends AppCompatActivity {

    DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    DatabaseReference referenciaCurva;

    BarChart barChart;
    BarData barData;

    String nomeCurva;
    String intervalo;
    List<String> label =  new ArrayList<>();
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obter_curva);

        barChart = findViewById(R.id.barchart3);

        Intent intent = getIntent();
        nomeCurva = intent.getStringExtra("nomeCurva");
        intervalo = intent.getStringExtra("intervalo");

        TextView text = (TextView)findViewById(R.id.textView11);
        text.setText("Curva de Carga: " + nomeCurva);

        referencia.child("CurvaCarga").child("Status").setValue("ObtendoCurva");
        referenciaCurva = FirebaseDatabase.getInstance().getReference(nomeCurva);

        GregorianCalendar gcalendar = new GregorianCalendar();

        int hour = gcalendar.get(Calendar.HOUR) ;
        int minute = gcalendar.get(Calendar.MINUTE);
        String minuteText;
        if (minute < 10)
            minuteText = ":0" + minute;
        else
            minuteText = ":" + minute;
        int second = gcalendar.get(Calendar.SECOND);
        String secondText;
        if (second < 10)
            secondText = ":0" + second;
        else
            secondText = ":" + second;
        String time =  hour + minuteText + secondText;
        label.add(x, time);
        label.add(1,"0");

        retrieveData();
    }

    private void retrieveData() {

        /*
        referenciaCurva.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */

        referenciaCurva.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<BarEntry> dataVals = new ArrayList<BarEntry>();

                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot myDataSnapchot : dataSnapshot.getChildren()){
                        DataPoint dataPoint = myDataSnapchot.getValue(DataPoint.class);
                        dataVals.add(new BarEntry(dataPoint.getxValue(), dataPoint.getyValue()));
                        x = dataPoint.getxValue();
                    }
                    showChart(dataVals);
                }else{
                    barChart.clear();
                    barChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showChart(ArrayList<BarEntry> dataVals) {
        BarDataSet barDataSet = new BarDataSet(dataVals, nomeCurva);
        ArrayList<IBarDataSet> iBarDataSets = new ArrayList<>();
        //barDataSet.setValues(dataVals);
        //barDataSet.setLabel("Data set 1");
        iBarDataSets.clear();
        iBarDataSets.add(barDataSet);
        barData = new BarData(iBarDataSets);
        barChart.clear();
        barChart.setData(barData);
        barChart.invalidate();
        barChart.setAutoScaleMinMaxEnabled(true);
        barChart.setDrawValueAboveBar(true);
        barData.setDrawValues(false);
        barData.setBarWidth(1f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(270);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setStartAtZero(true);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        Description description = barChart.getDescription();
        description.setEnabled(false);

        GregorianCalendar gcalendar = new GregorianCalendar();

        int hour = gcalendar.get(Calendar.HOUR) ;
        int minute = gcalendar.get(Calendar.MINUTE);
        String minuteText;
        if (minute < 10)
            minuteText = ":0" + minute;
        else
            minuteText = ":" + minute;
        int second = gcalendar.get(Calendar.SECOND);
        String secondText;
        if (second < 10)
            secondText = ":0" + second;
        else
            secondText = ":" + second;
        String time =  hour + minuteText + secondText;
        label.add(x, time);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return label.get((int)value);
            }
        });

    }


}
