package com.example.battleship.Repositories;

import com.example.battleship.Models.User;
import com.google.firebase.database.DataSnapshot;

public interface FirebaseUserCallback{
    void onUserCallback(User user);
}
