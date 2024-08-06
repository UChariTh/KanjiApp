package com.example.kanji2.quiz_views;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.kanji2.Model.QuizListModel;
import com.example.kanji2.R;
import com.example.kanji2.viewmodel.QuizListViewModel;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class Number_Quiz_Details extends AppCompatActivity {

    private TextView title , difficulty , totalQuestions;
    private Button startQuizBtn;
    private ProgressBar progressBar;
    private QuizListViewModel viewModel;
    private ImageView topicImage;
    private String quizId;
    private long totalQueCount;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_number_quiz_details);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(QuizListViewModel.class);

        title = findViewById(R.id.detailFragmentTitle);
        difficulty =findViewById(R.id.detailFragmentDifficulty);
        totalQuestions = findViewById(R.id.detailFragmentQuestions);
        startQuizBtn = findViewById(R.id.startQuizBtn);
        progressBar = findViewById(R.id.detailProgressBar);
        topicImage =findViewById(R.id.detailFragmentImage);




    }


}