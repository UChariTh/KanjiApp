package com.example.kanji2;

import static com.example.kanji2.repository.LockLevels.lockLevel;
import static com.example.kanji2.repository.LockLevels.unlockLevel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kanji2.Model.Level;
import com.example.kanji2.Model.LevelData;
import com.example.kanji2.repository.LockLevels;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Numbers extends AppCompatActivity {

    ImageView backbutton;
    LinearLayout getStart;
    CardView cw1,cw2,cw3,cw4,cw5,cw6,cw7,cw8,cw9,cw10,cw11,cw12,cw13;
    private CardView currentlyHighlightedCard = null;
    int selectedLevel = 1;
    String level = "level";

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

        lockAllLevels();
        retrieveDataFromDatabase();

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
                    level = clickedCard.getTag().toString();

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

        cw1.performClick();


        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLevel(level);

            }
        });

    }

    private void navigateToLevel(String levelTag) {
        Intent intent = null;
        switch (level) {
            case "level1":
                intent = new Intent(getApplicationContext(), Itchi.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level2":
                intent = new Intent(getApplicationContext(), ni.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level3":
                intent = new Intent(getApplicationContext(), sun.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level4":
                intent = new Intent(getApplicationContext(), shi.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level5":
                intent = new Intent(getApplicationContext(), Go.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level6":
                intent = new Intent(getApplicationContext(), roku.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level7":
                intent = new Intent(getApplicationContext(), nana.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level8":
                intent = new Intent(getApplicationContext(), hchi.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level9":
                intent = new Intent(getApplicationContext(), kyu.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level10":
                intent = new Intent(getApplicationContext(), ju.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level11":
                intent = new Intent(getApplicationContext(), Hiyaku.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level12":
                intent = new Intent(getApplicationContext(), Sen.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level13":
                intent = new Intent(getApplicationContext(), Man.class);
                intent.putExtra("selectedLevel",level);
                break;

        }

        if (intent != null && LockLevels.getSelectedNumber(level)<=(selectedLevel+1)) {

            startActivity(intent);
            currentlyHighlightedCard.setBackgroundColor(ContextCompat.getColor(Numbers.this, android.R.color.white));
            currentlyHighlightedCard.setBackground(ContextCompat.getDrawable(Numbers.this, R.drawable.levelcorner));
        } else {
            Toast.makeText(Numbers.this, "Please select Unlocked Level !", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveDataFromDatabase() {
        String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentRef = FirebaseFirestore.getInstance().collection("Numbers").document(userID);
        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                LevelData student = documentSnapshot.toObject(LevelData.class);

                if (student != null){
                    HashMap<Integer, Level> levelHashMap = LockLevels.getLevelsMap(student);
                    for (int i = 13; i>=0; i--){
                        if (levelHashMap.containsKey(i)){
                            if (levelHashMap.get(i).isWriteCompleted() && levelHashMap.get(i).isSpeakCompleted()){
                                unlockLevels(i);
                                selectedLevel = i;
                                break;
                            }
                        }
                    }
                }else {
                    unlockLevels(0);
                }
            }
        });
    }

    private void unlockLevels(int levelNumber){

        switch (levelNumber) {
            case 12:
                unlockLevel(cw13);
            case 11:
                unlockLevel(cw12);
            case 10:
                unlockLevel(cw11);
            case 9:
                unlockLevel(cw10);
            case 8:
                unlockLevel(cw9);
            case 7:
                unlockLevel(cw8);
            case 6:
                unlockLevel(cw7);
            case 5:
                unlockLevel(cw6);
            case 4:
                unlockLevel(cw5);
            case 3:
                unlockLevel(cw4);
            case 2:
                unlockLevel(cw3);
            case 1:
                unlockLevel(cw2);
            default:
                unlockLevel(cw1);

        }
        switch (levelNumber+1) {
            case 14:
            case 13:
                cw13.performClick();
                break;
            case 12:
                cw12.performClick();
                break;
            case 11:
                cw11.performClick();
                break;
            case 10:
                cw10.performClick();
                break;
            case 9:
                cw9.performClick();
                break;
            case 8:
                cw8.performClick();
                break;
            case 7:
                cw7.performClick();
                break;
            case 6:
                cw6.performClick();
                break;
            case 5:
                cw5.performClick();
                break;
            case 4:
                cw4.performClick();
                break;
            case 3:
                cw3.performClick();
                break;
            case 2:
                cw2.performClick();
                break;
            case 1:
            default:
                cw1.performClick();
        }
    }
    private void lockAllLevels() {

        lockLevel(this, cw2);
        lockLevel(this, cw3);
        lockLevel(this, cw4);
        lockLevel(this, cw5);
        lockLevel(this, cw6);
        lockLevel(this, cw7);
        lockLevel(this, cw8);
        lockLevel(this, cw9);
        lockLevel(this, cw10);
        lockLevel(this, cw11);
        lockLevel(this, cw12);
        lockLevel(this, cw13);
    }

}