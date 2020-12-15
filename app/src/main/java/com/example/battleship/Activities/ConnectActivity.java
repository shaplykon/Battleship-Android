package com.example.battleship.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.battleship.Adapters.FieldControlsFragment;
import com.example.battleship.Fragments.FieldFragment;
import com.example.battleship.Models.Game;
import com.example.battleship.R;
import com.example.battleship.Models.User;
import com.example.battleship.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectActivity extends AppCompatActivity implements FieldControlsFragment.ControlsInteractionListener {

    TextView idInput;
    TextView  hostTextView;
    TextView guestTextView;

    Button connectButton;

    FragmentManager fragmentManager;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference gamesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ProgressBar progressBar = findViewById(R.id.progressBar2);
        hostTextView = findViewById(R.id.hostTextView);
        guestTextView = findViewById(R.id.guestTextView);
        idInput = findViewById(R.id.idInput);
        connectButton = findViewById(R.id.connectButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        fragmentManager = getSupportFragmentManager();

        connectButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            gamesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("games").child(idInput.getText().toString());

            gamesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Game game = snapshot.getValue(Game.class);

                    if(game.getHostUser() != null){
                        showConnectionDetails(game);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }

    private void showConnectionDetails(Game game){
        game.setConnectedUser(new  User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getPhotoUrl().toString()));
        gamesDatabaseReference.setValue(game);
        ShowField();
        ShowControls();
        hostTextView.setText(game.getHostUser().username);

    }

    @Override
    public void controlInteraction(int action) {
        if (action == Constants.REFRESH_ACTION) {
            HideField();
            ShowField();
        }
    }

    private void ShowField(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                .add(R.id.fieldContainer, new FieldFragment(), Constants.FIELD_FRAGMENT)
                .commit();
    }

    private void HideField(){
        FieldFragment fieldFragment = (FieldFragment) fragmentManager.findFragmentByTag(Constants.FIELD_FRAGMENT);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                .remove(fieldFragment)
                .commit();
    }
    private void ShowControls(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                .add(R.id.controlsContainer, new FieldControlsFragment())
                .commit();
    }

}