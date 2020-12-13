package com.example.battleship.Models;

public class User {
    public String uid;
    public String username;
    public String profileImageUrl;

    User(){
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
