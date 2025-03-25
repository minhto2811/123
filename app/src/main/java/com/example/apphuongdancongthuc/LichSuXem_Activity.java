package com.example.apphuongdancongthuc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LichSuXem_Activity extends AppCompatActivity {
    GridView gridViewLichSu;
    LichSuXem_Adapter adapter;
    ArrayList<LichSuXem> mang;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_xem);

        TextView textTendn = findViewById(R.id.tendn);
        gridViewLichSu = findViewById(R.id.grv);
        mang = new ArrayList<>();
        adapter = new LichSuXem_Adapter(this, R.layout.ds_lichsuxem, mang);
        gridViewLichSu.setAdapter(adapter);

        database = new Database(this, "banhang.db", null, 1);

        // Lấy tên đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        // Kiểm tra tên đăng nhập
        if (tendn != null) {
            textTendn.setText(tendn);
            loadLichSuXem(tendn); // Gọi phương thức với tên đăng nhập
        } else {
            Intent intent = new Intent(LichSuXem_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish(); // Kết thúc activity nếu chưa đăng nhập
            return;
        }

        // Sự kiện click cho GridView
        gridViewLichSu.setOnItemClickListener((parent, view, position, id) -> {
            LichSuXem lichSuXem = mang.get(position);
            SanPham sanPham = database.getSanPhamByMasp(lichSuXem.getMasp()); // Lấy sản phẩm tương ứng
            if (sanPham != null) {
                Intent intent = new Intent(LichSuXem_Activity.this, ChiTietSanPham_Activity.class);

                // Truyền các thuộc tính của sanPham
                intent.putExtra("masp", sanPham.getMasp());
                intent.putExtra("tensp", sanPham.getTensp());
                intent.putExtra("nguyenlieu", sanPham.getNguyenlieu());
                intent.putExtra("cachlam", sanPham.getCachlam());
                intent.putExtra("ghichu", sanPham.getGhichu());
                intent.putExtra("nguoidang", sanPham.getNguoidang());
                intent.putExtra("mansp", sanPham.getMansp());
                intent.putExtra("anh", sanPham.getAnh());

                startActivity(intent);
            }
        });



        ImageButton btntrangchu = findViewById(R.id.btntrangchu);
        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LichSuXem_Activity.this, TrangchuNgdung_Activity.class);

                // Gửi tên đăng nhập qua Intent
                intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences

                startActivity(intent);
            }
        });
        ImageButton btntimkiem = findViewById(R.id.btntimkiem);
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LichSuXem_Activity.this, TimKiemSanPham_Activity.class);

                // Gửi tên đăng nhập qua Intent
                intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences

                startActivity(intent);
            }
        });

        ImageButton btncanhan = findViewById(R.id.btncanhan);
        btncanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LichSuXem_Activity.this, TrangCaNhan_nguoidung_Activity.class);
                intent.putExtra("tendn", tendn); // Gửi tên đăng nhập
                startActivity(intent);
            }
        });
        // Các sự kiện khác
        EditText timkiem=findViewById(R.id.timkiem);
        timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LichSuXem_Activity.this, TimKiemSanPham_Activity.class);

                // Gửi tên đăng nhập qua Intent
                intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences

                startActivity(intent);
            }
        });
    }

    public void loadLichSuXem(String username) {
        List<LichSuXem> lichSuXemListFromDB = database.getLichSuXemByUser(username);
        Set<String> uniqueMaspSet = new HashSet<>();
        mang.clear();

        for (LichSuXem lichSuXem : lichSuXemListFromDB) {
            String masp = lichSuXem.getMasp();
            if (!uniqueMaspSet.contains(masp)) {
                uniqueMaspSet.add(masp);
                mang.add(lichSuXem);
            }
        }

        adapter.notifyDataSetChanged();
    }
}