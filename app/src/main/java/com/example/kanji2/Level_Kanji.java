package com.example.kanji2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Level_Kanji extends AppCompatActivity {
    LinearLayout level01,level02,level03,level04,level05,level06,level07;

    ImageView backbutton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_kanji);

        level01 = findViewById(R.id.btn01);
        level02 = findViewById(R.id.btn02);
        level03 = findViewById(R.id.btn03);
        level04 = findViewById(R.id.btn04);
        level05 = findViewById(R.id.btn05);
        level06 = findViewById(R.id.btn06);
        level07 = findViewById(R.id.btn07);
        backbutton = findViewById(R.id.btnBack);

        level01.setOnClickListener(view -> {
            Intent intent = new Intent(Level_Kanji.this, Numbers.class);
            startActivity(intent);
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
