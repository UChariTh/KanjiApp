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
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    TextView textView;
    String userID;

    private PreferenceManager preferenceManager;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

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

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

//        buttonReg.setOnClickListener(view -> {
//
//            String email, password;
//            email = String.valueOf(editTextEmail.getText());
//            password = String.valueOf(editTextPassword.getText());
//
//            if (parentname.isEmpty()) {
//                parentName.setError("Enter parent's name ");
//                return;
//            }
//            if (name.isEmpty()) {
//                childName.setError("Enter child's name ");
//                return;
//            }
//            if (age.isEmpty()) {
//                userage.setError("Enter child's age ");
//                return;
//            }
//            try {
//                int ageValue = Integer.parseInt(age);
//
//            } catch (NumberFormatException e) {
//                userage.setError("Enter only numbers ");
//                return;
//            }
//            if (email.isEmpty()) {
//                useremail.setError("Enter your E-mail address ");
//                return;
//            }
//            if (!isValidEmail(email)) {
//                useremail.setError("Please enter valid E-mail address ");
//                return;
//            }
//            if (password.isEmpty()) {
//                userpassword.setError("Enter password ");
//                return;
//            }
//            if (!isValidPassword(password)) {
//                userpassword.setError("You have to enter at least 6 characters");
//                return;
//            }


        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String email=editTextEmail.getText().toString().trim();
                String password=editTextPassword.getText().toString().trim();
                String name = editTextUserName.getText().toString();
                String telephone = editTextTelephone.getText().toString();

                if (TextUtils.isEmpty(email)){
                    editTextEmail.setError("Enter Email Address! ");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Enter Password! ");
                    return;
                }
//                if (!isValidPassword(password)) {
//                    editTextPassword.setError("You have to enter at least 6 characters");
//                    return;
//                }
                if (TextUtils.isEmpty(name)){
                    editTextUserName.setError("Enter Your Name! ");
                    return;
                }
                if (TextUtils.isEmpty(telephone)){
                    editTextTelephone.setError("Enter Your Telephone Number! ");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();

                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    userID=currentUser.getUid();
                                    preferenceManager.putString(Constants.KEY_USER_ID,userID);

                                    DocumentReference documentReference = fStore.collection("students").document(userID);
                                    Map<String, Object> students = new HashMap<>();
                                    students.put("User Name", name);
                                    students.put("Telephone", telephone);
                                    students.put("Email", email);
                                    students.put("UserID", userID);

                                    Toast.makeText(Register.this, "Userrrrr"+preferenceManager.getString(Constants.KEY_USER_ID), Toast.LENGTH_SHORT).show();

                                    documentReference.set(students).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess:  " + userID));


                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

    }

//    private boolean isValidEmail(String email) {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    private boolean isValidPassword(String password) {
//        return password.length() >= 6;
//    }
}