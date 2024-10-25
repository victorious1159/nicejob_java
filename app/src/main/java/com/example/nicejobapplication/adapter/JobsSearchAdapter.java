package com.example.nicejobapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.fragment.SearchViewJob;
import com.example.nicejobapplication.modal.Jobs;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class JobsSearchAdapter extends RecyclerView.Adapter<JobsSearchAdapter.JobsSearchViewHolder> {

    private final ArrayList<Jobs> jobArrayList;
    private final SearchViewJob listener;
    private final FirebaseFirestore db;

    public JobsSearchAdapter(ArrayList<Jobs> jobArrayList, SearchViewJob listener) {
        this.jobArrayList = jobArrayList;
        this.listener = listener;
        this.db = FirebaseFirestore.getInstance();
    }

    public static class JobsSearchViewHolder extends RecyclerView.ViewHolder {
        public final ImageView logo;
        public final TextView jobName;
        public final TextView corpName;

        public JobsSearchViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logoSearch);
            jobName = itemView.findViewById(R.id.jobNameSearch);
            corpName = itemView.findViewById(R.id.corpNameSearch);
        }
    }

    @NonNull
    @Override
    public JobsSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.jobs_search_item, parent, false);
        return new JobsSearchViewHolder(adapterLayout);
    }

    @Override
    public int getItemCount() {
        return jobArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull JobsSearchViewHolder holder, int position) {
        Jobs item = jobArrayList.get(position);

        holder.jobName.setText(item.getJobName());

        // Lấy tên công ty
        db.collection("corporations").document(item.getCorpID()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                holder.corpName.setText(documentSnapshot.getString("corpName"));
            }
        });

        // Lấy logo công ty
        db.collection("corporations").document(item.getCorpID()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String urlImage = "gs://nicejob2-ddaa1.appspot.com/corporation_image/" + documentSnapshot.getString("corpLogo");
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage);
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(holder.itemView.getContext())
                            .load(uri)
                            .into(holder.logo);
                });
            }
        });

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position, jobArrayList));
    }
}
