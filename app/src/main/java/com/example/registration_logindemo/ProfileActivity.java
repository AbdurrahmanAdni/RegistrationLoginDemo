package com.example.registration_logindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextFullName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextOccupation;
    private EditText editTextCity;
    private Button buttonRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile Form");

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);
        editTextOccupation = findViewById(R.id.editTextOccupation);
        editTextCity = findViewById(R.id.editTextCity);
        buttonRegister = findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){
            //user already login

        }
    }

    private void registerUser(){
        final String fullName = editTextFullName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        final String occupation = editTextOccupation.getText().toString().trim();
        final String city = editTextCity.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextFullName.setError("Full Name Required");
            editTextFullName.requestFocus();
            return;
        }

        if(username.isEmpty()){
            editTextUsername.setError("Name Required");
            editTextUsername.requestFocus();
            return;
        }

        if(username.contains(" ")){
            editTextUsername.setError("Username cannot containt spaces");
            editTextUsername.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            editTextConfirmPassword.setError("Confirm Password Required");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if(!confirmPassword.equals(password)){
            Toast.makeText(ProfileActivity.this, password, Toast.LENGTH_LONG).show();
            Toast.makeText(ProfileActivity.this, confirmPassword, Toast.LENGTH_LONG).show();
            editTextConfirmPassword.setError("Must similar to password");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if(occupation.isEmpty()){
            editTextOccupation.setError("Occupetion Required");
            editTextOccupation.requestFocus();
            return;
        }

        if(city.isEmpty()){
            editTextCity.setError("City Required");
            editTextCity.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword((username + "@drtania.com"), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            User user = new User(fullName, occupation, city);

                            FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ProfileActivity.this, "Registration Success", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else{
                                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
