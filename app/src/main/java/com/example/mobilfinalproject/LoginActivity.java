package com.example.mobilfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton, signupRedirectButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectButton = findViewById(R.id.signupRedirectButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signupRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToSignup();
            }
        });
    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Lütfen email ve şifre girin", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Giriş başarılı ise kullanıcıyı yönlendir
                        redirectToMainActivity();
                    } else {
                        // Giriş başarısız ise kullanıcıyı bilgilendir
                        Toast.makeText(LoginActivity.this, "Giriş başarısız.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToSignup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        finish(); // Bu aktiviteyi kapat
    }

    private void redirectToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish(); // Bu aktiviteyi kapat
    }
}