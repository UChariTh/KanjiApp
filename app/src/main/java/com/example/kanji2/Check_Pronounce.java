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
import com.example.kanji2.ResponseModels.SpeakingResponse;
import com.github.squti.androidwaverecorder.WaveRecorder;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
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
    Boolean isStart=false;

    String userAnswer;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_pronounce);

//        client = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .build();


        if (getIntent() != null) {
            levelName = getIntent().getStringExtra("selectedLevel");
            letter = getIntent().getStringExtra("selectedLetter");
        }
        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }

        mic = findViewById(R.id.btnSpeaking);
        showLetter=findViewById(R.id.speakTextView);
        back =findViewById(R.id.btnBack);
        checkLetter=findViewById(R.id.check);

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

//                        if(!getTime){
//                            getTime=true;
//                            startTime();
//                            resetTime();
//                        }else{
//                        }

                        if(!isStart){
                            isStart=true;
                        }

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

//                showWinDialog();
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

        Toast.makeText(this, "Recording is started", Toast.LENGTH_SHORT).show();
        isRecording = true;
    }
    private void stopRecording() {
        if (waveRecorder != null) {
            waveRecorder.stopRecording();
            waveRecorder = null;

            Toast.makeText(this, "Saved Your Recording", Toast.LENGTH_SHORT).show();
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

//    private void sendAPIRequest() {
//        // Get the audio file path
//        File audioFile = new File(getOutputFilePath());
//
//        // Check if the audio file exists
//        if (!audioFile.exists()) {
//            Toast.makeText(this, "Audio file not found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Create a request body with the audio file
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("audio", "audio.wav", RequestBody.create(audioFile, MediaType.parse("audio/wav")))
//                .build();
//
//        // Build the request
//        Request request = new Request.Builder()
//                .url(Constants.BASE_URL + "/speech") // Updated API endpoint
//                .post(requestBody)
//                .build();
//
//        // Send the request using OkHttpClient
//        client = new OkHttpClient();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful() && response.body() != null) {
//                    // Parse and handle the response on the main thread
//                    String responseBody = response.body().string();
//                    runOnUiThread(() -> {
//                        try {
////                            SpeakingResponse speakingResponse = new Gson().fromJson(responseBody, SpeakingResponse.class);
////                            userAnswer = speakingResponse.getNumber();
//                            Toast.makeText(Check_Pronounce.this, "Transcription: " + responseBody, Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Toast.makeText(Check_Pronounce.this, "Failed to parse response", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    // Handle API error
//                    runOnUiThread(() -> Toast.makeText(Check_Pronounce.this, "API response error", Toast.LENGTH_SHORT).show());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                // Handle network failure
//                runOnUiThread(() -> Toast.makeText(Check_Pronounce.this, "API request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                e.printStackTrace();
//            }
//        });
//    }


    private void sendAPIRequest() {
        // Define the API URL with the correct IP address
        String apiURL = "http://192.168.8.116:8000/speech";  // Fix the URL syntax here
        File audioFile = new File(getOutputFilePath());

        // Check if the file exists
        if (!audioFile.exists()) {
            Toast.makeText(this, "Audio file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the request body for the file
        RequestBody fileBody = RequestBody.create(MediaType.parse("audio/wav"), audioFile);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio", audioFile.getName(), fileBody)
                .build();

        // Initialize OkHttpClient with a timeout
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(apiURL)
                .post(requestBody)
                .build();

        // Make the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle the transcription result
                    String transcription = response.body().string();
                    runOnUiThread(() -> Toast.makeText(Check_Pronounce.this, "Transcription: " + transcription, Toast.LENGTH_LONG).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(Check_Pronounce.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("API Request", "Failed to connect: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(Check_Pronounce.this, "API Request Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

        });
    }



    private String getOutputFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
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
                case "七":
                case "九":
                case "ニ":
                case "三":
                case "五":
                    intent = new Intent(getApplicationContext(), Numbers.class);

                    break;
                case "a":

            }
                startActivity(intent);
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