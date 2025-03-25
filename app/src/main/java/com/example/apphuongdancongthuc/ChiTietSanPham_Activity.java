package com.example.apphuongdancongthuc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChiTietSanPham_Activity extends AppCompatActivity {

     String masp, tendn;
     DatabaseHelper dbHelper;
    private ChiTietSanPham chiTietSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        // Nhận dữ liệu từ Intent
        String masp1 = getIntent().getStringExtra("masp");
        String tensp1 = getIntent().getStringExtra("tensp");
        String nguyenlieu1 = getIntent().getStringExtra("nguyenlieu");
        String cachlam1 = getIntent().getStringExtra("cachlam");
        String ghichu1 = getIntent().getStringExtra("ghichu");
        String nguoidang1 = getIntent().getStringExtra("nguoidang");
        String mansp1 = getIntent().getStringExtra("mansp");
        byte[] anh1 = getIntent().getByteArrayExtra("anh");

        TextView tensp = findViewById(R.id.tensp);
        ImageView imgsp = findViewById(R.id.imgsp);
        TextView nguyenlieu= findViewById(R.id.nguyenlieu);
        TextView cachlam = findViewById(R.id.cachlam);
        TextView ghichu = findViewById(R.id.ghichu);
        TextView nguoidang= findViewById(R.id.nguoidang);
        TextView textTendn = findViewById(R.id.tendn); // TextView hiển thị tên đăng nhập
        TextView mansp = findViewById(R.id.manhomsanpham);

        // Hiển thị thông tin sản phẩm
        tensp.setText(tensp1);
        nguyenlieu.setText(nguyenlieu1);
        cachlam.setText(cachlam1);
        ghichu.setText(ghichu1);
        nguoidang.setText(nguoidang1);
        mansp.setText(mansp1);
        // Hiển thị hình ảnh sản phẩm
        if (anh1 != null) {
            Bitmap imganh = BitmapFactory.decodeByteArray(anh1, 0, anh1.length);
            imgsp.setImageBitmap(imganh);
        } else {
            imgsp.setImageResource(R.drawable.logo); // Hình ảnh mặc định nếu không có
        }


        // Lấy tendn từ SharedPreferences
        SharedPreferences sharedPre = getSharedPreferences("MyPrefs", MODE_PRIVATE);
       tendn = sharedPre.getString("tendn", null);

        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            Intent intent = new Intent(ChiTietSanPham_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish(); // Kết thúc activity nếu chưa đăng nhập
            return;
        }

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();


            // Nhận chi tiết sản phẩm nếu có
            chiTietSanPham = intent.getParcelableExtra("chitietsanpham");

            // Nếu không có chi tiết sản phẩm, bạn có thể xử lý mã sản phẩm theo cách của riêng bạn
            if (chiTietSanPham != null) {
                masp = chiTietSanPham.getMasp(); // Lấy mã sản phẩm từ chi tiết
                tensp.setText(chiTietSanPham.getTensp());
                nguyenlieu.setText(chiTietSanPham.getNguyenlieu() != null ? String.valueOf(chiTietSanPham.getNguyenlieu()) : "Không có dữ liệu");
                cachlam.setText(chiTietSanPham.getCachlam() != null ? chiTietSanPham.getCachlam() : "Không có dữ liệu");
                ghichu.setText(chiTietSanPham.getGhichu() != null ? chiTietSanPham.getGhichu() : "Không có dữ liệu");
                nguoidang.setText(chiTietSanPham.getNguoidang());
                byte[] anhByteArray = chiTietSanPham.getAnh();

                dbHelper = new DatabaseHelper(this);
                if (tendn != null && masp != null) {
                    dbHelper.addLichSuXem(tendn, masp);
                    Toast.makeText(this, "Đã lưu lịch sử xem sản phẩm!", Toast.LENGTH_SHORT).show();
                }
                 if (anhByteArray != null && anhByteArray.length > 0) {
                    Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
                    imgsp.setImageBitmap(imganhbs);
                } else {
                    imgsp.setImageResource(R.drawable.vest); // Ảnh mặc định
                }
            } else {
                tensp.setText("Không có dữ liệu");
            }

        ImageButton btndaxem = findViewById(R.id.btndaxem);
        btndaxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietSanPham_Activity.this, LichSuXem_Activity.class);

                // Gửi tên đăng nhập qua Intent
                intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences

                startActivity(intent);
            }
        });


        // Các nút điều hướng
        setupNavigationButtons();

        // KHÔNG cần khai báo lại biến masp ở đây! ĐÃ KHAI BÁO Ở TRÊN
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        tendn = prefs.getString("tendn", "Ẩn danh");

// ⚠️ KHÔNG dùng lại getIntent().getStringExtra("masp")
// => vì bạn đã có masp từ chiTietSanPham ở trên
        if (masp == null || masp.isEmpty()) {
            Toast.makeText(this, "Không thể hiển thị nhận xét do thiếu mã sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

// Khởi tạo DB
        dbHelper = new DatabaseHelper(this);
        List<Comment> comments = dbHelper.getCommentsByProduct(masp);

        CommentAdapter adapter = new CommentAdapter(comments);
        RecyclerView rvComments = findViewById(R.id.rvComments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(adapter);

        EditText edtComment = findViewById(R.id.edtComment);
        Button btnSendComment = findViewById(R.id.btnSendComment);

        btnSendComment.setOnClickListener(v -> {
            String content = edtComment.getText().toString().trim();
            if (!content.isEmpty()) {
                dbHelper.insertComment(masp, tendn, content); // Lưu vào DB
                comments.add(new Comment(tendn, content));    // Hiển thị ngay
                adapter.notifyItemInserted(comments.size() - 1);
                rvComments.scrollToPosition(comments.size() - 1);
                edtComment.setText("");
            } else {
                Toast.makeText(this, "Vui lòng nhập nhận xét", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupNavigationButtons() {
        ImageButton btntrangchu = findViewById(R.id.btntrangchu);
        ImageButton btntimkiem = findViewById(R.id.btntimkiem);

        ImageButton btncanhan = findViewById(R.id.btncanhan);

        btntrangchu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TrangchuNgdung_Activity.class);
            startActivity(intent);
        });
btntimkiem.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent a=new Intent(ChiTietSanPham_Activity.this,TimKiemSanPham_Activity.class);
        startActivity(a);
    }
});


        btncanhan.setOnClickListener(view -> navigateToProfile());
    }

    private void navigateToProfile() {
        // Kiểm tra trạng thái đăng nhập của người dùng
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Đã đăng nhập, chuyển đến trang đơn hàng
            Intent intent = new Intent(getApplicationContext(), TrangCaNhan_nguoidung_Activity.class);

            // Truyền tendn qua Intent
            intent.putExtra("tendn", tendn);  // Thêm dòng này để truyền tendn

            startActivity(intent);
        } else {
            // Chưa đăng nhập, chuyển đến trang login
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
        }
    }

}
