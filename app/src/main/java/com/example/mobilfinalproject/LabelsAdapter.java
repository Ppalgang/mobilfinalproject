package com.example.mobilfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class LabelsAdapter extends ArrayAdapter<String> {

    public LabelsAdapter(Context context, ArrayList<String> labels) {
        super(context, 0, labels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ListView içinde her bir öğe için çağrılır
        String label = getItem(position);

        // Eğer bir önceki görünümü kullanabiliriz, kullanırız; aksi takdirde yeni bir görünüm oluştururuz
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_label, parent, false);
        }

        // Görünümdeki TextView'e etiketi yerleştiririz
        TextView labelTextView = convertView.findViewById(R.id.labelTextView);
        labelTextView.setText(label);

        return convertView;
    }
}