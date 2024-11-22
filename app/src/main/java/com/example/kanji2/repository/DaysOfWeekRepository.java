package com.example.kanji2.repository;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DaysOfWeekRepository {

    String userID;
    FirebaseAuth fAuth;

    public void sendGameDataToFirestore(Object user, FirebaseFirestore firestore, Context context) {
        fAuth       = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        CollectionReference collectionRef = firestore.collection("DaysOfWeek");
        DocumentReference documentRef = collectionRef.document(userID);

        documentRef.set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Data Failed to add", Toast.LENGTH_SHORT).show();
                });

    }
}
