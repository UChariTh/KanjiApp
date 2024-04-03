package com.example.kanji2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Man extends AppCompatActivity {

    ImageView backbutton;
    Button buttonAudio;
    LinearLayout start;
    String letter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man);

        if(getIntent()!=null) {
            letter=getIntent().getStringExtra("selectedLevel");
            Toast.makeText(this, "leter"+letter, Toast.LENGTH_SHORT).show();
        }

        backbutton = findViewById(R.id.backbutton);
        buttonAudio = findViewById(R.id.buttonAudio);
        start= findViewById(R.id.navigateGame);

        MediaPlayer mediaPlayer1 = MediaPlayer.create(this,R.raw.man);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Check_Letter.class);
                intent.putExtra("selectedLevel",letter);
                startActivity(intent);
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Level5.class);
//                startActivity(intent);
                finish();
            }
        });

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer1.start();
            }
        });
    }


}