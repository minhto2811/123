package com.example.apphuongdancongthuc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ThemSanPham_Activity extends AppCompatActivity {


    EditText tensp, nguyenlieu, cachlam, ghichu, nguoidang;
    Spinner mansp;
    Database database;
    ImageView imgsp;

    ArrayList<NhomSanPham> mangNSPList;

    private Uri imageUri; // Biến để lưu trữ URI của ảnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);

        // Khởi tạo các view
        tensp = findViewById(R.id.tensp);
        imgsp = findViewById(R.id.imgsp);
        nguyenlieu = findViewById(R.id.nguyenlieu);
        ghichu = findViewById(R.id.ghichu);
        cachlam = findViewById(R.id.cachlam);
        nguoidang = findViewById(R.id.nguoidang);
        mansp = findViewById(R.id.spn);
        Button chonimgbs = findViewById(R.id.btnAddImg);
        Button btnthem = findViewById(R.id.btnadd);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", "");
        nguoidang.setText(tendn);

        // Khởi tạo cơ sở dữ liệu
        database = new Database(this, "banhang.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS sanpham(masp INTEGER PRIMARY KEY AUTOINCREMENT, tensp NVARCHAR(200),nguyenlieu TEXT,cachlam TEXT,ghichu TEXT,nguoidang NVARCHAR(100),maso INTEGER , anh BLOB)");
        String role = getUserQuyen(tendn);
        if (!role.equals("admin")) {
            nguoidang.setEnabled(false);
        }
        // Tải danh sách nhóm sản phẩm
        loadTenNhomSanPham();

        // Thiết lập sự kiện cho nút chọn ảnh
        chonimgbs.setOnClickListener(view -> {
            openDrawableImagePicker(); // Gọi hàm mở hình ảnh từ drawable
        });

        // Thiết lập sự kiện cho nút thêm sản phẩm
        btnthem.setOnClickListener(view -> addSanPham());
    }

    private void loadTenNhomSanPham() {
        mangNSPList = new ArrayList<>();
        Cursor cursor = database.GetData("SELECT maso, tennsp FROM nhomsanpham"); // Lấy maso và tennhom

        while (cursor.moveToNext()) {
            String maso = cursor.getString(0); // Cột 0
            String tennhom = cursor.getString(1); // Cột 1
            mangNSPList.add(new NhomSanPham(maso, tennhom, null)); // null nếu không cần ảnh
        }

        // Tạo adapter cho Spinner
        ArrayAdapter<NhomSanPham> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mangNSPList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mansp.setAdapter(adapter);
        mansp.setSelection(0);
    }

    private void addSanPham() {
        // Lấy dữ liệu từ các trường
        String tenNsp = tensp.getText().toString().trim();
        String nguyenlieuStr = nguyenlieu.getText().toString().trim();
        String cachlamStr = cachlam.getText().toString().trim();
        String ghichuStr = ghichu.getText().toString().trim();

        String nguoidangStr = nguoidang.getText().toString().trim();
        int index = mansp.getSelectedItemPosition();
        NhomSanPham nsp = mangNSPList.get(index);
        String maso = nsp.getMa(); // Lấy maso từ Spinner

        // Kiểm tra dữ liệu không rỗng
        if (tenNsp.isEmpty() || nguyenlieuStr.isEmpty() || ghichuStr.isEmpty() || cachlamStr.isEmpty() || nguoidangStr.isEmpty()) {
            Toast.makeText(ThemSanPham_Activity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khởi tạo giá trị cho imageBytes
        byte[] imageBytes = null;
        if (imageUri != null) {
            imageBytes = getBytesFromUri(imageUri);
            if (imageBytes == null) {
                Toast.makeText(ThemSanPham_Activity.this, "Lỗi khi lấy ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        // Thêm sản phẩm vào cơ sở dữ liệu
        database.QueryData("INSERT INTO sanpham(tensp, nguyenlieu, cachlam, ghichu, nguoidang, maso, anh) VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{tenNsp, nguyenlieuStr, cachlamStr, ghichuStr, nguoidangStr, maso, imageBytes});

        Toast.makeText(ThemSanPham_Activity.this, "Thêm món ăn thành công!", Toast.LENGTH_LONG).show();

        finish();
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

    // Mở dialog chọn hình ảnh từ drawable
    private void openDrawableImagePicker() {
        final String[] imageNames = {"bobittet", "bosotvang", "bunbohue", "bunmoc", "bunrieucua", "cakho", "casotcachua", "casotcam", "changanuong", "galuoc", "ganuong", "mucnhoithit", "mucnuong", "mucxaoraucu", "phobo", "salat1", "salat2", "salat3", "salattom", "thitboxaoraumuong", "thitboxaoxa", "thitnuong", "tomnuong"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh từ drawable");
        builder.setItems(imageNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy tên hình ảnh đã chọn
                String selectedImageName = imageNames[which];

                // Lấy ID tài nguyên drawable
                int resourceId = getResources().getIdentifier(selectedImageName, "drawable", getPackageName());

                // Cập nhật ImageView
                imgsp.setImageResource(resourceId);
                imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + resourceId); // Cập nhật URI
            }
        });
        builder.show();
    }

    // Chuyển đổi URI thành mảng byte
    private byte[] getBytesFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray(); // Trả về mảng byte của ảnh
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}