package com.signupsigninsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class MainActivity extends AppCompatActivity {

    private TextView txtUsername, txtEmailId, txtGender, txtCountry, txtPhoneNo;
    private ProgressBar progressBar;
    private LinearLayout profileLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button logoutBtn = findViewById(R.id.logout_btn);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmailId = findViewById(R.id.txtEmailId);
        txtGender = findViewById(R.id.txtGender);
        txtCountry = findViewById(R.id.txtCountry);
        txtPhoneNo = findViewById(R.id.txtMobileNo);
        progressBar = findViewById(R.id.progress_bar);
        profileLL = findViewById(R.id.profileLayout);
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            getRetrieve();
        }
    }

    private void getRetrieve() {
        profileLL.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("Users")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (documentSnapshot.get("id").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        txtUsername.setText(documentSnapshot.get("username").toString());
                        txtEmailId.setText(documentSnapshot.get("email_id").toString());
                        txtGender.setText(documentSnapshot.get("gender").toString());
                        txtCountry.setText(documentSnapshot.get("country_name").toString());
                        txtPhoneNo.setText(documentSnapshot.get("phone_no").toString());
                        profileLL.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                profileLL.setVisibility(View.GONE);
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}

