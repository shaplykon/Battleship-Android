package com.example.battleship.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.battleship.Adapters.MatrixAdapter;
import com.example.battleship.Models.Matrix;
import com.example.battleship.R;

public class FieldFragment extends Fragment {
    RecyclerView recyclerView;

    public FieldFragment() {

    }

    public static FieldFragment newInstance() {
        FieldFragment fragment = new FieldFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field, container, false);
        recyclerView = view.findViewById(R.id.playerRecyclerView);

        Matrix matrix = new Matrix();
        MatrixAdapter adapter = new MatrixAdapter(getContext(), matrix);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),10));
        return view;
    }
}