package com.example.nicejobapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.modal.Jobs;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobsViewHolder> {

    private final Context context;
    private final ArrayList<Jobs> jobsArrayList;
    private final OnItemClickListener listener;

    public JobsAdapter(Context context, ArrayList<Jobs> jobsArrayList, OnItemClickListener listener) {
        this.context = context;
        this.jobsArrayList = jobsArrayList;
        this.listener = listener;
    }

    public static class JobsViewHolder extends RecyclerView.ViewHolder {
        public final TextView jobName;
        public final ImageButton corpLogo;
        public final TextView corpName;
        public final TextView address;
        public final TextView exp;
        public final TextView salary;
        public final TextView deadline;

        public JobsViewHolder(View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobName);
            corpLogo = itemView.findViewById(R.id.corpLogo);
            corpName = itemView.findViewById(R.id.corpName);
            address = itemView.findViewById(R.id.address);
            exp = itemView.findViewById(R.id.exp);
            salary = itemView.findViewById(R.id.salary);
            deadline = itemView.findViewById(R.id.deadline);
        }
    }

    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.jobs_item, parent, false);
        return new JobsViewHolder(adapterLayout);
    }

    @Override
    public int getItemCount() {
        return jobsArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewHolder holder, int position) {
        Jobs item = jobsArrayList.get(position);
        holder.jobName.setText(item.getJobName());

        String corpID = item.getCorpID();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy logo công ty
        db.collection("corporations").document(corpID).get().addOnSuccessListener(documentSnapshot -> {
            String urlImage = "gs://nicejob-367709.appspot.com/corporation_image/" + documentSnapshot.getString("corpLogo");
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage);
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(holder.itemView.getContext())
                        .load(uri)
                        .into(holder.corpLogo);
            });
        });

        // Lấy tên công ty
        db.collection("corporations").document(corpID).get().addOnSuccessListener(documentSnapshot -> {
            holder.corpName.setText(documentSnapshot.getString("corpName"));
        });

        holder.address.setText(item.getWorkAddress() != null && item.getWorkAddress().length > 0 ? item.getAddress(item.getWorkAddress()[0]) : "");
        holder.exp.setText(item.getExp(item.getExpId()));
        holder.salary.setText(item.getSalary(item.getSalaryId()));

        // Tính số ngày còn lại
        long currentTime = System.currentTimeMillis();
        Timestamp expertDay = item.getExpertDay(); // Giả sử đây là Timestamp của ngày hết hạn
        if (expertDay != null) {
            long deadlineTime = expertDay.getSeconds() * 1000; // Chuyển giây thành mili giây
            long daysLeft = (deadlineTime - currentTime) / (1000 * 60 * 60 * 24); // Tính số ngày còn lại
            holder.deadline.setText("Còn " + daysLeft + " ngày để ứng tuyển");
        } else {
            holder.deadline.setText("Hạn nộp không xác định");
        }


        holder.itemView.setOnClickListener(v -> listener.onItemClick(position, jobsArrayList));
    }
}
