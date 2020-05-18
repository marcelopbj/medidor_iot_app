package com.example.medidor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;


public class NovaCurvaFragment extends Fragment {

    Spinner spinnerIntervalo;
    Spinner spinnerPeriodo;

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    DatabaseReference referenciaCurvas;

    ArrayList<String> CurvasList = CurvaActivity.CurvasList;


    public NovaCurvaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_nova_curva, container, false);

        spinnerIntervalo = view.findViewById(R.id.spinnerIntervalo);
        ArrayAdapter<CharSequence> adapterIntervalo = ArrayAdapter.createFromResource(this.getActivity(), R.array.intervalos, android.R.layout.simple_spinner_item);
        adapterIntervalo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIntervalo.setAdapter(adapterIntervalo);

        spinnerPeriodo = view.findViewById(R.id.spinnerPeriodo);
        ArrayAdapter<CharSequence> adapterPeriodo = ArrayAdapter.createFromResource(this.getActivity(), R.array.periodos, android.R.layout.simple_spinner_item);
        adapterPeriodo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodo.setAdapter(adapterPeriodo);

        Button buttonOk = view.findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Boolean checkCurva = true;

            TextInputEditText campoCurva = view.findViewById(R.id.nomeCurva);
            String nomeCurva = campoCurva.getText().toString();
            if (campoCurva.length() == 0){
                campoCurva.setError("Inserir o Nome");
            }

            for (int i=0; i<CurvasList.size(); i++){
                if (nomeCurva.equals(CurvasList.get(i))){
                    campoCurva.setError("Curva já existe");
                    checkCurva=false;
                }
            }

            TextInputEditText campoConstante = view.findViewById(R.id.valorConstante);
            if (campoConstante.length() == 0){
                campoConstante.setError("Insira a Constante");
            }

            if (campoCurva.length() > 0 && campoConstante.length() > 0 && checkCurva == true){

                referencia.child("CurvaCarga").child("NomeCurva").setValue(nomeCurva);

                String constante = campoConstante.getText().toString();
                double valorConstante =  Double.parseDouble(constante);
                referencia.child("CurvaCarga").child("ValorConstante").setValue(valorConstante);

                String intervalo = spinnerIntervalo.getSelectedItem().toString();
                referencia.child("Curvas").child("info").child(nomeCurva).child("Intervalo").setValue(intervalo);
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
                        referencia.child("Curvas").child("info").child(nomeCurva).child("Periodo").setValue(1);
                        break;
                    case "6 horas":
                        referencia.child("CurvaCarga").child("Periodo").setValue(6);
                        referencia.child("Curvas").child("info").child(nomeCurva).child("Periodo").setValue(6);
                        break;
                    case "12 horas":
                        referencia.child("CurvaCarga").child("Periodo").setValue(12);
                        referencia.child("Curvas").child("info").child(nomeCurva).child("Periodo").setValue(12);
                        break;
                    case "24 horas":
                        referencia.child("CurvaCarga").child("Periodo").setValue(24);
                        referencia.child("Curvas").child("info").child(nomeCurva).child("Periodo").setValue(24);
                        break;
                    default:
                        break;
                }

                Intent intentObterCurvaActivity = new Intent(getActivity(), ObterCurvaActivity.class);
                intentObterCurvaActivity.putExtra("nomeCurva",nomeCurva);
                intentObterCurvaActivity.putExtra("intervalo", intervalo);
                intentObterCurvaActivity.putExtra("periodo", periodo);
                startActivity(intentObterCurvaActivity);
            }

            }
        });

        return view;
    }


}
