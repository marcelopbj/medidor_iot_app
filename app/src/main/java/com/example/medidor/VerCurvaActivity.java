package com.example.medidor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerCurvaActivity extends AppCompatActivity {

    DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    DatabaseReference referenciaCurvas;
    DatabaseReference referenciaCurvaSelecionada;

    String curvaSelecionada;
    String periodoString;
    String intervalo;
    int periodo;

    BarChart barChart;
    BarData barData;

    ArrayList<BarEntry> dataVals = PesquisarFragment.dataVals;
    List<String> label =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_curva);

        barChart = findViewById(R.id.barchartVer);

        referenciaCurvas = FirebaseDatabase.getInstance().getReference("Curvas");


        Intent intent = getIntent();
        curvaSelecionada = intent.getStringExtra("curvaSelecionada");
        //periodoString = intent.getStringExtra("periodo");
        //periodo = Integer.parseInt(periodoString);
        intervalo = intent.getStringExtra("intervalo");



        TextView text = (TextView)findViewById(R.id.textTituloVer);
        text.setText("Curva de Carga: " + curvaSelecionada);


        TextView textTeste = findViewById(R.id.textPotenciaVer);
        textTeste.setText(intervalo);

        referenciaCurvaSelecionada = referenciaCurvas.child("id").child(curvaSelecionada);

        /*
        switch (intervalo){
            /*
            case "30 segundos":

                break;
            case "1 minuto":

                break;
            case "5 minutos":

                break;
            case "15 minutos":

                break;
            case "30 minutos":

                break;


            case "1 hora":
                for (int i=0; i<=periodo; i++){
                    if(i<10){
                        label.add("0" + i + ":00");
                    }else{
                        label.add(i + ":00");
                    }
                }
                break;
            default:
                break;
        }

         */


        showChart(dataVals);
    }

    private void showChart(ArrayList<BarEntry> dataVals){

        BarDataSet barDataSet = new BarDataSet(dataVals, curvaSelecionada);
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

    }
}
