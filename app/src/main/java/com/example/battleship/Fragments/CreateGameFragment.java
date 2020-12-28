package com.example.battleship.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.battleship.Activities.LobbyActivity;
import com.example.battleship.Models.Game;
import com.example.battleship.Models.User;
import com.example.battleship.R;
import com.example.battleship.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CreateGameFragment extends DialogFragment {
    TextView gameIdTextView;
    ProgressBar progressBar;

    User hostUser;
    String gameId;

    DatabaseReference usersDatabaseReference;
    DatabaseReference gameDatabaseReference;
    ValueEventListener gameEventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_dialog, container, false);
        String hostUid = FirebaseAuth.getInstance().getUid();
        gameIdTextView = view.findViewById(R.id.gameIdTextView);
        gameId = String.valueOf((int)((Math.random() * ((Constants.MAX_ID - Constants.MIN_ID) + 1)) + Constants.MIN_ID));

        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(hostUid);
        gameDatabaseReference = FirebaseDatabase.getInstance().getReference("/games/" + gameId);

        progressBar = view.findViewById(R.id.progressBar);

        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hostUser = snapshot.getValue(User.class);
                gameIdTextView.setText(gameId);
                Game game = new Game(hostUser, gameId);
                gameDatabaseReference.setValue(game);
                WaitGuestConnection();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void WaitGuestConnection() {
        gameEventListener = gameDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Game game = snapshot.getValue(Game.class);
                if (game.getConnectedUser() != null)
                    StartGame(game);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void StartGame(Game game){
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(this).commit();
        if(gameEventListener != null)
            gameDatabaseReference.removeEventListener(gameEventListener);
        Intent intent = new Intent(getContext(), LobbyActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
      //  DeleteGame();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        DeleteGame();
    }

    private void DeleteGame(){
        if(gameEventListener != null)
            gameDatabaseReference.removeEventListener(gameEventListener);
        gameDatabaseReference.removeValue();
    }
}