package com.example.kanji2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kanji2.LocalDatabase.Constants;
import com.example.kanji2.Model.Level;
import com.example.kanji2.Model.LevelData;
import com.example.kanji2.ml.Model;

import com.example.kanji2.repository.NumberRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

//            Toast.makeText(this, "leter"+letter, Toast.LENGTH_SHORT).show();
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
                bitmap = null;
//                Toast.makeText(Check_Letter.this, "Draw Letter :- " + userAnswer, Toast.LENGTH_SHORT).show();
//                System.out.println("You draw letter :- "+userAnswer);
                checkUserAnswer(letter);


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
            case "七":
            case "九":
            case "ニ":
            case "三":
            case "五":
                if (userAnswer.equals(letter)) {
                    result=true;
                    winNumber();
//                    Toast.makeText(this, "Your are correct ! "+result, Toast.LENGTH_SHORT).show();
                }else {
                    result=false;
                    resetNumber();
//                    Toast.makeText(this, "Try again ! "+result, Toast.LENGTH_SHORT).show();
                }
                clearBlackBoard();
                break;
            case "a":
//                win();
                break;
        }
        return result;

    }

    public void resetNumber() {
        Toast.makeText(this, "Try again ! "+result+levelName, Toast.LENGTH_SHORT).show();

        levelDataGet(levelName,letter,false );

        showWrongDialog();

//        Intent i = new Intent(this, WritingLose.class);
//        i.putExtra("LevelName",levelName);
//        startActivity(i);
//        finish();
    }

    public void winNumber() {
        Toast.makeText(this, "Your are correct ! "+result, Toast.LENGTH_SHORT).show();

        levelDataGet(levelName,letter,true );

        showWinDialog();


    }

    private void levelDataGet(String levelName, String letter, boolean isWin) {
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
            Model model = Model.newInstance(Check_Letter.this);


            // Resize the image to the preferred size (64x64)
            int imageSize = 64;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, imageSize, imageSize, true);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 64, 64, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] vals = new int[imageSize * imageSize];
            resizedBitmap.getPixels(vals, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = vals[pixel++]; //FGB values
                    // Uncomment these two lines if that array size {1, 64, 64, 1} like this
//                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.F / 1));
//                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.F / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.F / 1));
                }
            }

            //load the image to tensorflow
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);

            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();
            int maxPossibility = 0;
            float maxConfidence = 0;
            boolean isHaveProbability = false;

            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPossibility = i;
                    isHaveProbability = true;
                }
            }
            Toast.makeText(this, ""+maxPossibility, Toast.LENGTH_SHORT).show();
//            System.out.println("result : "+ maxPossibility);
//            System.out.println("result : "+ Constants.resultMappedClass[maxPossibility]);
            model.close();

            if (!isHaveProbability && confidences[0] == 0)
                return "-1";

            return Constants.resultMappedClass[maxPossibility];


        } catch (IOException e) {
            // TODO Handle the exception
//            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return "-1";
        }
    }

//    public String classifyImage(Bitmap image) {
//        try {
//            // Load the new model
//            Modelnew model = Modelnew.newInstance(Check_Letter.this);
//
//            // Resize the image to the preferred size (64x64)
//            int imageSize = 64;
//            Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, imageSize, imageSize, true);
//
//            // Creates inputs for reference.
//            // The input shape is now [1, 64, 64, 3] to handle RGB images.
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 64, 64, 3}, DataType.FLOAT32);
//
//            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3); // 3 channels (R, G, B)
//            byteBuffer.order(ByteOrder.nativeOrder());
//
//            int[] vals = new int[imageSize * imageSize];
//            resizedBitmap.getPixels(vals, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
//
//            int pixel = 0;
//            for (int i = 0; i < imageSize; i++) {
//                for (int j = 0; j < imageSize; j++) {
//                    int val = vals[pixel++]; // ARGB values
//                    // Put R, G, B values into the ByteBuffer
//                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.F / 255)); // Red channel
//                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.F / 255));  // Green channel
//                    byteBuffer.putFloat((val & 0xFF) * (1.F / 255));         // Blue channel
//                }
//            }
//
//            // Load the processed image data into the input tensor
//            inputFeature0.loadBuffer(byteBuffer);
//
//            // Runs model inference and gets the result.
//            Modelnew.Outputs outputs = model.process(inputFeature0);
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//
//            float[] confidences = outputFeature0.getFloatArray();
//            int maxPossibility = 0;
//            float maxConfidence = 0;
//            boolean isHaveProbability = false;
//
//            // Find the class with the highest confidence score
//            for (int i = 0; i < confidences.length; i++) {
//                if (confidences[i] > maxConfidence) {
//                    maxConfidence = confidences[i];
//                    maxPossibility = i;
//                    isHaveProbability = true;
//                }
//            }
//            Toast.makeText(this, ""+maxPossibility, Toast.LENGTH_SHORT).show();
//            // Release the model resources
//            model.close();
//
//            // Handle the case where there are no confident predictions
//            if (!isHaveProbability && confidences[0] == 0)
//                return "-1";
//
//            // Return the predicted class name
//            return Constants.resultMappedClass[maxPossibility];
//
//
//        } catch (IOException e) {
//            // TODO Handle the exception
//            return "-1";
//        }
//    }




    public void drawPaintSketchImage(){

        if (bitmap == null){
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
        }
        canvas.drawLine(floatStartX,
                floatStartY-300,
                floatEndX,
                floatEndY-300,
                paint);
        imageView.setImageBitmap(bitmap);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }

        return super.onTouchEvent(event);
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

}