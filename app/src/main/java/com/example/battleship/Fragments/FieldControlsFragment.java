package com.example.battleship.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.example.battleship.R;
import com.example.battleship.Utils.Constants;

public class FieldControlsFragment extends Fragment {

    ControlsInteractionListener controlsInteractionListener;
    Button refreshButton;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch readySwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface ControlsInteractionListener{
        void ControlInteraction(int action, Boolean value);
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
        readySwitch = view.findViewById(R.id.readySwitch);
        refreshButton.setOnClickListener(v -> controlsInteractionListener.ControlInteraction(Constants.REFRESH_ACTION, null));
        readySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> controlsInteractionListener.ControlInteraction(Constants.READY_ACTION, isChecked));
        return view;
    }
}