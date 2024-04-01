package com.example.kanji2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class shi extends AppCompatActivity {


    ImageView backbutton;
    Button buttonAudio;
    Button buttonAudio2;
    Button buttonAudio3;
    Button buttonAudio4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shi);

        backbutton = findViewById(R.id.backbutton);
        buttonAudio = findViewById(R.id.buttonAudio);
        buttonAudio2 = findViewById(R.id.buttonAudio2);
        buttonAudio3 = findViewById(R.id.buttonAudio3);
        buttonAudio4 = findViewById(R.id.buttonAudio4);

        MediaPlayer mediaPlayer1 = MediaPlayer.create(this,R.raw.shi);
        MediaPlayer mediaPlayer2 = MediaPlayer.create(this,R.raw.shiki);
        MediaPlayer mediaPlayer3 = MediaPlayer.create(this,R.raw.yonin);
        MediaPlayer mediaPlayer4 = MediaPlayer.create(this,R.raw.shigathu);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Level5.class);
                startActivity(intent);
                finish();
            }
        });



        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Level5.class);
                startActivity(intent);
                finish();
            }
        });

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer1.start();
            }
        });

        buttonAudio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();
            }
        });

        buttonAudio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mediaPlayer3.start();
            }
        });

        buttonAudio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mediaPlayer4.start();
            }
        });

    }

}