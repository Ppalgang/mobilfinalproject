package com.example.mobilfinalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class AddPhotoFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private ImageButton chooseImageButton;
    private Button uploadButton;

    private ArrayList<String> selectedLabels = new ArrayList<>();

    private String encodeImageToBase64(Uri imageUri) throws IOException {
        InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
        byte[] bytes = getBytes(inputStream);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);

        chooseImageButton = view.findViewById(R.id.chooseImageButton);
        uploadButton = view.findViewById(R.id.uploadButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kullanıcının e-posta adresini al
                String userEmail = getCurrentUserEmail();

                if (!TextUtils.isEmpty(userEmail)) {
                    uploadData(userEmail);
                } else {
                    // Kullanıcı oturum açmamışsa uyarı mesajı göster
                    showToast("User is not authenticated. Please log in.");
                }
            }
        });

        Button selectLabelsButton = view.findViewById(R.id.selectLabelsButton);
        selectLabelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLabelSelectionDialog();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadData(String userEmail) {
        if (imageUri != null) {
            try {
                String imageBase64 = encodeImageToBase64(imageUri);

                // Firebase veritabanına yüklemek için bir anahtar oluştur
                String photoId = UUID.randomUUID().toString(); // Rastgele bir UUID oluştur

                // Veritabanına eklemek üzere bir fotoğraf nesnesi oluştur
                Photo photo = new Photo(photoId, imageBase64, userEmail, selectedLabels, 0, 0);

                // Photos düğümüne fotoğrafı ekleyin
                DatabaseReference photosRef = FirebaseDatabase.getInstance("https://mobilfinalproject-c5751-default-rtdb.europe-west1.firebasedatabase.app/").getReference("photos");
                photosRef.child(photoId).setValue(photo, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // İhtiyaca göre beğeni ve beğenmeme sayıları ekleyebilirsiniz
                            photosRef.child(photoId).child("likes").setValue(0);
                            photosRef.child(photoId).child("dislikes").setValue(0);

                            // Toast mesajı göster
                            showToast("Upload successful");

                            // Seçilen etiketleri ve resmi temizle
                            selectedLabels.clear();
                            imageUri = null;

                            // ImageView'ı temizle
                            ImageView imageView = requireView().findViewById(R.id.imageView);
                            imageView.setImageDrawable(null);
                        } else {
                            // Hata durumunda işlemleri tanımlayın
                            Log.e("UploadData", "Error uploading data: " + error.getMessage());
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLabelSelectionDialog() {
        DatabaseReference labelsRef = FirebaseDatabase.getInstance("https://mobilfinalproject-c5751-default-rtdb.europe-west1.firebasedatabase.app/").getReference("labels");

        labelsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                ArrayList<String> labelNames = new ArrayList<>();

                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Label label = snapshot.getValue(Label.class);
                    if (label != null) {
                        labelNames.add(label.getLabelName());
                    }
                }

                final CharSequence[] items = labelNames.toArray(new CharSequence[labelNames.size()]);
                final boolean[] selectedItems = new boolean[labelNames.size()];

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Choose Labels");
                builder.setMultiChoiceItems(items, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedItems[which] = isChecked;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handleSelectedLabels(selectedItems, labelNames);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda işlemleri tanımlayın
            }
        });
    }

    private void handleSelectedLabels(boolean[] selectedItems, ArrayList<String> labelNames) {
        // Seçilen etiketleri işleyin
        selectedLabels.clear();
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i]) {
                selectedLabels.add(labelNames.get(i));
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String getCurrentUserEmail() {
        // Firebase Authentication'dan kullanıcı bilgilerini al
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Kullanıcının e-posta adresini döndür
            String userEmail = user.getEmail();

            // "@" işaretinden önceki kısmı al
            if (userEmail != null && userEmail.contains("@")) {
                return userEmail.split("@")[0];
            }
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Resmi seçtikten sonra ImageView'da göster
            ImageView imageView = requireView().findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
