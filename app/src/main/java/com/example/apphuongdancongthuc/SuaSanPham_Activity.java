package com.example.apphuongdancongthuc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class SuaSanPham_Activity extends AppCompatActivity {
    Database database;


    ArrayList<SanPham> mangBS;
    SanPhamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_san_pham);
        mangBS = new ArrayList<>();
        database = new Database(this, "banhang.db", null, 1);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", "");
        String role = getUserQuyen(tendn);
        adapter = new SanPhamAdapter(this, mangBS, true, role.equals("admin"));


    }

    private String getUserQuyen(String username) {
        String quyen = "";
        Cursor cursor = database.getReadableDatabase().rawQuery(
                "SELECT quyen FROM taikhoan WHERE tendn = ?",
                new String[]{username});

        if (cursor.moveToFirst()) {
            int quyenColumnIndex = cursor.getColumnIndex("quyen");
            if (quyenColumnIndex != -1) {
                quyen = cursor.getString(quyenColumnIndex);
            } else {
                Log.e("Error", "Column 'quyen' not found in result set");
            }
        } else {
            Log.e("Error", "No user found with username: " + username);
        }
        cursor.close(); // Đóng cursor
        return quyen;
    }

}