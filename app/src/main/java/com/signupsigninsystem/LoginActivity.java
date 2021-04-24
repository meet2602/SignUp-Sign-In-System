package com.signupsigninsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private EditText edEmailId, edPassword;
    private Button signInBtn, createAccBtn, forgotPassBtn;
    private TextInputLayout txtPasswordL;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmailId = findViewById(R.id.edEmailId);
        edPassword = findViewById(R.id.edPassword);
        signInBtn = findViewById(R.id.signInBtn);
        createAccBtn = findViewById(R.id.createAccBtn);
        forgotPassBtn = findViewById(R.id.forgotPassBtn);
        txtPasswordL = findViewById(R.id.textFieawld1);
        progressBar = findViewById(R.id.progress_bar);
        signInBtn.setOnClickListener(v -> {
            Validation();
            hideKeyboard(v);
        });
        createAccBtn.setOnClickListener(v -> {
           hideKeyboard(v);
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        forgotPassBtn.setOnClickListener(v -> {
            hideKeyboard(v);
            Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
            startActivity(intent);
        });
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edPassword.getText().toString().trim().isEmpty()) {
                    txtPasswordL.setError("Required");
                } else if (edPassword.getText().toString().trim().length() < 8) {
                    txtPasswordL.setError("Password must be 8 character!");
                } else {
                    txtPasswordL.setError(null);
                }
            }
        });

    }
    private void Validation() {
        if (edEmailId.getText().toString().trim().isEmpty()) {
            edEmailId.setError("Required");
        } else if (!validEmail(edEmailId.getText().toString().trim())) {
            edEmailId.setError("Enter valid e-mail!");
        } else if (edPassword.getText().toString().trim().isEmpty()) {
            txtPasswordL.setError("Required");
        } else if (edPassword.getText().toString().trim().length() < 8) {
            txtPasswordL.setError("Password must be 8 character!");
        } else {
            txtPasswordL.setError(null);
            SignIn();
        }
    }

    private boolean validEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void SignIn() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(edEmailId.getText().toString(), edPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }
}