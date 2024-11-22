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

    ImageView backbutton,noteButton;
    Button buttonAudio;
    Button writingStart,pronounceStart;
    String level,letter="万";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man);

        if(getIntent()!=null) {
            level=getIntent().getStringExtra("selectedLevel");
        }

        backbutton = findViewById(R.id.backbutton);
        noteButton = findViewById(R.id.notebutton);
        buttonAudio = findViewById(R.id.buttonAudio);
        writingStart = findViewById(R.id.navigateWritingGame);
        pronounceStart=findViewById(R.id.navigateSpeakingGame);

        MediaPlayer mediaPlayer1 = MediaPlayer.create(this,R.raw.man);

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
    }


}