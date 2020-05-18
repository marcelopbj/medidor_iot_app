package com.example.medidor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TesteActivity extends AppCompatActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        getSupportActionBar().setTitle("Teste de Precisão");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referencia.child("TestePrecisao").child("Status" ).setValue("ObtendoValores");

    }

    public void proximo (View view){

        TextInputEditText campoConstante = findViewById(R.id.constante);
        if (campoConstante.length() == 0){
            campoConstante.setError("Insira a Constante");
        }

        TextInputEditText campoPulsos = findViewById(R.id.pulsos);
        if(campoPulsos.length() == 0){
            campoPulsos.setError("Insira nº de Pulsos");
        }

        if (campoConstante.length() > 0 && campoPulsos.length() > 0){

            String valorConstante = campoConstante.getText().toString();
            double constante =  Double.parseDouble(valorConstante);
            referencia.child("TestePrecisao").child("ValorConstante").setValue(constante);

            String numeroPulsos = campoPulsos.getText().toString();
            int pulsos = Integer.parseInt(numeroPulsos);
            referencia.child("TestePrecisao").child("NumeroPulsos").setValue(pulsos);

            referencia.child("TestePrecisao").child("Status").setValue("Proximo");
            Intent intentRealizarTeste = new Intent(getApplicationContext(), RealizarTesteActivity.class);
            intentRealizarTeste.putExtra("pulsos", numeroPulsos);
            intentRealizarTeste.putExtra("constante", valorConstante);
            startActivity(intentRealizarTeste);

        }

    }

}
