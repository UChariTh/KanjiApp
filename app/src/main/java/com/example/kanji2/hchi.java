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

public class hchi extends AppCompatActivity {


    ImageView backbutton,noteButton;
    Button buttonAudio;
    Button buttonAudio2;
    Button buttonAudio3;
    Button buttonAudio4;
    Button writingStart,pronounceStart;
    String level,letter="一";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hchi);

        if(getIntent()!=null) {
            level=getIntent().getStringExtra("selectedLevel");
        }

        backbutton = findViewById(R.id.backbutton);
        noteButton = findViewById(R.id.notebutton);
        buttonAudio = findViewById(R.id.buttonAudio);
        buttonAudio2 = findViewById(R.id.buttonAudio2);
        buttonAudio3 = findViewById(R.id.buttonAudio3);
        buttonAudio4 = findViewById(R.id.buttonAudio4);
        writingStart = findViewById(R.id.navigateWritingGame);
        pronounceStart=findViewById(R.id.navigateSpeakingGame);

        MediaPlayer mediaPlayer1 = MediaPlayer.create(this,R.raw.hachi);
        MediaPlayer mediaPlayer2 = MediaPlayer.create(this,R.raw.juhachi);
        MediaPlayer mediaPlayer3 = MediaPlayer.create(this,R.raw.hachigatsu);
        MediaPlayer mediaPlayer4 = MediaPlayer.create(this,R.raw.hachiji);

        writingStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Check_Letter.class);
                intent.putExtra("selectedLevel",level);
                intent.putExtra("selectedLetter",letter);

                startActivity(intent);


            }
        });
        pronounceStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Check_Pronounce.class);
                intent.putExtra("selectedLevel",level);
                intent.putExtra("selectedLetter",letter);

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

        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Note.class);
                startActivity(intent);
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