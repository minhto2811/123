package com.example.apphuongdancongthuc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TabProductUserActivity extends AppCompatActivity {

    private Database database;
    private ListView lv;
    private FloatingActionButton addButton;
    private ArrayList<SanPham> mangSP;
    private SanPhamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_product_user);
        initBottomBar();
        initView();
        setupDatabase();
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ThemSanPham_Activity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", "");
        Cursor cursor = database.GetData("SELECT * FROM sanpham Where nguoidang ='" + tendn + "'");
        mangSP.clear();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String masp = cursor.getInt(0) + "";
                String tensp = cursor.getString(1);
                String nguyenlieu = cursor.getString(2); // Giữ nguyên là float
                String cachlam = cursor.getString(3);
                String ghichu = cursor.getString(4);
                String nguoidang = cursor.getString(5); // Giữ nguyên là int
                String maso = cursor.getString(6);
                byte[] blob = cursor.getBlob(7);
                mangSP.add(new SanPham(masp, tensp, nguyenlieu, cachlam, ghichu, nguoidang, maso, blob));
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
            cursor.close();
        } else {
            Toast.makeText(this, "Null load dữ liệu", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }

    private void setupDatabase() {
        database = new Database(this, "banhang.db", null, 1);
    }

    private void initView() {
        lv = findViewById(R.id.listtk);
        addButton = findViewById(R.id.btnthem);
        mangSP = new ArrayList<>();
        adapter = new SanPhamAdapter(TabProductUserActivity.this, mangSP, true,false);
        lv.setAdapter(adapter);
    }

    private void initBottomBar() {
        String tendn = getIntent().getStringExtra("tendn");
        ImageButton btntrangchu = findViewById(R.id.btntrangchu);
        btntrangchu.setOnClickListener(v -> {
            Intent intent = new Intent(TabProductUserActivity.this, TrangchuNgdung_Activity.class);
            intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences
            startActivity(intent);
        });
        ImageButton btntimkiem = findViewById(R.id.btntimkiem);
        btntimkiem.setOnClickListener(v -> {
            Intent intent = new Intent(TabProductUserActivity.this, TimKiemSanPham_Activity.class);
            intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences
            startActivity(intent);
        });
        ImageButton btnsanpham = findViewById(R.id.btnsanpham);
        btnsanpham.setOnClickListener(v -> {
            Intent a = new Intent(getApplicationContext(), TabProductUserActivity.class);
            startActivity(a);
        });
        ImageButton btndaxem = findViewById(R.id.btndaxem);
        btndaxem.setOnClickListener(v -> {
            Intent intent = new Intent(TabProductUserActivity.this, LichSuXem_Activity.class);
            intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences
            startActivity(intent);
        });
        ImageButton btncanhan = findViewById(R.id.btncanhan);
        btncanhan.setOnClickListener(view -> {
            Intent intent = new Intent(TabProductUserActivity.this, TrangCaNhan_nguoidung_Activity.class);
            intent.putExtra("tendn", tendn); // Gửi tên đăng nhập
            startActivity(intent);
        });
    }
}