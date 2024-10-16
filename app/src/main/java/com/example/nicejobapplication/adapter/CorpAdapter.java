package com.example.nicejobapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.modal.Corporation;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CorpAdapter extends RecyclerView.Adapter<CorpAdapter.CorpViewHolder> {

    private final Context context;
    private final ArrayList<Corporation> corpArrayList;
    private final OnClickCorpListener listener;

    public CorpAdapter(Context context, ArrayList<Corporation> corpArrayList, OnClickCorpListener listener) {
        this.context = context;
        this.corpArrayList = corpArrayList;
        this.listener = listener;
    }

    public static class CorpViewHolder extends RecyclerView.ViewHolder {
        public final TextView corpTitle;
        public final TextView corpDes;
        public final ImageView corpLogo;

        public CorpViewHolder(View itemView) {
            super(itemView);
            corpTitle = itemView.findViewById(R.id.corpTitle);
            corpDes = itemView.findViewById(R.id.corpDes);
            corpLogo = itemView.findViewById(R.id.corpImage);
        }
    }

    @NonNull
    @Override
    public CorpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.corp_item, parent, false);
        return new CorpViewHolder(adapterLayout);
    }

    @Override
    public int getItemCount() {
        return corpArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CorpViewHolder holder, int position) {
        Corporation item = corpArrayList.get(position);
        holder.corpTitle.setText(item.getCorpName());
        holder.corpDes.setText(item.getCorpDescription());

        // Tải logo công ty từ Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(item.getCorpLogo());
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(holder.itemView.getContext())
                    .load(uri)
                    .into(holder.corpLogo);
        });

        // Cài đặt sự kiện click cho mỗi mục
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position, corpArrayList));
    }
}
