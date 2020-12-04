package com.example.battleship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    Button signOutButton;

    EditText usernameEdit;
    EditText emailEdit;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        usernameEdit = findViewById(R.id.usernameEdit);
        emailEdit = findViewById(R.id.emailEdit);

        usernameEdit.setText(currentUser.getDisplayName());
        emailEdit.setText(currentUser.getEmail());
        Uri profileImageUri;
        if(currentUser.getPhotoUrl() != null){
            profileImageUri = Uri.parse(currentUser.getPhotoUrl().toString());
        }
        else{
            profileImageUri = Uri.parse("https://yt3.ggpht.com/a/AATXAJwqEcDwWfOxAKdtQUUSm-lLCopCNoBdoFlTv9BsTA=s900-c-k-c0xffffffff-no-rj-mo");
        }
        SimpleDraweeView draweeView =  findViewById(R.id.profileImage);
        draweeView.setImageURI(profileImageUri);

        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(task -> {
            Toast.makeText(ProfileActivity.this, "Successfully signed out!", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
        }));


    }
}