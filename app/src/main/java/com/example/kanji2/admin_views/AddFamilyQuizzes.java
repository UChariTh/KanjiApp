package com.example.kanji2.admin_views;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kanji2.LocalDatabase.Constants;
import com.example.kanji2.LocalDatabase.PreferenceManager;
import com.example.kanji2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFamilyQuizzes extends AppCompatActivity {

    TextInputEditText question, answer, op1, op2, op3;
    Button addQuiz;
    ProgressBar progressBar;
    TextView quizCount;
    ImageView backButton;
    String category = "Family";
    private int quizNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_family_quizzes);

        question = findViewById(R.id.txtQuestion);
        answer = findViewById(R.id.txtAnswer);
        op1 = findViewById(R.id.txtOption1);
        op2 = findViewById(R.id.txtOption2);
        op3 = findViewById(R.id.txtOption3);
        addQuiz = findViewById(R.id.btnAddQuiz);
        quizCount = findViewById(R.id.txtQuizCount);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backbutton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        quizNumber = 0;
        if (quizNumber == 0) {
            quizNumber = 1;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference quizDocRef = db.collection("quizzes").document(category);

        quizDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists() && documentSnapshot.contains("questions")) {
                    Long firestoreQuizNumber = documentSnapshot.getLong("questions");
                    if (firestoreQuizNumber != null) {
                        quizNumber = Math.max(quizNumber, firestoreQuizNumber.intValue() + 1);
                    }
                    quizCount.setText("Quiz " + quizNumber);

                } else {
                    quizCount.setText("Quiz " + quizNumber);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("AddFamilyQuizzes", "Error getting document", e);
                quizCount.setText("Quiz " + quizNumber);

            }
        });


        CollectionReference quizzesRef = quizDocRef.collection("questions");

        addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String addQuestion = question.getText().toString();
                String addAnswer = answer.getText().toString();
                String addOption1 = op1.getText().toString();
                String addOption2 = op2.getText().toString();
                String addOption3 = op3.getText().toString();

                if (TextUtils.isEmpty(addQuestion)) {
                    question.setError("Please Enter The Question! ");
                    return;
                }
                if (TextUtils.isEmpty(addAnswer)) {
                    answer.setError("Please Enter The Answer! ");
                    return;
                }
                if (TextUtils.isEmpty(addOption1)) {
                    op1.setError("Please Enter The Option 01 ! ");
                    return;
                }
                if (TextUtils.isEmpty(addOption2)) {
                    op2.setError("Please Enter The Option 02 ! ");
                    return;
                }
                if (TextUtils.isEmpty(addOption3)) {
                    op3.setError("Please Enter The Option 03 ! ");
                    return;
                }

                List<String> options = new ArrayList<>();
                options.add(addOption1);
                options.add(addOption2);
                options.add(addOption3);
                options.add(addAnswer);

                // Shuffle the options
                Collections.shuffle(options);

                // Create a new question map
                Map<String, Object> questionData = new HashMap<>();
                questionData.put("question", addQuestion);
                questionData.put("option_a", options.get(0));
                questionData.put("option_b", options.get(1));
                questionData.put("option_c", options.get(2));
                questionData.put("option_d", options.get(3));
                questionData.put("answer", addAnswer);
                questionData.put("timer", 10);

                quizzesRef.add(questionData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference questionDocumentReference) {
                                Log.d("AddQuiz", "Question added with ID: " + questionDocumentReference.getId());
                                Toast.makeText(AddFamilyQuizzes.this, "Quiz question added successfully", Toast.LENGTH_SHORT).show();
                                updateQuizCount();
                                clearTextBoxes();

                                // Update the title and questions fields in the quiz document
                                DocumentReference quizDocRef = db.collection("quizzes").document(category);
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("title", category);
                                updates.put("questions",quizNumber-1);
                                quizDocRef.update(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("AddQuiz", "Quiz document updated successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("AddQuiz", "Error updating quiz document", e);
                                            }
                                        });

                                progressBar.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AddQuiz", "Error adding question", e);
                                Toast.makeText(AddFamilyQuizzes.this, "Error adding quiz question", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    private void updateQuizCount() {
        quizNumber++;
        quizCount.setText("Quiz " + quizNumber);

    }

    private void clearTextBoxes() {
        question.setText(null);
        answer.setText(null);
        op1.setText(null);
        op2.setText(null);
        op3.setText(null);

        question.requestFocus();
    }
}