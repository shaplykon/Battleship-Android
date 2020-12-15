package com.example.battleship.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.battleship.Fragments.ConnectGameFragment;
import com.example.battleship.Fragments.CreateGameFragment;
import com.example.battleship.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    Button signInButton;
    Button profileButton;
    Button connectButton;
    Button createGameButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        connectButton = findViewById(R.id.connectButton);
        createGameButton = findViewById(R.id.createGameButton);

        createGameButton.setOnClickListener(v -> {
            DialogFragment createGameFragment = new CreateGameFragment();
            createGameFragment.show(getSupportFragmentManager(), "createGameFragment");
        });

        connectButton.setOnClickListener(v -> {
            DialogFragment connectGameFragment = new ConnectGameFragment();
            connectGameFragment.show(getSupportFragmentManager(), "connectGameFragment");
        });

        profileButton =  findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v-> {
            if(mAuth.getCurrentUser() != null){
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(v -> {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI();
            } else {
                Toast.makeText(this, "Problem occurred while authenticating", Toast.LENGTH_SHORT).show();
                        
            }
        }
    }
    
    @SuppressLint("SetTextI18n")
    private void updateUI(){

        
    }
}