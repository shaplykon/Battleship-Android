package com.example.battleship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import kotlinx.coroutines.internal.ConcurrentKt;

public class ConnectActivity extends AppCompatActivity {

    TextView idInput;
    Button connectButton;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference gamesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ProgressBar progressBar = findViewById(R.id.progressBar2);
        idInput = findViewById(R.id.idInput);
        connectButton = findViewById(R.id.connectButton);
        TextView  textView = findViewById(R.id.textView2);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();




        connectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                gamesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("games").child(idInput.getText().toString());


                gamesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Game game = snapshot.getValue(Game.class);
                        textView.append(game.hostUser.username);
                        game.connectedUser = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getPhotoUrl().toString());
                        gamesDatabaseReference.setValue(game);
                        SimpleDraweeView hostView =  findViewById(R.id.hostImage);
                        SimpleDraweeView connectedView = findViewById(R.id.connectedImage);
                        connectedView.setImageURI(Uri.parse(game.connectedUser.profileImageUrl));
                        hostView.setImageURI(Uri.parse(game.hostUser.profileImageUrl));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}