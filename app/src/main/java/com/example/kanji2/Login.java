package com.example.kanji2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanji2.LocalDatabase.Constants;
import com.example.kanji2.LocalDatabase.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    TextView textView;
    private PreferenceManager preferenceManager;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser != null){

//            signIn();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
//            Toast.makeText(Login.this, "userID "+currentUser.getUid(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);

        fAuth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());

        preferenceManager.putString(Constants.KEY_USER_ID,fAuth.getUid());

//        String email=  preferenceManager.getString(Constants.KEY_PREFERENCE_EMAIL);
//        if(email != null){
//            signIn();
//        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)||!isValidEmail(email)){
                    editTextEmail.setError( "Enter your Email address! ");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    editTextPassword.setError( "Enter your password! ");
                    return;
                }

                preferenceManager.putString(Constants.KEY_PREFERENCE_EMAIL, String.valueOf(editTextEmail));
                preferenceManager.putString(Constants.KEY_PREFERENCE_PASSWORD, String.valueOf(editTextPassword));

                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    preferenceManager.putString(Constants.KEY_USER_ID,fAuth.getUid());
                                    signIn();

//                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                    startActivity(intent);
//                                    finish();

                                } else {
                                    System.out.println("Exception "+task.getException());
                                    System.out.println("Result "+task.getResult());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    void navigateToDashBoard(){
//        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void signIn() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        System.out.println("User ID"+preferenceManager.getString(Constants.KEY_USER_ID));
        database.collection("students").document(preferenceManager.getString(Constants.KEY_USER_ID))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        preferenceManager.putString(Constants.KEY_PREFERENCE_USER_NAME, documentSnapshot.getString("User Name"));

                        navigateToDashBoard();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("error"+e);
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}