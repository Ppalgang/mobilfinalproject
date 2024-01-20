package com.example.mobilfinalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private List<Photo> photoList;

    public GalleryAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photoList.get(position);

        // Base64 formatındaki veriyi resme dönüştür ve ImageView'a yükle
        byte[] decodedString = Base64.decode(photo.getImageUrl(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageView.setImageBitmap(decodedByte);

        holder.txtUsername.setText(photo.getUsername());
        holder.txtLabels.setText("" + photo.getLabelsAsString());
        holder.txtLikes.setText("" + photo.getLikeCount());
        holder.txtDislikes.setText("" + photo.getDislikeCount());

        // Like butonuna tıklama işlemi
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Like değerini arttır
                updateLikeCount(photo.getPhotoId(), true);

                // TextView'leri güncelle
                holder.txtLikes.setText("" + (photo.getLikeCount() + 1));
            }
        });

        // Dislike butonuna tıklama işlemi
        holder.btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dislike değerini arttır
                updateLikeCount(photo.getPhotoId(), false);

                // TextView'leri güncelle
                holder.txtDislikes.setText("" + (photo.getDislikeCount() + 1));
            }
        });


        // Dislike butonuna tıklama işlemi
        holder.btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dislike değerini arttır
                updateLikeCount(photo.getPhotoId(), false);
            }
        });
    }


    private void updateLikeCount(String photoId, boolean isLike) {
        DatabaseReference photoRef = FirebaseDatabase.getInstance("https://mobilfinalproject-c5751-default-rtdb.europe-west1.firebasedatabase.app/").getReference("photos").child(photoId);

        // Like ve dislike değerlerini arttır
        if (isLike) {
            photoRef.child("likes").setValue(ServerValue.increment(1));
            photoRef.child("likeCount").setValue(ServerValue.increment(1));
        } else {
            photoRef.child("dislikes").setValue(ServerValue.increment(1));
            photoRef.child("dislikeCount").setValue(ServerValue.increment(1));
        }
    }



    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Resim
        ImageView imageView;

        // Kullanıcı adı, etiketler, beğeni ve beğenmeme sayıları için TextView'ler
        TextView txtUsername;
        TextView txtLabels;
        TextView txtLikes;
        TextView txtDislikes;
        Button btnLike;
        Button btnDislike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtUsername = itemView.findViewById(R.id.textViewUsername);
            txtLabels = itemView.findViewById(R.id.textViewLabels);
            txtLikes = itemView.findViewById(R.id.textViewLikeCount);
            txtDislikes = itemView.findViewById(R.id.textViewDislikeCount);
            btnLike = itemView.findViewById(R.id.btnLike); // Replace with the actual ID of your like button
            btnDislike = itemView.findViewById(R.id.btnDislike); // Replace with the actual ID of your dislike button
        }
    }
}
