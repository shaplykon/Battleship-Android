package com.example.battleship.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.battleship.R;
import com.example.battleship.Utils.Constants;

public class FieldControlsFragment extends Fragment {

    ControlsInteractionListener controlsInteractionListener;
    Button refreshButton;
    Button readyButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface ControlsInteractionListener{
        void controlInteraction(int action);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        controlsInteractionListener = (ControlsInteractionListener)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_field_controls, container, false);

        refreshButton = view.findViewById(R.id.refreshButton);
        readyButton = view.findViewById(R.id.readyButton);

        refreshButton.setOnClickListener(v -> controlsInteractionListener.controlInteraction(Constants.REFRESH_ACTION));

        return view;
    }
}