package com.example.user.fastoutalpha;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText Username,Password;
    TextView SignupText;
    Button loginBut;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignupText = findViewById(R.id.signuptext);
        SignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);

        loginBut = findViewById(R.id.loginButton);
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( Username.getText().toString().equals("") || Password.getText().toString().equals("") ){
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("An error has occurred");
                    builder.setMessage("Enter email and password");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    //TODO check account with firebase and login
                    final String user = Username.getText().toString();
                    final String pass = Password.getText().toString();
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference users = root.child("users");
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(user)){
                                String checkPass = dataSnapshot.child(user).child("password").getValue().toString();
                                if(!checkPass.equals(pass)){
                                    builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Something went wrong");
                                    builder.setMessage("Invalid Username or Password");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Password.setText("");
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }else{
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            }else{
                                builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Something went wrong");
                                builder.setMessage("Invalid Username or Password");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Password.setText("");
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
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
}
