package com.example.woofer;

public interface RequestInt {
    void onAccept(int user_id, int position);
    void onReject(int user_id, int position);
    void remove(int user_id, int position);
}