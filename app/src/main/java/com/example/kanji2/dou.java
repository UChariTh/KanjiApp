package com.example.kanji2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class dou extends AppCompatActivity {

    Button backbutton;
    Button buttonAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dou);

        backbutton = findViewById(R.id.backbutton);
        buttonAudio = findViewById(R.id.buttonAudio);

        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.dou);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Level4.class);
                startActivity(intent);
                finish();
            }
        });

        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });
    }
}