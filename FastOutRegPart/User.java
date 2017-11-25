package com.example.user.fastoutalpha;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by user on 11/25/2017.
 */

public class User {

    public String email;
    public String password;
    public String fullname;
    public String ic;
    public String username;
    private DatabaseReference mDatabase;

    public User(){

    }
    public User(String username, String email, String password, String ic, String fullname){
        this.username = username;
        this.email = email;
        this.password = password;
        this.ic = ic;
        this.fullname = fullname;
    }

}
