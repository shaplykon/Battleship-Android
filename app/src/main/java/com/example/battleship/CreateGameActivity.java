package com.example.battleship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateGameActivity extends AppCompatActivity {

    TextView gameIdTextView;
    ProgressBar progressBar;
    int MIN_ID = 100000;
    int MAX_ID = 1000000;
    User hostUser;
    String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        String hostUid = FirebaseAuth.getInstance().getUid();

        gameIdTextView = findViewById(R.id.gameIdTextView);
        gameId = String.valueOf((int)((Math.random() * ((MAX_ID - MIN_ID) + 1)) + MIN_ID));

        DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(hostUid);
        DatabaseReference gamesDatabaseReference = FirebaseDatabase.getInstance().getReference("/games/" + gameId);

        progressBar = findViewById(R.id.progressBar);

        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hostUser = snapshot.getValue(User.class);

                progressBar.setVisibility(View.INVISIBLE);
                gameIdTextView.setVisibility(View.VISIBLE);
                gameIdTextView.append(gameId);

                Game game = new Game(hostUser, gameId);
                gamesDatabaseReference.setValue(game);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameId);
        databaseReference.removeValue();

    }
}