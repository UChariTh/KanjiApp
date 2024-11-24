package com.example.kanji2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    Spinner userType;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    TextView textView;
    private PreferenceManager preferenceManager;


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = fAuth.getCurrentUser();
        String userTypeFirebase=preferenceManager.getString(Constants.KEY_PREFERENCE_USER_TYPE);
        if(currentUser != null){

            if (userTypeFirebase.equals("Student")){
                navigateStudentDashBoard();

//                Toast.makeText(Login.this, "Student Login "+userTypeFirebase, Toast.LENGTH_SHORT).show();

            }else if(userTypeFirebase.equals("Admin")){
                navigateAdminDashBoard();

//                Toast.makeText(Login.this, "Admin Login", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(Login.this, "error", Toast.LENGTH_SHORT).show();
            }

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
        userType=findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_options, com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        userType.setAdapter(adapter);

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
                String email, password,type;
                email = String.valueOf(editTextEmail.getText()).trim();
                password = String.valueOf(editTextPassword.getText());
                type = userType.getSelectedItem().toString();

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
                preferenceManager.putString(Constants.KEY_PREFERENCE_USER_TYPE, type);

                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    preferenceManager.putString(Constants.KEY_USER_ID,fAuth.getUid());
                                    signIn();

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

    void navigateStudentDashBoard(){
//        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    void navigateAdminDashBoard(){
//        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Admin_Main.class);
        startActivity(intent);
        finish();
    }
    private void signIn() {
        String user=userType.getSelectedItem().toString();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        System.out.println("User ID"+preferenceManager.getString(Constants.KEY_USER_ID));
        database.collection("students").document(preferenceManager.getString(Constants.KEY_USER_ID))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        preferenceManager.putString(Constants.KEY_PREFERENCE_USER_NAME, documentSnapshot.getString("User Name"));
                        preferenceManager.putString(Constants.KEY_PREFERENCE_USER_TYPE, documentSnapshot.getString("User Type"));

                        String userTypeFirebase=documentSnapshot.getString("User Type");

                        if (user.equals("Admin") && userTypeFirebase.equals("Admin")) {
                            navigateAdminDashBoard();
//                            Toast.makeText(Login.this, "Admin Login"+userTypeFirebase, Toast.LENGTH_SHORT).show();
                        } else if (user.equals("Student") && userTypeFirebase.equals("Student")) {
                            navigateStudentDashBoard();
//                            Toast.makeText(Login.this, "Student Login"+userTypeFirebase, Toast.LENGTH_SHORT).show();
                        } else {
                            // Show error message if user types don't match
                            Toast.makeText(Login.this, "User type mismatch", Toast.LENGTH_SHORT).show();
                        }

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