package com.example.nicejobapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nicejobapplication.R;
import com.example.nicejobapplication.modal.CV;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CvAdapter extends RecyclerView.Adapter<CvAdapter.CvViewHolder> {

    private final Context context;
    private final ArrayList<CV> cvArrayList;
    private final OnItemClickCVListener listener;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public CvAdapter(Context context, ArrayList<CV> cvArrayList, OnItemClickCVListener listener) {
        this.context = context;
        this.cvArrayList = cvArrayList;
        this.listener = listener;
    }

    public static class CvViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView createAt;
        public final ImageView edit;
        public final ImageView delete;

        public CvViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ViewNameCV);
            createAt = itemView.findViewById(R.id.ViewCreateAt);
            edit = itemView.findViewById(R.id.btnEdit);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public CvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adapterLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item, parent, false);
        return new CvViewHolder(adapterLayout);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CvViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

        CV item = cvArrayList.get(position);
        holder.name.setText(item.getCvName());
        holder.createAt.setText(millisecondsToDate(String.valueOf(item.getCreatedAt()), dateFormat));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position, cvArrayList));
        holder.edit.setOnClickListener(v -> listener.onItemClickUpdate(position));
        holder.delete.setOnClickListener(v -> {
            auth = FirebaseAuth.getInstance();
            String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : null;
            db = FirebaseFirestore.getInstance();

            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
            dialogBuilder.setTitle("Delete")
                    .setIcon(R.drawable.ic_warning)
                    .setMessage("Bạn có chắc muốn xóa CV này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        String cvId = item.getCvId();
                        if (cvId != null && userEmail != null) {
                            db.collection("created_cv").document(userEmail)
                                    .collection(userEmail).document(cvId).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Đã xóa thông tin này!", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    });
                        }
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return cvArrayList.size();
    }

    private String millisecondsToDate(String milliseconds, SimpleDateFormat dateFormat) {
        long millis = Long.parseLong(milliseconds);
        return dateFormat.format(millis);
    }
}
