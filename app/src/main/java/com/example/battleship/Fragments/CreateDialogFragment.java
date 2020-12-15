package com.example.battleship.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.battleship.Models.Game;
import com.example.battleship.Models.User;
import com.example.battleship.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateDialogFragment extends DialogFragment {
    TextView gameIdTextView;
    ProgressBar progressBar;
    int MIN_ID = 100000;
    int MAX_ID = 1000000;
    User hostUser;
    String gameId;
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
        gameId = String.valueOf((int)((Math.random() * ((MAX_ID - MIN_ID) + 1)) + MIN_ID));

        DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(hostUid);
        DatabaseReference gamesDatabaseReference = FirebaseDatabase.getInstance().getReference("/games/" + gameId);

        progressBar = view.findViewById(R.id.progressBar);

        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hostUser = snapshot.getValue(User.class);
                gameIdTextView.setText(gameId);
                Game game = new Game(hostUser, gameId);
                gamesDatabaseReference.setValue(game);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        DeleteGame();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        DeleteGame();
    }

    private void DeleteGame(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameId);
        databaseReference.removeValue();
    }
}