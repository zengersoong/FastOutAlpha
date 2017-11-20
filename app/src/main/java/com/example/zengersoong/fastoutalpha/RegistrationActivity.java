package com.example.zengersoong.fastoutalpha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.quickstart.database.models.User;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    EditText Name,Email,Pass,CPass,IC;
    Button button;
    AlertDialog.Builder builder;
    Context context;
    Activity activity = (Activity) this.context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Name = findViewById(R.id.regName);
        Email = findViewById(R.id.regEmail);
        Pass = findViewById(R.id.regPassword);
        CPass = findViewById(R.id.CregPassword);
        IC = findViewById(R.id.regID);
        button = findViewById(R.id.regBut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }


    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = Email.getText().toString();
        String password = Pass.getText().toString();
        String conpassword = CPass.getText().toString();
        String name = Name.getText().toString();
        String IC = IC.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult😠) {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                hideProgressDialog();

                if (task.isSuccessful()) {
                    onAuthSuccess();
                } else {
                    Toast.makeText(SignInActivity.this, "Sign Up Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(Email.getText().toString())) {
            Email.setError("Required");
            result = false;
        } else {
            Email.setError(null);
        }

        if (TextUtils.isEmpty(Pass.getText().toString())) {
            Pass.setError("Required");
            result = false;
        } else {
            Pass.setError(null);
        }
        if (TextUtils.isEmpty(CPass.getText().toString())) {
            CPass.setError("Required");
            result = false;
        } else {
            CPass.setError(null);
        }
        if (TextUtils.isEmpty(Name.getText().toString())) {
            Name.setError("Required");
            result = false;
        } else {
            Name.setError(null);
        }
        if (TextUtils.isEmpty(IC.getText().toString())) {
            IC.setError("Required");
            result = false;
        } else {
            IC.setError(null);
        }
        if (Pass.getText().toString() != CPass.getText().toString()){
            Toast.makeText(RegistrationActivity.this, "Sign Up Failed",
                    Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    private void onAuthSuccess() {

        // Write new user
        writeNewUser(Name.getText().toString(), Email.getText().toString(), Pass.getText().toString(), IC.getText().toString());

        // Go to MainActivity
        activity.finish();
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}