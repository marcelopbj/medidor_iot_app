package com.example.medidor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CurvaActivity extends AppCompatActivity {

    Spinner spinnerIntervalo;
    Spinner spinnerPeriodo;

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    DatabaseReference referenciaCurvas;

    public static ArrayList<String> CurvasList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curva);


        getSupportActionBar().setTitle("Curva de Carga");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configuraBottomNavigationView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPager, new NovaCurvaFragment()).commit();

        /*
        spinnerIntervalo = findViewById(R.id.spinnerIntervalo);
        ArrayAdapter<CharSequence> adapterIntervalo = ArrayAdapter.createFromResource(this, R.array.intervalos, android.R.layout.simple_spinner_item);
        adapterIntervalo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIntervalo.setAdapter(adapterIntervalo);

        spinnerPeriodo = findViewById(R.id.spinnerPeriodo);
        ArrayAdapter<CharSequence> adapterPeriodo = ArrayAdapter.createFromResource(this, R.array.periodos, android.R.layout.simple_spinner_item);
        adapterPeriodo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodo.setAdapter(adapterPeriodo);

         */

        referencia.child("CurvaCarga").child("Intervalo").setValue(0);
        referencia.child("CurvaCarga").child("NomeCurva").setValue(0);
        referencia.child("CurvaCarga").child("Periodo").setValue(0);
        referencia.child("CurvaCarga").child("PotenciaMedia").setValue(0);
        referencia.child("CurvaCarga").child("Status").setValue("ObtendoValores");
        referencia.child("CurvaCarga").child("ValorConstante").setValue(0);


        CurvasList.clear();

        referenciaCurvas = FirebaseDatabase.getInstance().getReference("Curvas");

        referenciaCurvas.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()){
                    CurvasList.add(String.valueOf(dsp.getKey()));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void habilitarNavegacao(BottomNavigationViewEx viewEx){

        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId()){
                    case R.id.ic_nova_curva:
                        fragmentTransaction.replace(R.id.viewPager, new NovaCurvaFragment()).commit();
                        return true;

                     case R.id.ic_pesquisar:
                        fragmentTransaction.replace(R.id.viewPager, new PesquisarFragment()).commit();
                        return true;
                }

                return false;
            }
        });

    }

    private void configuraBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.getBottomNavigationItemViews();

        habilitarNavegacao(bottomNavigationViewEx);

    }
/*
    public void ok (View view){
        TextInputEditText campoCurva = findViewById(R.id.nomeCurva);
        if (campoCurva.length() == 0){
            campoCurva.setError("Inserir o Nome");
        }

        TextInputEditText campoConstante = findViewById(R.id.valorConstante);
        if (campoConstante.length() == 0){
            campoConstante.setError("Insira a Constante");
        }


        TextView text = findViewById(R.id.textView);
        text.setText(CurvasList.get(2));

        /*
        if (campoCurva.length() > 0 && campoConstante.length() > 0){

            String nomeCurva = campoCurva.getText().toString();
            referencia.child("CurvaCarga").child("NomeCurva").setValue(nomeCurva);

            String constante = campoConstante.getText().toString();
            double valorConstante =  Double.parseDouble(constante);
            referencia.child("CurvaCarga").child("ValorConstante").setValue(valorConstante);

            String intervalo = spinnerIntervalo.getSelectedItem().toString();
            switch (intervalo){
                case "30 segundos":
                    referencia.child("CurvaCarga").child("Intervalo").setValue(0.5);
                    break;
                case "1 minuto":
                    referencia.child("CurvaCarga").child("Intervalo").setValue(1);
                    break;
                case "5 minutos":
                    referencia.child("CurvaCarga").child("Intervalo").setValue(5);
                    break;
                case "15 minutos":
                    referencia.child("CurvaCarga").child("Intervalo").setValue(15);
                    break;
                case "30 minutos":
                    referencia.child("CurvaCarga").child("Intervalo").setValue(30);
                    break;
                case "1 hora":
                    referencia.child("CurvaCarga").child("Intervalo").setValue(60);
                    break;
                default:
                    break;
            }

            String periodo = spinnerPeriodo.getSelectedItem().toString();
            switch (periodo){
                case "1 hora":
                    referencia.child("CurvaCarga").child("Periodo").setValue(1);
                    break;
                case "6 horas":
                    referencia.child("CurvaCarga").child("Periodo").setValue(6);
                    break;
                case "12 horas":
                    referencia.child("CurvaCarga").child("Periodo").setValue(12);
                    break;
                case "24 horas":
                    referencia.child("CurvaCarga").child("Periodo").setValue(24);
                    break;
                default:
                    break;
            }

            Intent intentObterCurvaActivity = new Intent(getApplicationContext(), ObterCurvaActivity.class);
            intentObterCurvaActivity.putExtra("nomeCurva",nomeCurva);
            intentObterCurvaActivity.putExtra("intervalo", intervalo);
            intentObterCurvaActivity.putExtra("periodo", periodo);
            startActivity(intentObterCurvaActivity);

        }



    }
    */

}
