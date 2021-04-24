package com.signupsigninsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText edEmailId;
    private Button sendEmailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        edEmailId = findViewById(R.id.edEmailId);
        sendEmailBtn = findViewById(R.id.sendEmailBtn);

        sendEmailBtn.setOnClickListener(v -> {
            if (edEmailId.getText().toString().trim().isEmpty()) {
                edEmailId.setError("Required");
            } else if (!validEmail(edEmailId.getText().toString().trim())) {
                edEmailId.setError("Enter valid e-mail!");
            } else {
                sendEmailBtn.setEnabled(false);
                sendEmail();
                hideKeyboard(v);
            }
        });
    }

    private void sendEmail() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(edEmailId.getText().toString())
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassActivity.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                            sendEmailBtn.setEnabled(true);
                            finish();
                        } else {
                            sendEmailBtn.setEnabled(true);
                            Toast.makeText(ForgotPassActivity.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }
}