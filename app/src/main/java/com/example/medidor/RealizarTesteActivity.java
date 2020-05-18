package com.example.medidor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RealizarTesteActivity extends AppCompatActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    Button button;
    String numeroPulsos;
    String valorConstante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_teste);

        getSupportActionBar().setTitle("Teste de Precisão");

        button = (Button)findViewById(R.id.button3);
        button.setTextSize(50);
        button.setText("iniciar");

        Intent intent = getIntent();
        numeroPulsos = intent.getStringExtra("pulsos");
        valorConstante = intent.getStringExtra("constante");

        TextView textConstante = findViewById(R.id.textConstante);
        TextView textPulsos = findViewById(R.id.textPulsos);

        textConstante.setText(valorConstante + " Wh");
        textPulsos.setText( numeroPulsos + " Pulsos");

    }

    public void iniciar (View view){

        referencia.child("TestePrecisao").child("Status").setValue("Iniciar");
        content();

    }

    public void content(){

        DatabaseReference receberPulsos = referencia.child("TestePrecisao").child("PulsosStatus");

        receberPulsos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String pulsosStatus = dataSnapshot.getValue().toString();

                if (pulsosStatus.equals("Calibrando")){
                    button.setTextSize(40);
                    button.setText("calibrando...");
                } else{
                    button.setTextSize(120);
                    button.setText(pulsosStatus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference receberPotencia = referencia.child("TestePrecisao").child("Potencia");

        receberPotencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String potencia = dataSnapshot.getValue().toString();

                if (potencia.equals("")){

                }
                else{
                    TextView textPotencia = findViewById(R.id.textPotencia);
                    textPotencia.setText(potencia + " W");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refresh(500);

    }

    public void refresh (int milliseconds){

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };

    }

}

