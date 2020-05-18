package com.example.medidor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    private Button buttonTeste;
    private Button buttonCurva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        referencia.child( "StatusMenu" ).setValue( "MenuPrincipal" );

        buttonTeste = findViewById(R.id.buttonTeste);

        buttonTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                referencia.child( "StatusMenu" ).setValue( "TestePrecisao" );
                Intent intentTeste = new Intent(getApplicationContext(), TesteActivity.class);
                startActivity(intentTeste);
            }
        });

        buttonCurva = findViewById(R.id.buttonCurva);

        buttonCurva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                referencia.child( "StatusMenu" ).setValue( "CurvaCarga" );
                Intent intentCurva = new Intent(getApplicationContext(), CurvaActivity.class);
                startActivity(intentCurva);
            }
        });

    }
}
