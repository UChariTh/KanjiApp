package com.example.kanji2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kanji2.LocalDatabase.Constants;
import com.example.kanji2.ml.HandWrittenTfLiteModelLite;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Check_Letter extends AppCompatActivity {

    ImageView imageView,back;
    LinearLayout erase,checkAnswer;
    String letter;


    private float floatStartX = -1, floatStartY = -1,
            floatEndX = -1, floatEndY = -1;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();
    String userAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_letter);

        if(getIntent()!=null) {
            letter=getIntent().getStringExtra("selectedLevel");
//            Toast.makeText(this, "leter"+letter, Toast.LENGTH_SHORT).show();
        }

        imageView = findViewById(R.id.writingView);
        erase     =findViewById(R.id.btnErase);
        back     =findViewById(R.id.btnBack);
        checkAnswer=findViewById(R.id.check);

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
                System.out.println("You draw letter :- "+userAnswer);
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

    private void checkUserAnswer(String letter) {

        switch(letter)
        {
            case "-":
                if (userAnswer.equals(letter)) {
                    Toast.makeText(this, "Your are correct !", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Try again !", Toast.LENGTH_SHORT).show();
                }
                break;

            case "ニ":

                break;


        }
    }


    public String classifyImage(Bitmap image) {
        try {
            HandWrittenTfLiteModelLite model = HandWrittenTfLiteModelLite.newInstance(Check_Letter.this);

            // Resize the image to the preferred size (150x150)
            int imageSize = 150;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, imageSize, imageSize, true);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 150, 150, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] vals = new int[imageSize * imageSize];
            resizedBitmap.getPixels(vals, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = vals[pixel++]; //FGB values
                    // Uncomment these two lines if that array size {1, 150, 150, 3} like this
//                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.F / 1));
//                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.F / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.F / 1));
                }
            }

            //load the image to tensorflow
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            HandWrittenTfLiteModelLite.Outputs outputs = model.process(inputFeature0);
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
}