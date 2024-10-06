package com.example.kanji2;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Check_Pronounce extends AppCompatActivity {

    ImageButton mic;
    ImageView back;
    TextView showLetter;
    String levelName,letter;
    private Handler handler;
    private boolean isAnimating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_pronounce);

        if (getIntent() != null) {
            levelName = getIntent().getStringExtra("selectedLevel");
            letter = getIntent().getStringExtra("selectedLetter");
        }

        mic = findViewById(R.id.btnSpeaking);
        showLetter=findViewById(R.id.speakTextView);
        back =findViewById(R.id.btnBack);

        handler = new Handler();
        isAnimating = false;

        showLetter.setText(letter);


        mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        startContinuousAnimation();

//                        btnRecordPress(v);
//                        if(!getTime){
//                            getTime=true;
//                            startTime();
//                            resetTime();
//                        }else{
//                        }

//                        if(!isStart){
//                            isStart=true;
//                        }

                        break;
                    case MotionEvent.ACTION_UP:

                        stopContinuousAnimation();

//                        cancelTimer();

                        break;
                }
                return true;

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    private void startContinuousAnimation() {
        if (!isAnimating) {
            isAnimating = true;
            handler.post(animationRunnable);
        }
    }

    private void stopContinuousAnimation() {
        isAnimating = false;
        handler.removeCallbacks(animationRunnable);
    }

    private final Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAnimating) {
                YoYo.with(Techniques.Swing)
                        .duration(1000)
                        .playOn(mic);

                handler.postDelayed(this, 1000);  // Re-run the animation every 1 second
            }
        }
    };

}