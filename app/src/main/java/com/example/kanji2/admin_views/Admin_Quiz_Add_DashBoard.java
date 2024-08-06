package com.example.kanji2.admin_views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kanji2.Admin_Main;
import com.example.kanji2.Level_Kanji;
import com.example.kanji2.MainActivity;
import com.example.kanji2.Numbers;
import com.example.kanji2.R;

public class Admin_Quiz_Add_DashBoard extends AppCompatActivity {

    LinearLayout Number,Family,level03,level04,level05,level06,level07;
    ImageView backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_quiz_add_dash_board);


        Number = findViewById(R.id.btn01);
        Family = findViewById(R.id.btn02);
        level03 = findViewById(R.id.btn03);
        level04 = findViewById(R.id.btn04);
        level05 = findViewById(R.id.btn05);
        level06 = findViewById(R.id.btn06);
        level07 = findViewById(R.id.btn07);
        backbutton = findViewById(R.id.btnBack);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Admin_Main.class);
                startActivity(intent);
                finish();
            }
        });

        Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNumberQuizzes.class);
                startActivity(intent);

            }
        });
        Family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddFamilyQuizzes.class);
                startActivity(intent);
            }
        });

    }



}