package com.example.battleship.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.battleship.Adapters.StatisticsAdapter;
import com.example.battleship.Models.Statistic;
import com.example.battleship.Models.User;
import com.example.battleship.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StatisticsFragment extends DialogFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        List<Statistic> statistics=  new ArrayList<>();
        for(int i = 0; i < 10; i++){
            User user = new User(
                    Objects.requireNonNull(mAuth.getCurrentUser()).getUid(),
                    mAuth.getCurrentUser().getDisplayName(),
                    Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).toString());

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss yyyy.MM.dd ");
            SimpleDateFormat formatForTimeNow = new SimpleDateFormat("HH:mm:ss");
            statistics.add(new Statistic(user, user, formatForDateNow.format(new Date()), formatForTimeNow.format(new Date())));
        }

        RecyclerView statisticsRecycler = view.findViewById(R.id.statisticsRecycler);
        StatisticsAdapter statisticsAdapter = new StatisticsAdapter(statistics, getContext());
        statisticsRecycler.setAdapter(statisticsAdapter);

        return view;
    }
}