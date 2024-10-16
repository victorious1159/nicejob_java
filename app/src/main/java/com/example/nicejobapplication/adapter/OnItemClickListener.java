package com.example.nicejobapplication.adapter;

import com.example.nicejobapplication.modal.Jobs;
import java.util.ArrayList;

public interface OnItemClickListener {
    void onItemClick(int position, ArrayList<Jobs> jobsArrayList);
    void onItemClickUpdate(int position);
}

