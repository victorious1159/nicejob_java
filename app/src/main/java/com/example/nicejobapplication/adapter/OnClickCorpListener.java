package com.example.nicejobapplication.adapter;

import androidx.annotation.NonNull; // Thêm import này nếu cần

import com.example.nicejobapplication.modal.Corporation;

import java.util.ArrayList;

public interface OnClickCorpListener {
    void onItemClick(int position, @NonNull ArrayList<Corporation> corporationList);

    void onItemClickUpdate(int position);
}
