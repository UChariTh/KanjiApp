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

public class Check_Letter extends AppCompatActivity {

    ImageView imageView,back;
    LinearLayout erase;
    String letter;


    private float floatStartX = -1, floatStartY = -1,
            floatEndX = -1, floatEndY = -1;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_letter);

        if(getIntent()!=null) {
            letter=getIntent().getStringExtra("selectedLevel");
            Toast.makeText(this, "leter"+letter, Toast.LENGTH_SHORT).show();
        }

        imageView = findViewById(R.id.writingView);
        erase     =findViewById(R.id.btnErase);
        back     =findViewById(R.id.btnBack);

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

    public void drawPaintSketchImage(){

        if (bitmap == null){
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(20);
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