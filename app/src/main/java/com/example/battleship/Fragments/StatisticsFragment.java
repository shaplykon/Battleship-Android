package com.example.battleship.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.battleship.Adapters.StatisticsAdapter;
import com.example.battleship.Models.Ship;
import com.example.battleship.Models.Statistic;
import com.example.battleship.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends DialogFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        List<Statistic> statistics=  new ArrayList<>();
        DatabaseReference statisticsDatabaseReference = FirebaseDatabase.getInstance().getReference("statistics");

        statisticsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null ){
                    for(DataSnapshot sampleSnapshot:snapshot.getChildren()){
                        statistics.add(sampleSnapshot.getValue(Statistic.class));
                        RecyclerView statisticsRecycler = view.findViewById(R.id.statisticsRecycler);
                        StatisticsAdapter statisticsAdapter = new StatisticsAdapter(statistics, getContext());
                        statisticsRecycler.setAdapter(statisticsAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}