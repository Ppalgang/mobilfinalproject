package com.example.mobilfinalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;


public class AddLabelFragment extends Fragment {

    private EditText labelInput, descriptionInput;
    private Button addButton;
    private ListView labelsListView;
    private ArrayList<String> labelsList;
    private ArrayAdapter<String> labelsAdapter;

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_label, container, false);

        labelInput = view.findViewById(R.id.labelInput);
        descriptionInput = view.findViewById(R.id.descriptionInput);
        addButton = view.findViewById(R.id.addButton);
        labelsListView = view.findViewById(R.id.labelsListView);

        labelsList = new ArrayList<>();
        labelsAdapter = new LabelsAdapter(getContext(), labelsList);
        labelsListView.setAdapter(labelsAdapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://mobilfinalproject-c5751-default-rtdb.europe-west1.firebasedatabase.app/").getReference("labels");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLabel();
            }
        });

        // Firebase verilerini dinleyerek listeyi güncelleyin
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // onDataChange metodu çalıştığında önceki etiketleri temizle
                labelsList.clear();

                // Veritabanından yeni verileri oku ve listeye ekle
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Label label = snapshot.getValue(Label.class);
                    if (label != null) {
                        labelsList.add(label.getLabelName());
                    }
                }

                // Listeyi güncelledikten sonra adaptöre haber ver
                labelsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda işlemleri tanımlayın
            }
        });

        return view;
    }


    private void addLabel() {
        String labelName = labelInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        if (!TextUtils.isEmpty(labelName) && !TextUtils.isEmpty(description)) {
            // Etiket nesnesini oluşturun
            Label label = new Label(labelName, description);

            // Etiketi Firebase veritabanına ekleyin
            String labelId = databaseReference.push().getKey();
            databaseReference.child(labelId).setValue(label);

            // Giriş yaptığınızda etiket listesini temizleyin
            labelInput.setText("");
            descriptionInput.setText("");

            // Labelleri tekrar çek
            fetchLabels();  // Bu satırı ekledik
        } else {
            Toast.makeText(getContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchLabels() {
        // Firebase verilerini dinleyerek listeyi güncelleyin
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                labelsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Label label = snapshot.getValue(Label.class);
                    if (label != null) {
                        labelsList.add(label.getLabelName());
                    }
                }
                labelsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda işlemleri tanımlayın
            }
        });
    }
}
