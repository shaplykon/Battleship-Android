package com.example.battleship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateGameActivity extends AppCompatActivity {

    TextView gameIdTextView;

    int MIN_ID = 100000;
    int MAX_ID = 1000000;
    User hostUser;
    String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        String hostUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(hostUid);

        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hostUser= snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gameIdTextView = findViewById(R.id.gameIdTextView);
        gameId = String.valueOf((int)((Math.random() * ((MAX_ID - MIN_ID) + 1)) + MIN_ID));
        gameIdTextView.append(gameId);

        DatabaseReference gamesDatabaseReference = FirebaseDatabase.getInstance().getReference("/games/" + gameId);

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(v -> {
            Game game = new Game(hostUser, gameId);
            gamesDatabaseReference.setValue(game);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameId);
        databaseReference.removeValue();

    }
}