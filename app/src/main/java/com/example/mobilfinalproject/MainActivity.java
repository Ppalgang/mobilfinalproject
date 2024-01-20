package com.example.mobilfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton, addLabelButton, addPhotoButton, galleryButton, aboutButton, backmenuButton;
    private FrameLayout fragmentContainer;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Firebase kullanıcısı oturum açmışsa ana ekranı göster, aksi takdirde SplashActivity'yi göster
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        } else {
            // Kullanıcı oturum açtıysa UI öğelerini başlat
            initializeUI();
        }
    }

    private void initializeUI() {
        logoutButton = findViewById(R.id.logout);
        addLabelButton = findViewById(R.id.addlabel);
        addPhotoButton = findViewById(R.id.addphoto);
        galleryButton = findViewById(R.id.galery);
        aboutButton = findViewById(R.id.about);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        backmenuButton = findViewById(R.id.backmenu);

        findViewById(R.id.backmenu).setVisibility(View.GONE);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Firebase hesabından çıkış yap
                mAuth.signOut();

                // MainActivity'yi tekrar başlat
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                finish(); // Bu aktiviteyi kapat
            }
        });

        addLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add Label Fragment'i başlat
                startFragment(new AddLabelFragment());
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add Photo Fragment'i başlat
                startFragment(new AddPhotoFragment());
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gallery Fragment'i başlat
                startFragment(new GalleryFragment());
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // About Fragment'i başlat
                startFragment(new AboutFragment());
            }
        });

        backmenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ana menüyü göster
                findViewById(R.id.menubuttons).setVisibility(View.VISIBLE);

                // Fragment'ı kaldır
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().commit();

                // Geri butonunu gizle
                backmenuButton.setVisibility(View.GONE);
            }
        });
    }

    private void startFragment(Fragment fragment) {
        // Diğer elemanları gizle
        findViewById(R.id.menubuttons).setVisibility(View.GONE);
        findViewById(R.id.backmenu).setVisibility(View.VISIBLE);

        // Verilen fragment'ı yükleyin
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

