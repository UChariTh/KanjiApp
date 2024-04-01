package com.example.kanji2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Numbers extends AppCompatActivity {

    ImageView backbutton;
    LinearLayout getStart;
    CardView cw1,cw2,cw3,cw4,cw5,cw6,cw7,cw8,cw9,cw10,cw11,cw12,cw13;
    private CardView currentlyHighlightedCard = null;
    int selectedLevel = 1;
    String letter = "letter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        backbutton = findViewById(R.id.backbutton);
        getStart = findViewById(R.id.navigateLetter);
        cw1 = findViewById(R.id.cw01);
        cw2 = findViewById(R.id.cw02);
        cw3 = findViewById(R.id.cw03);
        cw4 = findViewById(R.id.cw04);
        cw5 = findViewById(R.id.cw05);
        cw6 = findViewById(R.id.cw06);
        cw7 = findViewById(R.id.cw07);
        cw8 = findViewById(R.id.cw08);
        cw9 = findViewById(R.id.cw09);
        cw10 = findViewById(R.id.cw10);
        cw11 = findViewById(R.id.cw11);
        cw12 = findViewById(R.id.cw12);
        cw13 = findViewById(R.id.cw13);



        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Level_Kanji.class);
                startActivity(intent);
                finish();
            }
        });

        View.OnClickListener cardClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentlyHighlightedCard != null) {
                    // Reset the background of the previously highlighted CardView
                    currentlyHighlightedCard.setBackgroundColor(ContextCompat.getColor(Numbers.this, android.R.color.white));
                    currentlyHighlightedCard.setBackground(ContextCompat.getDrawable(Numbers.this, R.drawable.levelcorner));
                }

                if (view instanceof CardView) {
                    CardView clickedCard = (CardView) view;
                    letter = clickedCard.getTag().toString();
//                    Toast.makeText(Level1.this, ""+letter, Toast.LENGTH_SHORT).show();

                    // Change the background of the clicked CardView
                    clickedCard.setBackground(ContextCompat.getDrawable(Numbers.this, R.drawable.select_card_view));

                    // Store the clicked CardView as the currently highlighted CardView
                    currentlyHighlightedCard = clickedCard;
                }

            }
        };

        cw1.setOnClickListener(cardClickListener);
        cw2.setOnClickListener(cardClickListener);
        cw3.setOnClickListener(cardClickListener);
        cw4.setOnClickListener(cardClickListener);
        cw5.setOnClickListener(cardClickListener);
        cw6.setOnClickListener(cardClickListener);
        cw7.setOnClickListener(cardClickListener);
        cw8.setOnClickListener(cardClickListener);
        cw9.setOnClickListener(cardClickListener);
        cw10.setOnClickListener(cardClickListener);
        cw11.setOnClickListener(cardClickListener);
        cw12.setOnClickListener(cardClickListener);
        cw13.setOnClickListener(cardClickListener);


        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLevel(letter);

            }
        });

    }

    private void navigateToLevel(String levelTag) {
        Intent intent = null;
        switch (letter) {
            case "-":
                intent = new Intent(getApplicationContext(), Itchi.class);
                intent.putExtra("selectedLevel",letter);
                break;
            case "ニ":
                intent = new Intent(getApplicationContext(), ni.class);
                intent.putExtra("selectedLevel",letter);

                break;
            case "三":
                intent = new Intent(getApplicationContext(), sun.class);
                intent.putExtra("selectedLevel",letter);
                break;

        }

        if (intent != null) {
            startActivity(intent);
            finish(); // Finish this activity to prevent returning to it on back press
        } else {
            Toast.makeText(Numbers.this, "Level interface not found", Toast.LENGTH_SHORT).show();
        }
    }

}