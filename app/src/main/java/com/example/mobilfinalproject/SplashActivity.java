package com.example.mobilfinalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Kullanıcı oturum açmışsa ana menüye yönlendir
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Kullanıcı oturum açmış, ana menüye yönlendir
            startMainMenuActivity();
        } else {
            // Kullanıcı oturum açmamışsa, splash ekranını göster
            setContentView(R.layout.activity_splash);
            setupLoginScreen();
        }
    }

    private void setupLoginScreen() {
        // Giriş ekranını göster
        findViewById(R.id.loginButton).setOnClickListener(v -> {
            // Giriş ekranına yönlendir
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish(); // Bu aktiviteyi kapat
        });

        findViewById(R.id.signupButton).setOnClickListener(v -> {
            // Kayıt ekranına yönlendir
            startActivity(new Intent(SplashActivity.this, SignupActivity.class));
            finish(); // Bu aktiviteyi kapat
        });
    }

    private void startMainMenuActivity() {
        // Ana menü aktivitesine yönlendir
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish(); // Bu aktiviteyi kapat
    }
}