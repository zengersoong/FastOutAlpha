package com.example.user.fastoutalpha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    EditText Name,Email,Username,Pass,CPass,IC;
    Button button;
    AlertDialog.Builder builder;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Name = findViewById(R.id.regName);
        Email = findViewById(R.id.regEmail);
        Pass = findViewById(R.id.regPassword);
        CPass = findViewById(R.id.CregPassword);
        Username = findViewById(R.id.regUsername);
        IC = findViewById(R.id.regID);
        button = findViewById(R.id.regBut);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Name.getText().toString().equals("")||Email.getText().toString().equals("")||Pass.getText().toString().equals("")||CPass.getText().toString().equals("")
                        ||IC.getText().toString().equals("")||Username.getText().toString().equals("")){
                    builder = new AlertDialog.Builder(RegistrationActivity.this);
                    builder.setTitle("Something went wrong");
                    builder.setMessage("All fields have to be filled");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else if (!(Pass.getText().toString().equals(CPass.getText().toString()))){
                    builder = new AlertDialog.Builder(RegistrationActivity.this);
                    builder.setTitle("Something went wrong");
                    builder.setMessage("Passwords do not match");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Pass.setText("");
                            CPass.setText("");
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    final String userEmail = Email.getText().toString();
                    final String fullname = Name.getText().toString();
                    final String password = Pass.getText().toString();
                    final String ic = IC.getText().toString();
                    final String username = Username.getText().toString();
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference users = root.child("users");
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(username)){
                                builder = new AlertDialog.Builder(RegistrationActivity.this);
                                builder.setTitle("Something went wrong");
                                builder.setMessage("User Already Exist");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }else{
                                writeNewUser(userEmail,username,password,ic,fullname);
                                Toast.makeText(RegistrationActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }
    private void writeNewUser(String email, String username, String password, String ic, String fullname){
        User user = new User(username, email, password, ic, fullname);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(username).setValue(user);
    }

}
