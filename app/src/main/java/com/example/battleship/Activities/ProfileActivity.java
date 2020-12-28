package com.example.battleship.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.battleship.R;
import com.example.battleship.ViewModels.ProfileViewModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity {

    int PICK_IMAGE_ACTION = 1;
    
    SimpleDraweeView profileImageView;
    ImageButton imageUploadButton;
    
    Button signOutButton;
    Button confirmButton;
    
    EditText usernameEdit;
    EditText emailEdit;
    
    SwitchMaterial gravatarSwitch;
    
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Uri selectedPhotoUri;

    ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageUploadButton = findViewById(R.id.uploadImageButton);
        profileImageView = findViewById(R.id.profileImage);
        confirmButton = findViewById(R.id.confirmButton);
        signOutButton = findViewById(R.id.signOutButton);
        gravatarSwitch = findViewById(R.id.gravatarSwitch);
        usernameEdit = findViewById(R.id.usernameEdit);
        emailEdit = findViewById(R.id.emailEdit);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        usernameEdit.setText(currentUser.getDisplayName());
        emailEdit.setText(currentUser.getEmail());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        boolean gravatarUsing = sharedPreferences.getBoolean("gravatarUsing", false);

        profileViewModel = new ProfileViewModel(gravatarUsing, currentUser);

        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable username) {
                profileViewModel.username.setValue(username.toString());
            }
        });

        confirmButton.setOnClickListener(v -> {
            editor.putBoolean("gravatarUsing", profileViewModel.isGravatarUsed.getValue());
            editor.apply();
            profileViewModel.SaveChanges();
            finish();
        });

        profileViewModel.isGravatarUsed.observe(this, isGravatarUsed -> {
            profileViewModel.profileImageUri.setValue(profileViewModel.GetProfileImage());
            if(isGravatarUsed) imageUploadButton.setVisibility(View.INVISIBLE);
            else imageUploadButton.setVisibility(View.VISIBLE);
        }
        );

        profileViewModel.profileImageUri.observe(this,
                s -> profileImageView.setImageURI(Uri.parse(profileViewModel.profileImageUri.getValue())));

        gravatarSwitch.setOnCheckedChangeListener((buttonView, isGravatarUsed) -> {
            profileViewModel.isGravatarUsed.setValue(isGravatarUsed);
        });

        signOutButton.setOnClickListener(v -> AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(task -> {
            Toast.makeText(ProfileActivity.this, "Successfully signed out!", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);

        }));

        imageUploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_ACTION);

        });


        profileImageView.setImageURI(profileViewModel.profileImageUri.getValue());
        gravatarSwitch.setChecked(gravatarUsing);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_ACTION && resultCode == RESULT_OK && data != null){
            selectedPhotoUri = data.getData();
            profileViewModel.profileImageUri.setValue(selectedPhotoUri.toString());
        }
    }

}