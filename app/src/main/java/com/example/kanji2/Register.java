package com.example.kanji2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword,editTextUserName,editTextTelephone;
    Button buttonReg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    TextView textView;
    String userID;

    private PreferenceManager preferenceManager;

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextUserName = findViewById(R.id.userName);
        editTextTelephone = findViewById(R.id.telephoneNumber);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        preferenceManager = new PreferenceManager(getApplicationContext());

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            String name = editTextUserName.getText().toString();
            String telephone = editTextTelephone.getText().toString();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String userType = "Student";

            if (TextUtils.isEmpty(name)) {
                progressBar.setVisibility(View.GONE);
                editTextUserName.setError("Enter Your Name!");
                return;
            }

            if (TextUtils.isEmpty(telephone)) {
                progressBar.setVisibility(View.GONE);
                editTextTelephone.setError("Enter Your Telephone Number!");
                return;
            }

            try {
                Integer.parseInt(telephone);
            } catch (NumberFormatException e) {
                progressBar.setVisibility(View.GONE);
                editTextTelephone.setError("Enter only numbers!");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                progressBar.setVisibility(View.GONE);
                editTextEmail.setError("Enter Email Address!");
                return;
            }

            if (!isValidEmail(email)) {
                progressBar.setVisibility(View.GONE);
                editTextEmail.setError("Please enter a valid E-mail address!");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.GONE);
                editTextPassword.setError("Enter Password!");
                return;
            }

            if (!isValidPassword(password)) {
                progressBar.setVisibility(View.GONE);
                editTextPassword.setError("You have to enter at least 6 characters!");
                return;
            }

            fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Account Created.", Toast.LENGTH_SHORT).show();

                            userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                            preferenceManager.putString(Constants.KEY_USER_ID, userID);
                            preferenceManager.putString(Constants.KEY_PREFERENCE_USER_TYPE,userType );

                            DocumentReference documentReference = fStore.collection("students").document(userID);
                            Map<String, Object> students = new HashMap<>();
                            students.put("User Name", name);
                            students.put("Telephone", telephone);
                            students.put("Email", email);
                            students.put("UserID", userID);
                            students.put("User Type", userType);

                            documentReference.set(students)
                                    .addOnSuccessListener(unused -> Log.d(TAG, "onSuccess: " + userID))
                                    .addOnFailureListener(e -> Log.e(TAG, "onFailure: Firestore save failed", e));

                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.putExtra("fromRegister", true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}