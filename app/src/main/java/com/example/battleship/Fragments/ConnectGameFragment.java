package com.example.battleship.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.battleship.Activities.LobbyActivity;
import com.example.battleship.Models.Game;
import com.example.battleship.Models.User;
import com.example.battleship.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectGameFragment extends DialogFragment {

    TextView idInput;
    TextView  hostTextView;
    TextView guestTextView;

    Button connectButton;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    DatabaseReference gamesDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_connect_game, container, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar2);
        hostTextView = view.findViewById(R.id.hostTextView);
        guestTextView = view.findViewById(R.id.guestTextView);
        idInput = view.findViewById(R.id.idInput);
        connectButton = view.findViewById(R.id.connectButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        connectButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            gamesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("games").child(idInput.getText().toString());

            gamesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Game game = snapshot.getValue(Game.class);
                    if(game.getHostUser() != null){
                        game.setConnectedUser(new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getPhotoUrl().toString()));
                        gamesDatabaseReference.setValue(game);
                        Intent intent = new Intent(getContext(), LobbyActivity.class);
                        intent.putExtra("game", game);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

        return view;
    }
}