package com.example.battleship.Repositories;

import android.net.Uri;

import com.example.battleship.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class ProfileRepository {

    public void SaveNickname(User user, ProfileCallback callback){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + user.getUid());
        databaseReference.setValue(user).addOnSuccessListener(aVoid -> {
            callback.onSuccess();
        });
    }

    public void SavePhoto(User user, ProfileCallback callback){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + user.getUid());
        databaseReference.setValue(user).addOnSuccessListener(aVoid -> {
            callback.onSuccess();
        });
    }

    public void UploadPhoto(String uri, StorageUploadCallback callback){
        String filename = UUID.randomUUID().toString();
        StorageReference reference =  FirebaseStorage.getInstance().getReference("/images/" + filename);

        reference.putFile(Uri.parse(uri)).addOnSuccessListener(
                taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        callback.onSuccess(uri);

                    }
                }));
    }

    public void UpdateProfile(FirebaseUser firebaseUser, User user){
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUsername())
                .setPhotoUri(Uri.parse(user.getProfileImageUrl()))
                .build();
        firebaseUser.updateProfile(changeRequest);
    }

}
