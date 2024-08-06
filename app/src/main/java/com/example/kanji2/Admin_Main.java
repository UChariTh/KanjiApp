package com.example.kanji2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kanji2.LocalDatabase.Constants;
import com.example.kanji2.LocalDatabase.PreferenceManager;
import com.example.kanji2.admin_views.Admin_Quiz_Add_DashBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Admin_Main extends AppCompatActivity {

    FirebaseAuth fAuth;

    ImageView logOut;


    Button navStartQuiz;
    FirebaseUser user;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logOut=findViewById(R.id.btnLogOut);
        navStartQuiz=findViewById(R.id.get_started);

        preferenceManager = new PreferenceManager(getApplicationContext());

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        navStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Admin_Quiz_Add_DashBoard.class);
                startActivity(intent);
                finish();
            }
        });

//        if (user == null){
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            startActivity(intent);
//            finish();
//        }
//        else {
////            textView.setText(user.getEmail());
//
////            String userName = preferenceManager.getString(Constants.KEY_PREFERENCE_USER_NAME);
////            textView.setText(userName);
//        }

        logOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            preferenceManager.clear();

            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });
    }
}