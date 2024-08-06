package com.example.kanji2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kanji2.LocalDatabase.Constants;
import com.example.kanji2.LocalDatabase.PreferenceManager;
import com.example.kanji2.quiz_views.Student_Quiz_DashBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    ImageView logOut;
    Button categoryDashBoard,quizDashBoard;

    TextView textView,instructions;
    FirebaseUser user;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryDashBoard = findViewById(R.id.get_started);
        quizDashBoard = findViewById(R.id.nav_quiz);
        instructions = findViewById(R.id.instruction);
        textView = findViewById(R.id.user_details);
        logOut=findViewById(R.id.btnLogOut);

        preferenceManager = new PreferenceManager(getApplicationContext());

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();


//       String userName = preferenceManager.getString(Constants.KEY_PREFERENCE_USER_NAME);
//        textView.setText(userName);

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
//            textView.setText(user.getEmail());

            String userName = preferenceManager.getString(Constants.KEY_PREFERENCE_USER_NAME);
            textView.setText(userName);
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                preferenceManager.clear();

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

            }
        });

        categoryDashBoard.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Level_Kanji.class);
                startActivity(intent);
                finish();
            }
        });

        quizDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Student_Quiz_DashBoard.class);
                startActivity(intent);
                finish();
            }
        });

        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Instructions.class);
                startActivity(intent);
                finish();
            }
        });
    }
}