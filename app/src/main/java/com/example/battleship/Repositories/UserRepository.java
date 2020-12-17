package com.example.battleship.Repositories;

import androidx.annotation.NonNull;

import com.example.battleship.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {
    public static void GetUserAsynchronous(String uid, FirebaseUserCallback firebaseUserCallback){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("/users/").child(uid);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                firebaseUserCallback.onUserCallback(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        userReference.addListenerForSingleValueEvent(valueEventListener);
    }
}

