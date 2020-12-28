package com.example.battleship.Repositories;

import com.google.firebase.database.DataSnapshot;

public interface FirebaseCallback {
    void onSuccess(DataSnapshot snapshot);
}
