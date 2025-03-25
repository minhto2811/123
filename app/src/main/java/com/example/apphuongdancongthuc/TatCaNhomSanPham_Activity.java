package com.example.apphuongdancongthuc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TatCaNhomSanPham_Activity extends AppCompatActivity {
    GridView grv;

    ArrayList<NhomSanPham> mangNSPgrv; // Danh sách cho GridView


    NhomSanPhamAdapter adapterGrv;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_ca_nhom_san_pham);
        ImageButton btntimkiem = findViewById(R.id.btntimkiem);
        ImageButton btntrangchu = findViewById(R.id.btntrangchu);


        ImageButton btncanhan = findViewById(R.id.btncanhan);
        EditText timkiem = findViewById(R.id.timkiem);
        TextView textTendn = findViewById(R.id.tendn); // TextView hiển thị tên đăng nhập
        grv= findViewById(R.id.grv);


        // Lấy tên đăng nhập từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        // Kiểm tra tên đăng nhập
        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            Intent intent = new Intent(TatCaNhomSanPham_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish(); // Kết thúc activity nếu chưa đăng nhập
            return;
        }
        ImageButton btndaxem = findViewById(R.id.btndaxem);
        btndaxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TatCaNhomSanPham_Activity.this, LichSuXem_Activity.class);

                // Gửi tên đăng nhập qua Intent
                intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences

                startActivity(intent);
            }
        });
        grv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng nhóm sản phẩm từ adapter
                NhomSanPham nhomSanPham = mangNSPgrv.get(position);

                if (nhomSanPham != null) {
                    // Chuyển đến DanhMucSanPham_Activity và truyền mã của nhóm sản phẩm
                    Intent intent = new Intent(TatCaNhomSanPham_Activity.this, DanhMucSanPham_Activity.class);
                    intent.putExtra("nhomSpId", nhomSanPham.getMa()); // Gửi mã nhóm sản phẩm
                    startActivity(intent);
                }
            }
        });

        // Gửi tên đăng nhập qua Intent trong sự kiện click
        btntrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TatCaNhomSanPham_Activity.this, TrangchuNgdung_Activity.class);
                // Gửi tên đăng nhập qua Intent
                intent.putExtra("tendn", tendn); // sử dụng biến tendn đã được xác nhận
                startActivity(intent);
            }
        });
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TatCaNhomSanPham_Activity.this, TimKiemSanPham_Activity.class);

                // Gửi tên đăng nhập qua Intent
                intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences

                startActivity(intent);
            }
        });


        btncanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TatCaNhomSanPham_Activity.this, TrangCaNhan_nguoidung_Activity.class);
                intent.putExtra("tendn", tendn); // Gửi tên đăng nhập
                startActivity(intent);
            }
        });
        // Các sự kiện khác
        timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TatCaNhomSanPham_Activity.this, TimKiemSanPham_Activity.class);

                // Gửi tên đăng nhập qua Intent
                intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences

                startActivity(intent);
            }
        });

        // Khởi tạo danh sách và adapter

        mangNSPgrv= new ArrayList<>();

        adapterGrv = new NhomSanPhamAdapter(this, mangNSPgrv, false);
        grv.setAdapter(adapterGrv);


        database = new Database(this, "banhang.db", null, 1);


        Loaddulieusanphamgridview2();
    }

    private void Loaddulieusanphamgridview2() {
        Cursor dataCongViec = database.GetData("SELECT * from nhomsanpham ");
        mangNSPgrv.clear();

        while (dataCongViec.moveToNext()) {
            String ma = dataCongViec.getString(0);
            String ten = dataCongViec.getString(1);
            byte[] blob = dataCongViec.getBlob(2); // Lấy mảng byte từ cột chứa ảnh
            mangNSPgrv.add(new NhomSanPham(ma, ten, blob));
        }

        adapterGrv.notifyDataSetChanged(); // Cập nhật adapter
    }


}