package com.example.kanji2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Itchi extends AppCompatActivity {

    ImageView backbutton;
    Button buttonAudio;
    Button buttonAudio2;
    Button buttonAudio3;
    Button buttonAudio4;
    LinearLayout start;
    String letter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itchi);

        if(getIntent()!=null) {
            letter=getIntent().getStringExtra("selectedLevel");
            Toast.makeText(this, "leter"+letter, Toast.LENGTH_SHORT).show();
        }


        backbutton = findViewById(R.id.backbutton);
        buttonAudio = findViewById(R.id.buttonAudio);
        buttonAudio2 = findViewById(R.id.buttonAudio2);
        buttonAudio3 = findViewById(R.id.buttonAudio3);
        buttonAudio4 = findViewById(R.id.buttonAudio4);
        start= findViewById(R.id.navigateGame);


        MediaPlayer mediaPlayer1 = MediaPlayer.create(this,R.raw.itchi);
        MediaPlayer mediaPlayer2 = MediaPlayer.create(this,R.raw.itchi);
        MediaPlayer mediaPlayer3 = MediaPlayer.create(this,R.raw.ichiin);
        MediaPlayer mediaPlayer4 = MediaPlayer.create(this,R.raw.hithothu);

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