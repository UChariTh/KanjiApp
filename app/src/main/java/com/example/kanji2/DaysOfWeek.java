package com.example.kanji2;

import static com.example.kanji2.repository.LockLevels.lockLevel;
import static com.example.kanji2.repository.LockLevels.unlockLevel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kanji2.Model.Level;
import com.example.kanji2.Model.LevelData;
import com.example.kanji2.repository.LockLevels;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DaysOfWeek extends AppCompatActivity {

    ImageView backbutton;
    LinearLayout getStart;
    CardView cw1,cw2,cw3,cw4,cw5,cw6,cw7,cw8;
    private CardView currentlyHighlightedCard = null;
    int selectedLevel = 1;
    String level = "level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_days_of_week);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
                    currentlyHighlightedCard.setBackgroundColor(ContextCompat.getColor(DaysOfWeek.this, android.R.color.white));
                    currentlyHighlightedCard.setBackground(ContextCompat.getDrawable(DaysOfWeek.this, R.drawable.levelcorner));
                }

                if (view instanceof CardView) {
                    CardView clickedCard = (CardView) view;
                    level = clickedCard.getTag().toString();

                    // Change the background of the clicked CardView
                    clickedCard.setBackground(ContextCompat.getDrawable(DaysOfWeek.this, R.drawable.select_card_view));

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
                intent = new Intent(getApplicationContext(), Ki.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level2":
                intent = new Intent(getApplicationContext(), Sui.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level3":
                intent = new Intent(getApplicationContext(), Do.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level4":
                intent = new Intent(getApplicationContext(), Kin.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level5":
                intent = new Intent(getApplicationContext(), Ka.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level6":
                intent = new Intent(getApplicationContext(), Gethu.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level7":
                intent = new Intent(getApplicationContext(), Nichi.class);
                intent.putExtra("selectedLevel",level);
                break;
            case "level8":
                intent = new Intent(getApplicationContext(), Shu.class);
                intent.putExtra("selectedLevel",level);
                break;

        }


        if (intent != null && LockLevels.getSelectedNumber(level)<=(selectedLevel+1)) {

            startActivity(intent);
            currentlyHighlightedCard.setBackgroundColor(ContextCompat.getColor(DaysOfWeek.this, android.R.color.white));
            currentlyHighlightedCard.setBackground(ContextCompat.getDrawable(DaysOfWeek.this, R.drawable.levelcorner));
        } else {
            Toast.makeText(DaysOfWeek.this, "Please select Unlocked Level !", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveDataFromDatabase() {
        String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentRef = FirebaseFirestore.getInstance().collection("DaysOfWeek").document(userID);
        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                LevelData student = documentSnapshot.toObject(LevelData.class);

                if (student != null){
                    HashMap<Integer, Level> levelHashMap = LockLevels.getLevelsMap(student);
                    for (int i = 8; i>=0; i--){
                        if (levelHashMap.containsKey(i)){
                            if (levelHashMap.get(i).isWriteCompleted() && levelHashMap.get(i).isSpeakCompleted() ){
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
            case 9:
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
    }

}