package com.example.medidor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PesquisarFragment extends Fragment {

    ListView listView;
    SearchView searchView;

    ArrayList<String> CurvasList = CurvaActivity.CurvasList;

    DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    DatabaseReference referenciaCurvas;
    DatabaseReference referenciaCurvaSelecionada;
    DatabaseReference referenciaIntervalo;

    String intervalo;
    String periodo;

    public static ArrayList<BarEntry> dataVals = new ArrayList<BarEntry>();

    public PesquisarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_pesquisar, container, false);

        referenciaCurvas = FirebaseDatabase.getInstance().getReference("Curvas");

        listView = view.findViewById(R.id.listView);
        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                CurvasList
        );

        listView.setAdapter(adaptador);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String curvaSelecionada = listView.getItemAtPosition(position).toString();

                referenciaCurvaSelecionada = referenciaCurvas.child("id").child(curvaSelecionada);


                referenciaCurvaSelecionada.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChildren()){
                            for(DataSnapshot myDataSnapchot : dataSnapshot.getChildren()){
                                DataPoint dataPoint = myDataSnapchot.getValue(DataPoint.class);
                                dataVals.add(new BarEntry(dataPoint.getxValue(), dataPoint.getyValue()));
                            }

                        }else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                referenciaIntervalo =  referenciaCurvas.child("info").child(curvaSelecionada).child("Intervalo");
                referenciaIntervalo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        intervalo = dataSnapshot.getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                /*
                referenciaCurvas.child("info").child(curvaSelecionada).child("Periodo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        periodo = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                 */

                Intent intentVerCurvaActivity = new Intent(getActivity(), VerCurvaActivity.class);
                intentVerCurvaActivity.putExtra("curvaSelecionada",curvaSelecionada);
                //intentVerCurvaActivity.putExtra("periodo", periodo);
                intentVerCurvaActivity.putExtra("intervalo", intervalo);
                startActivity(intentVerCurvaActivity);

            }
        });
        
        return view;
    }


}
