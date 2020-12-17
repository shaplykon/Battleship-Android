package com.example.battleship.Models;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class User implements Serializable {
    public String uid;
    public String username;
    public String profileImageUrl;

    public User(){
        this.uid = "";
        this.username = "";
        this.profileImageUrl = "";
    }

    public User(String uid, String username, String profileImageUrl){
        this.uid = uid;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }


}
