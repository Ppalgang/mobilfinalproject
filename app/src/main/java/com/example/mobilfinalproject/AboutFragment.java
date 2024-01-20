package com.example.mobilfinalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    private ImageView imageView;
    private Button btnLinkedIn;
    private static final String LINKEDIN_PROFILE_URL = "https://www.linkedin.com/in/mrbatuhanyalcin";

    public AboutFragment() {
        // Boş kurucu metodu
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        imageView = view.findViewById(R.id.imageView);
        btnLinkedIn = view.findViewById(R.id.btnLinkedIn);

        // Resmi ve LinkedIn butonunu ayarla
        setupUI();

        return view;
    }

    private void setupUI() {
        imageView.setImageResource(R.drawable.batuhan); // Drawable klasöründe bulunan resmin adını belirtin

        // LinkedIn bağlantısını açma işlemi
        btnLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinkedInProfile();
            }
        });
    }

    // LinkedIn profiline yönlendirme
    public void openLinkedInProfile() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LINKEDIN_PROFILE_URL));
        startActivity(intent);
    }
}
