package com.example.kanji2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kanji2.LocalDatabase.Constants;
import com.example.kanji2.Model.Level;
import com.example.kanji2.Model.LevelData;
import com.example.kanji2.ml.Model;

import com.example.kanji2.ml.WrittingModel;
import com.example.kanji2.repository.DaysOfWeekRepository;
import com.example.kanji2.repository.FamilyRepository;
import com.example.kanji2.repository.NumberRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class Check_Letter extends AppCompatActivity {

    ImageView imageView,back;
    LinearLayout erase,checkAnswer;
    String levelName,letter;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private float floatStartX = -1, floatStartY = -1,
            floatEndX = -1, floatEndY = -1;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();
    String userAnswer;
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_letter);

        if(getIntent()!=null) {
            levelName=getIntent().getStringExtra("selectedLevel");
            letter=getIntent().getStringExtra("selectedLetter");

            Toast.makeText(this, "lettter: "+letter, Toast.LENGTH_SHORT).show();

        }

        imageView = findViewById(R.id.writingView);
        erase     =findViewById(R.id.btnErase);
        back     =findViewById(R.id.btnBack);
        checkAnswer=findViewById(R.id.check);

        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();


        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( bitmap == null){
                    Toast.makeText(Check_Letter.this, "Please give a answer !", Toast.LENGTH_SHORT).show();
                    return;
                }

                drawPaintSketchImage();
                userAnswer = classifyImage(bitmap);
                classifyImage(bitmap);

                bitmap = null;
//                Toast.makeText(Check_Letter.this, "Draw Letter :- " + userAnswer, Toast.LENGTH_SHORT).show();
//                System.out.println("You draw letter :- "+userAnswer);

                checkUserAnswer(letter);
                clearBlackBoard();

//                saveBitmapToLocalDirectory(bitmap, "kanji_drawing_1");


                checkAnswer.setBackground(ContextCompat.getDrawable(Check_Letter.this, R.drawable.clickerasebg));
                checkAnswer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkAnswer.setBackground(ContextCompat.getDrawable(Check_Letter.this, R.drawable.block04but));
                    }
                }, 100);
            }


        });

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearBlackBoard();

                erase.setBackground(ContextCompat.getDrawable(Check_Letter.this, R.drawable.clickerasebg));
                erase.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        erase.setBackground(ContextCompat.getDrawable(Check_Letter.this, R.drawable.erasebg));

                    }
                }, 100);
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            case "万":
                if (userAnswer.equals(letter)) {
                    result=true;
                    winNumber();
//                    Toast.makeText(this, "Your are correct ! "+result, Toast.LENGTH_SHORT).show();
                }else {
                    result=false;
                    resetNumber();
//                    Toast.makeText(this, "Try again ! "+result, Toast.LENGTH_SHORT).show();
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
        clearBlackBoard();
        return result;

    }

    public void resetNumber() {
//        Toast.makeText(this, "Try again ! "+result+levelName, Toast.LENGTH_SHORT).show();

        levelDataGetNumber(levelName,letter,false );

        showWrongDialog();

//        Intent i = new Intent(this, WritingLose.class);
//        i.putExtra("LevelName",levelName);
//        startActivity(i);
//        finish();
    }

    public void winNumber() {
//        Toast.makeText(this, "Your are correct ! "+result, Toast.LENGTH_SHORT).show();

        levelDataGetNumber(levelName,letter,true );

        showWinDialog();


    }

    private void levelDataGetNumber(String levelName, String letter, boolean isWin) {
        NumberRepository numberRepository = new NumberRepository();
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("Numbers").document(userID);

        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            LevelData levelData;

            if (documentSnapshot.exists()) {
                // If the document exists, get the current LevelData
                levelData = documentSnapshot.toObject(LevelData.class);
            } else {
                // If no document exists, create a new LevelData object
                levelData = new LevelData();
            }

            // Find the corresponding level and update it based on levelName
            Level level = new Level();
            level.setLevelName(levelName);
            level.setLetter(letter);
            level.setWriteCompleted(isWin);

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
                default:
                    Toast.makeText(this, "Invalid level name", Toast.LENGTH_SHORT).show();
                    break;
            }

            numberRepository.sendGameDataToFirestore(levelData, fStore, this);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load data from Firestore", Toast.LENGTH_SHORT).show();
        });
    }


    public String classifyImage(Bitmap image) {
        try {
            // Load the new model
            WrittingModel model = WrittingModel.newInstance(Check_Letter.this);

            // Resize the image to the new preferred size (128x128)
            int imageSize = 128;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, imageSize, imageSize, true);

            // Create input tensor with shape [1, 128, 128, 3] for RGB images
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);

            // Prepare ByteBuffer for input tensor data
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3); // 3 channels (R, G, B)
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] vals = new int[imageSize * imageSize];
            resizedBitmap.getPixels(vals, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());

            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = vals[pixel++];
                    // Scale to [-1, 1] to match MobileNetV2's expected input range
                    byteBuffer.putFloat((((val >> 16) & 0xFF) / 127.5f) - 1f); // Red
                    byteBuffer.putFloat((((val >> 8) & 0xFF) / 127.5f) - 1f);  // Green
                    byteBuffer.putFloat(((val & 0xFF) / 127.5f) - 1f);         // Blue
                }
            }

            // Load the processed image data into the input tensor
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets the result
            WrittingModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPossibility = 0;
            float maxConfidence = 0;
            boolean isHaveProbability = false;

            // Find the class with the highest confidence score
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPossibility = i;
                    isHaveProbability = true;
                }
            }

//           Toast.makeText(this, "" + maxPossibility, Toast.LENGTH_SHORT).show();
            // Release the model resources
            model.close();

            // Handle the case where there are no confident predictions
            if (!isHaveProbability && confidences[0] == 0)
                return "-1";

            // Return the predicted class name
            return Constants.resultMappedClass[maxPossibility];

        } catch (IOException e) {
            // Handle the exception
            return "-1";
        }
    }


    public void drawPaintSketchImage() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);

            canvas = new Canvas(bitmap);
//            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(25);
        }

        // Draw white line on the black canvas
        canvas.drawLine(floatStartX,
                floatStartY - 220,
                floatEndX,
                floatEndY - 220,
                paint);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }

        return super.onTouchEvent(event);
    }

    public void saveBitmapToLocalDirectory(Bitmap bitmap, String fileName) {
        // Create or get the directory to save the bitmap
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());

        // Get the directory for storing images in the app-specific external directory (under DCIM)
        File directory = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages");

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs();
        }


        // Create a new file with a properly formatted name
        File fileSaveImage = new File(directory, timeStamp + ".png");

        try {
            // Save the bitmap to the file as a PNG
            FileOutputStream fileOutputStream = new FileOutputStream(fileSaveImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            // Clear the canvas (or "blackboard") after saving
            clearBlackBoard();

            // Notify the user that the file was saved successfully
            Toast.makeText(this, "File Saved Successfully: " + fileSaveImage.getAbsolutePath(), Toast.LENGTH_LONG).show();
//            System.out.println("File Saved Successfully: " + fileSaveImage.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private void clearBlackBoard(){
        if (canvas != null) {
            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            imageView.setImageBitmap(null);
        }
    }

    private void showWinDialog(){
        ConstraintLayout D1 = findViewById(R.id.successConstraintLayoutWrite);
        View view = LayoutInflater.from(Check_Letter.this).inflate(R.layout.success_dialog_write, D1);
        Button ButSuccess = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(Check_Letter.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        ButSuccess.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showWrongDialog(){
        ConstraintLayout D1 = findViewById(R.id.wongConstraintLayoutWrite);
        View view = LayoutInflater.from(Check_Letter.this).inflate(R.layout.wrong_dialog_write, D1);
        Button ButSuccess = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(Check_Letter.this);
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

    public void resetFamily() {
//        Toast.makeText(this, "Try again ! "+result+levelName, Toast.LENGTH_SHORT).show();

        levelDataGetFamily(levelName,letter,false );

        showWrongDialog();
    }

    public void winFamily() {
//        Toast.makeText(this, "Your are correct ! "+result, Toast.LENGTH_SHORT).show();

        levelDataGetFamily(levelName,letter,true );

        showWinDialog();
    }

    private void levelDataGetFamily(String levelName, String letter, boolean isWin) {
        FamilyRepository familyRepository = new FamilyRepository();
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("Family").document(userID);

        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            LevelData levelData;

            if (documentSnapshot.exists()) {
                // If the document exists, get the current LevelData
                levelData = documentSnapshot.toObject(LevelData.class);
            } else {
                // If no document exists, create a new LevelData object
                levelData = new LevelData();
            }

            // Find the corresponding level and update it based on levelName
            Level level = new Level();
            level.setLevelName(levelName);
            level.setLetter(letter);
            level.setWriteCompleted(isWin);

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

                default:
                    Toast.makeText(this, "Invalid level name", Toast.LENGTH_SHORT).show();
                    break;
            }

            familyRepository.sendGameDataToFirestore(levelData, fStore, this);
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
        DaysOfWeekRepository daysOfWeekRepository = new DaysOfWeekRepository();
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentRef = fStore.collection("DaysOfWeek").document(userID);

        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            LevelData levelData;

            if (documentSnapshot.exists()) {
                // If the document exists, get the current LevelData
                levelData = documentSnapshot.toObject(LevelData.class);
            } else {
                // If no document exists, create a new LevelData object
                levelData = new LevelData();
            }

            // Find the corresponding level and update it based on levelName
            Level level = new Level();
            level.setLevelName(levelName);
            level.setLetter(letter);
            level.setWriteCompleted(isWin);

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

                default:
                    Toast.makeText(this, "Invalid level name", Toast.LENGTH_SHORT).show();
                    break;
            }

            daysOfWeekRepository.sendGameDataToFirestore(levelData, fStore, this);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load data from Firestore", Toast.LENGTH_SHORT).show();
        });
    }

}