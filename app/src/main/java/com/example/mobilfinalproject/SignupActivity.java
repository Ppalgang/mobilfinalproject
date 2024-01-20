package com.example.mobilfinalproject;

import android.annotation.SuppressLint;
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

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    private Button signupButton, loginRedirectButton;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginRedirectButton = findViewById(R.id.loginRedirectButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        loginRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToLogin();
            }
        });
    }

    private void signUp() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Lütfen email ve şifre girin", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Kayıt başarılı ise kullanıcıyı yönlendir
                        redirectToMainActivity();
                    } else {
                        // Kayıt başarısız ise kullanıcıyı bilgilendir
                        Toast.makeText(SignupActivity.this, "Kayıt oluşturulamadı.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToLogin() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish(); // Bu aktiviteyi kapat
    }

    private void redirectToMainActivity() {
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish(); // Bu aktiviteyi kapat
    }
}