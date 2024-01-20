package com.example.mobilfinalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private Spinner spinnerLabels;
    private Button btnFilter;
    private RecyclerView recyclerViewGallery;

    private List<Photo> photoList;
    private GalleryAdapter galleryAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        spinnerLabels = view.findViewById(R.id.spinnerLabels);
        btnFilter = view.findViewById(R.id.btnFilter);
        recyclerViewGallery = view.findViewById(R.id.recyclerViewGallery);

        // Labels'ı Spinner'a yükle
        loadLabels();

        // RecyclerView için ayarlamalar
        recyclerViewGallery.setLayoutManager(new LinearLayoutManager(requireContext()));
        photoList = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(requireContext(), photoList);
        recyclerViewGallery.setAdapter(galleryAdapter);

        // Filtreleme butonu tıklandığında
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterGallery();
            }
        });

        // Galeri verilerini yükle
        loadGalleryData();

        return view;
    }

    private void loadLabels() {
        DatabaseReference labelsRef = FirebaseDatabase.getInstance("https://mobilfinalproject-c5751-default-rtdb.europe-west1.firebasedatabase.app/").getReference("labels");

        labelsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> labelNames = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Label label = snapshot.getValue(Label.class);
                    if (label != null) {
                        labelNames.add(label.getLabelName());
                    }
                }

                // Spinner için adapter oluştur
                ArrayAdapter<String> labelAdapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        labelNames
                );

                // Spinner'a adapter'ı bağla
                spinnerLabels.setAdapter(labelAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda işlemleri tanımlayın
            }
        });
    }

    private void loadGalleryData() {
        DatabaseReference photosRef = FirebaseDatabase.getInstance("https://mobilfinalproject-c5751-default-rtdb.europe-west1.firebasedatabase.app/").getReference("photos");

        photosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Galeri verilerini temizle
                photoList.clear();

                // Tüm fotoğrafları döngüye al
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Photo photo = snapshot.getValue(Photo.class);
                    if (photo != null) {
                        photoList.add(photo);
                    }
                }

                // Adapter'a değişiklikleri bildir
                galleryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda işlemleri tanımlayın
            }
        });
    }

    private void filterGallery() {
        // Spinner'dan seçilen label'ı al
        String selectedLabel = spinnerLabels.getSelectedItem().toString();

        // Seçilen label'a göre filtreleme yap
        List<Photo> filteredList = new ArrayList<>();
        for (Photo photo : photoList) {
            if (photo.getLabels() != null && photo.getLabels().contains(selectedLabel)) {
                filteredList.add(photo);
            }
        }

        // Filtrelenmiş listeyi RecyclerView'a yükle
        galleryAdapter.setPhotoList(filteredList);
        galleryAdapter.notifyDataSetChanged();
    }
}
