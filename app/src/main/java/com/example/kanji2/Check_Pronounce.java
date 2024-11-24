package com.example.kanji2;

import static android.Manifest.permission.RECORD_AUDIO;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.kanji2.LocalDatabase.Constants;
import com.example.kanji2.Model.Level;
import com.example.kanji2.Model.LevelData;
import com.example.kanji2.ResponseModels.SpeakingResponse;
import com.example.kanji2.repository.NumberRepository;
import com.github.squti.androidwaverecorder.WaveRecorder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Check_Pronounce extends AppCompatActivity {

    ImageButton mic;
    ImageView back;
    TextView showLetter;
    LinearLayout checkLetter;
    String levelName,letter;
    private Handler handler;
    private boolean isAnimating;

    private static int Microphone_Permission_Code = 200;
    private WaveRecorder waveRecorder;
    boolean isRecording = false;
    Boolean isStart=false,result;

    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userAnswer;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_pronounce);

        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();


        if (getIntent() != null) {
            levelName = getIntent().getStringExtra("selectedLevel");
            letter = getIntent().getStringExtra("selectedLetter");

//            Toast.makeText(this, "lettter: "+letter, Toast.LENGTH_SHORT).show();
        }
        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }

        mic = findViewById(R.id.btnSpeaking);
        showLetter=findViewById(R.id.speakTextView);
        back =findViewById(R.id.btnBack);
        checkLetter=findViewById(R.id.check);

        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        handler = new Handler();
        isAnimating = false;

        showLetter.setText(letter);


        mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        startContinuousAnimation();

                        btnRecordPress(v);

                        if(!isStart){
                            isStart=true;
                        }

                        break;
                    case MotionEvent.ACTION_UP:

                        stopContinuousAnimation();


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


        checkLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStart){
                    Toast.makeText(Check_Pronounce.this, "Answer Please !", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (isRecording){
                        stopRecording();

                    }
                    isStart = false;
                    sendAPIRequest();


                }


                checkLetter.setBackground(ContextCompat.getDrawable(Check_Pronounce.this, R.drawable.clickerasebg));
                checkLetter.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkLetter.setBackground(ContextCompat.getDrawable(Check_Pronounce.this, R.drawable.block04but));
                    }
                }, 100);


            }
        });

    }



    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }else{
            return false;
        }
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, Microphone_Permission_Code);
        }
    }

    public void btnRecordPress(View view) {

        if (!isRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        waveRecorder = new WaveRecorder(getOutputFilePath());
        waveRecorder.setNoiseSuppressorActive(true);
        waveRecorder.startRecording();

//        Toast.makeText(this, "Recording is started", Toast.LENGTH_SHORT).show();
        isRecording = true;
    }
    private void stopRecording() {
        if (waveRecorder != null) {
            waveRecorder.stopRecording();
            waveRecorder = null;

//            Toast.makeText(this, "Saved Your Recording", Toast.LENGTH_SHORT).show();
            isRecording = false;
        }
    }

    @Override
    protected void onPause() {
        if (waveRecorder != null)
            waveRecorder.pauseRecording();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (waveRecorder != null)
            waveRecorder.resumeRecording();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (waveRecorder != null){
            waveRecorder.stopRecording();
            waveRecorder = null;
        }
        finish();
    }

    private void sendAPIRequest() {
//         Get the audio file path
        File audioFile = new File(getOutputFilePath());

        // Check if the audio file exists
        if (!audioFile.exists()) {
            Toast.makeText(this, "Audio file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request body with the audio file
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("audio", "audio.wav", RequestBody.create(audioFile, MediaType.parse("audio/wav")))
//                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio", "speak_recording_file.wav",
                        RequestBody.create(audioFile, MediaType.parse("audio/wav")))
                .build();

        for (int i = 0; i < ((MultipartBody) requestBody).size(); i++) {
            MultipartBody.Part part = ((MultipartBody) requestBody).part(i);
            Log.d("APIRequest", "Part " + i + " - Name: " + part.headers().get("Content-Disposition"));
            Log.d("APIRequest", "MIME Type: " + part.body().contentType());
//            Log.d("APIRequest", "Content Length: " + part.body().contentLength());
        }


        // Build the request
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/speech") // Updated API endpoint
                .post(requestBody)
                .build();

        // Send the request using OkHttpClient
//        client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int statusCode = response.code();
                String responseBody;

                if (response.body() != null) {
              
                    responseBody = new String(response.body().bytes(), StandardCharsets.UTF_8);
//                    Toast.makeText(Check_Pronounce.this, "Transcription: " + responseBody, Toast.LENGTH_LONG).show();
//                    System.out.println("Result" + responseBody);
                } else {
                    responseBody = null;
                }

                if (response.isSuccessful()) {
                    // Handle the success
                    runOnUiThread(() -> {
                        try {
                            userAnswer=responseBody;
//                            Toast.makeText(Check_Pronounce.this, "Transcription: " + responseBody, Toast.LENGTH_LONG).show();
//                            System.out.println("Result" + responseBody);

                            checkUserAnswer(letter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Check_Pronounce.this, "Failed to parse response", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Log the error details
                    Log.e("APIResponse", "Error code: " + statusCode + ", Body: " + responseBody);
                    runOnUiThread(() -> Toast.makeText(Check_Pronounce.this, "API response error: " + statusCode, Toast.LENGTH_SHORT).show());
                }
            }



            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle network failure
                runOnUiThread(() -> Toast.makeText(Check_Pronounce.this, "API request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        });
    }

    private boolean checkUserAnswer(String letter) {

        switch(letter)
        {
            case "一":
            case "二":
            case "三":
            case "四":
            case "五":
            case "六":
            case "七":
            case "八":
            case "九":
            case "十":
            case "百":
            case "千":
            case "万":
                if (userAnswer.equals(letter)) {
                    result=true;
                    winNumber();

                }else {
                    result=false;
                    resetNumber();

                }
                break;

            case "父":
            case "兄":
            case "姉":
            case "母":
            case "妹":
            case "弟":
                if (userAnswer.equals(letter)) {
                    result=true;
                    winFamily();

                }else {
                    result=false;
                    resetFamily();

                }
                break;

            case "日":
            case "木":
            case "曜":
            case "金":
            case "火":
            case "水":
            case "月":
            case "土":
                if (userAnswer.equals(letter)) {
                    result=true;
                    winDaysOfWeek();

                }else {
                    result=false;
                    resetDaysOfWeek();

                }
                break;

        }
        return result;

    }

    public void resetNumber() {
//        Toast.makeText(this, "Try again ! "+result, Toast.LENGTH_SHORT).show();

        levelDataGetNumber(levelName,letter,false );

        showWrongDialog();

    }
    public void winNumber() {
//        Toast.makeText(this, "Your are correct ! "+result, Toast.LENGTH_SHORT).show();

        levelDataGetNumber(levelName,letter,true );

        showWinDialog();

    }
    private void levelDataGetNumber(String levelName, String letter, boolean isWin) {
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("Numbers").document(userID);

        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            LevelData levelData;

            if (documentSnapshot.exists()) {
                levelData = documentSnapshot.toObject(LevelData.class);
            } else {
                levelData = new LevelData();
            }

            Level level = null;

            switch (levelName) {
                case "level1":
                    level = levelData.getLevel1();
                    break;
                case "level2":
                    level = levelData.getLevel2();
                    break;
                case "level3":
                    level = levelData.getLevel3();
                    break;
                case "level4":
                    level = levelData.getLevel4();
                    break;
                case "level5":
                    level = levelData.getLevel5();
                    break;
                case "level6":
                    level = levelData.getLevel6();
                    break;
                case "level7":
                    level = levelData.getLevel7();
                    break;
                case "level8":
                    level = levelData.getLevel8();
                    break;
                case "level9":
                    level = levelData.getLevel9();
                    break;
                case "level10":
                    level = levelData.getLevel10();
                    break;
                case "level11":
                    level = levelData.getLevel11();
                    break;
                case "level12":
                    level = levelData.getLevel12();
                    break;
                case "level13":
                    level = levelData.getLevel13();
                    break;
                default:
                    Toast.makeText(this, "Invalid level name", Toast.LENGTH_SHORT).show();
                    return;
            }

            if (level == null) {
                level = new Level();
                level.setWriteCompleted(false); // Initialize writeComplete to false for a new level
            }

            // Update fields
            level.setLevelName(levelName);
            level.setLetter(letter);
            level.setSpeakCompleted(isWin);

            // Update writeCompleted only if necessary
            if (!level.isWriteCompleted()) {
                level.setWriteCompleted(false);
            } else {
                level.setWriteCompleted(true);
            }

            // Update the level in LevelData
            switch (levelName) {
                case "level1":
                    levelData.setLevel1(level);
                    break;
                case "level2":
                    levelData.setLevel2(level);
                    break;
                case "level3":
                    levelData.setLevel3(level);
                    break;
                case "level14":
                    levelData.setLevel14(level);
                    break;
                case "level15":
                    levelData.setLevel15(level);
                    break;
                case "level6":
                    levelData.setLevel6(level);
                    break;
                case "level7":
                    levelData.setLevel7(level);
                    break;
                case "level8":
                    levelData.setLevel8(level);
                    break;
                case "level9":
                    levelData.setLevel9(level);
                    break;
                case "level10":
                    levelData.setLevel10(level);
                    break;
                case "level11":
                    levelData.setLevel11(level);
                    break;
                case "level12":
                    levelData.setLevel12(level);
                    break;
                case "level13":
                    levelData.setLevel13(level);
                    break;
            }

            // Save the updated LevelData back to Firestore
            documentRef.set(levelData).addOnSuccessListener(aVoid -> {
//                Toast.makeText(this, "Level updated successfully", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to update level: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load data from Firestore", Toast.LENGTH_SHORT).show();
        });
    }


    public void resetFamily() {

        levelDataGetFamily(levelName,letter,false );

        showWrongDialog();

    }
    public void winFamily() {

        levelDataGetFamily(levelName,letter,true );

        showWinDialog();


    }
    private void levelDataGetFamily(String levelName, String letter, boolean isWin) {
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("Family").document(userID);

        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            LevelData levelData;

            if (documentSnapshot.exists()) {
                levelData = documentSnapshot.toObject(LevelData.class);
            } else {
                levelData = new LevelData();
            }

            Level level = null;

            switch (levelName) {
                case "level1":
                    level = levelData.getLevel1();
                    break;
                case "level2":
                    level = levelData.getLevel2();
                    break;
                case "level3":
                    level = levelData.getLevel3();
                    break;
                case "level4":
                    level = levelData.getLevel4();
                    break;
                case "level5":
                    level = levelData.getLevel5();
                    break;
                case "level6":
                    level = levelData.getLevel6();
                    break;

                default:
                    Toast.makeText(this, "Invalid level name", Toast.LENGTH_SHORT).show();
                    return;
            }

            if (level == null) {
                level = new Level();
                level.setWriteCompleted(false); // Initialize writeComplete to false for a new level
            }

            // Update fields
            level.setLevelName(levelName);
            level.setLetter(letter);
            level.setSpeakCompleted(isWin);

            // Update writeCompleted only if necessary
            if (!level.isWriteCompleted()) {
                level.setWriteCompleted(false);
            } else {
                level.setWriteCompleted(true);
            }

            // Update the level in LevelData
            switch (levelName) {
                case "level1":
                    levelData.setLevel1(level);
                    break;
                case "level2":
                    levelData.setLevel2(level);
                    break;
                case "level3":
                    levelData.setLevel3(level);
                    break;
                case "level14":
                    levelData.setLevel14(level);
                    break;
                case "level15":
                    levelData.setLevel15(level);
                    break;
                case "level6":
                    levelData.setLevel6(level);
                    break;

            }

            // Save the updated LevelData back to Firestore
            documentRef.set(levelData).addOnSuccessListener(aVoid -> {
//                Toast.makeText(this, "Level updated successfully", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to update level: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load data from Firestore", Toast.LENGTH_SHORT).show();
        });
    }


    public void resetDaysOfWeek() {

        levelDataGetDaysOfWeek(levelName,letter,false );

        showWrongDialog();

    }
    public void winDaysOfWeek() {

        levelDataGetDaysOfWeek(levelName,letter,true );

        showWinDialog();

    }
    private void levelDataGetDaysOfWeek(String levelName, String letter, boolean isWin) {
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("DaysOfWeek").document(userID);

        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            LevelData levelData;

            if (documentSnapshot.exists()) {
                levelData = documentSnapshot.toObject(LevelData.class);
            } else {
                levelData = new LevelData();
            }

            Level level = null;

            switch (levelName) {
                case "level1":
                    level = levelData.getLevel1();
                    break;
                case "level2":
                    level = levelData.getLevel2();
                    break;
                case "level3":
                    level = levelData.getLevel3();
                    break;
                case "level4":
                    level = levelData.getLevel4();
                    break;
                case "level5":
                    level = levelData.getLevel5();
                    break;
                case "level6":
                    level = levelData.getLevel6();
                    break;
                case "level7":
                    level = levelData.getLevel7();
                    break;
                case "level8":
                    level = levelData.getLevel8();
                    break;

                default:
                    Toast.makeText(this, "Invalid level name", Toast.LENGTH_SHORT).show();
                    return;
            }

            if (level == null) {
                level = new Level();
                level.setWriteCompleted(false); // Initialize writeComplete to false for a new level
            }

            // Update fields
            level.setLevelName(levelName);
            level.setLetter(letter);
            level.setSpeakCompleted(isWin);

            // Update writeCompleted only if necessary
            if (!level.isWriteCompleted()) {
                level.setWriteCompleted(false);
            } else {
                level.setWriteCompleted(true);
            }

            // Update the level in LevelData
            switch (levelName) {
                case "level1":
                    levelData.setLevel1(level);
                    break;
                case "level2":
                    levelData.setLevel2(level);
                    break;
                case "level3":
                    levelData.setLevel3(level);
                    break;
                case "level14":
                    levelData.setLevel14(level);
                    break;
                case "level15":
                    levelData.setLevel15(level);
                    break;
                case "level6":
                    levelData.setLevel6(level);
                    break;
                case "level7":
                    levelData.setLevel7(level);
                    break;
                case "level8":
                    levelData.setLevel8(level);
                    break;

            }

            // Save the updated LevelData back to Firestore
            documentRef.set(levelData).addOnSuccessListener(aVoid -> {
//                Toast.makeText(this, "Level updated successfully", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to update level: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load data from Firestore", Toast.LENGTH_SHORT).show();
        });
    }


    private String getOutputFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        if (!musicDirectory.exists()) {
            musicDirectory.mkdirs(); // Ensure the directory exists
        }

        File file = new File(musicDirectory, "speak recording file" + ".wav");

        return file.getPath();
    }


    private void showWinDialog(){
        ConstraintLayout D1 = findViewById(R.id.successConstraintLayoutPronounce);
        View view = LayoutInflater.from(Check_Pronounce.this).inflate(R.layout.success_dialog_pronounce, D1);
        Button ButSuccess = view.findViewById(R.id.successDone);


        AlertDialog.Builder builder = new AlertDialog.Builder(Check_Pronounce.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

//                Toast.makeText(Check_Pronounce.this, ""+letter, Toast.LENGTH_SHORT).show();

                finish();
                Intent intent = null;
            switch (letter){
                case "一":
                case "二":
                case "三":
                case "四":
                case "五":
                case "六":
                case "七":
                case "八":
                case "九":
                case "十":
                case "万":
                    intent = new Intent(getApplicationContext(), Numbers.class);
                    break;

                case "父":
                case "兄":
                case "姉":
                case "母":
                case "妹":
                case "弟":
                    intent = new Intent(getApplicationContext(), Family.class);
                    break;

                case "日":
                case "木":
                case "曜":
                case "金":
                case "火":
                case "水":
                case "月":
                case "土":
                    intent = new Intent(getApplicationContext(), DaysOfWeek.class);
                    break;
            }
                startActivity(intent);
            }
        });

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void showWrongDialog(){
        ConstraintLayout D1 = findViewById(R.id.wongConstraintLayoutWrite);
        View view = LayoutInflater.from(Check_Pronounce.this).inflate(R.layout.wrong_dialog_write, D1);
        Button ButSuccess = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(Check_Pronounce.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
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