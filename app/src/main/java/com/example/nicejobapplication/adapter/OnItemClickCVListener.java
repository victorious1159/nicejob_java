package com.example.nicejobapplication.adapter;

import com.example.nicejobapplication.modal.CV;
import com.example.nicejobapplication.modal.Jobs;

import java.util.ArrayList;

public interface OnItemClickCVListener {
    void onItemClick(int position, ArrayList<CV> cvArrayList);
    void onItemClickUpdate(int position);
}

