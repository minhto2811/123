package com.example.apphuongdancongthuc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "banhang.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          }

    public void insertComment(String productId, String username, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productId", productId);
        values.put("username", username);
        values.put("content", content);
        db.insert("Comment", null, values);
    }

    public List<Comment> getCommentsByProduct(String productId) {
        List<Comment> comments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username, content FROM Comment WHERE productId = ?", new String[]{productId});
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(0);
                String content = cursor.getString(1);
                comments.add(new Comment(username, content));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return comments;
    }


    // Phương thức để lấy sản phẩm theo masp
    public SanPham getSanPhamByMasp(String masp) {
        SanPham sanPham = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM sanpham WHERE masp = ?", new String[]{masp});
        if (cursor != null && cursor.moveToFirst()) {
            String masp1 = cursor.getString(cursor.getColumnIndexOrThrow("masp"));
            String tensp = cursor.getString(cursor.getColumnIndexOrThrow("tensp"));
            String nguyenlieu= cursor.getString(cursor.getColumnIndexOrThrow("nguyenlieu"));
            String cachlam = cursor.getString(cursor.getColumnIndexOrThrow("cachlam"));
            String ghichu = cursor.getString(cursor.getColumnIndexOrThrow("ghichu"));
            String nguoidang = cursor.getString(cursor.getColumnIndexOrThrow("nguoidang"));
            String mansp = cursor.getString(cursor.getColumnIndexOrThrow("maso"));
            byte[] anh = cursor.getBlob(cursor.getColumnIndexOrThrow("anh"));

            sanPham  = new SanPham(masp1, tensp, nguyenlieu, cachlam, ghichu, nguoidang, mansp, anh);

        }

        if (cursor != null) {
            cursor.close();
        }
        return sanPham;
    }
    public String getTenSanPhamByMaSp(int masp) {
        String tensp = null;
        SQLiteDatabase db = this.getReadableDatabase();

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery("SELECT tensp FROM sanpham WHERE masp = ?", new String[]{String.valueOf(masp)});

        // Kiểm tra cursor không null và di chuyển đến bản ghi đầu tiên
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy tên sản phẩm từ cursor
            int tenspIndex = cursor.getColumnIndex("tensp");
            if (tenspIndex != -1) {
                tensp = cursor.getString(tenspIndex);
            } else {
                Log.e("Database Error", "Column 'tensp' not found.");
            }
        } else {
            Log.e("Database Error", "Cursor is empty or null.");
        }

        // Đóng cursor
        if (cursor != null) {
            cursor.close();
        }

        return tensp; // Trả về tên sản phẩm
    }

    public List<SanPham> getProductsByNhomSpId(String nhomSpId) {
        List<SanPham> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn để lấy sản phẩm theo nhomSpId
        Cursor cursor = db.rawQuery("SELECT * FROM sanpham WHERE maso = ?", new String[]{nhomSpId});

        if (cursor.moveToFirst()) {
            do {
                String masp = cursor.getString(cursor.getColumnIndexOrThrow("masp"));
                String tensp = cursor.getString(cursor.getColumnIndexOrThrow("tensp"));
               String nguyenlieu= cursor.getString(cursor.getColumnIndexOrThrow("nguyenlieu"));
                String cachlam = cursor.getString(cursor.getColumnIndexOrThrow("cachlam"));
                String ghichu = cursor.getString(cursor.getColumnIndexOrThrow("ghichu"));
                String nguoidang = cursor.getString(cursor.getColumnIndexOrThrow("nguoidang"));
                String mansp = cursor.getString(cursor.getColumnIndexOrThrow("maso"));
                byte[] anh = cursor.getBlob(cursor.getColumnIndexOrThrow("anh"));

                SanPham sanPham = new SanPham(masp, tensp, nguyenlieu, cachlam, ghichu, nguoidang, mansp, anh);
                productList.add(sanPham);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    // Phương thức tìm kiếm sản phẩm theo tên
    public ArrayList<SanPham> searchSanPhamByName(String name) {
        ArrayList<SanPham> sanPhamList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Sử dụng LIKE để tìm kiếm gần đúng
        String query = "SELECT * FROM sanpham WHERE tensp LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + name + "%"});

        if (cursor.moveToFirst()) {
            do {
                // Lấy chỉ số cột
                int maspIndex = cursor.getColumnIndex("masp");
                int tenspIndex = cursor.getColumnIndex("tensp");
                int nguyenlieuIndex = cursor.getColumnIndex("nguyenlieu");
                int cachlamIndex = cursor.getColumnIndex("cachlam");
                int ghichuIndex = cursor.getColumnIndex("ghichu");
                int nguoidangIndex = cursor.getColumnIndex("nguoidang");
                int manhomsanphamIndex = cursor.getColumnIndex("maso");
                int anhIndex = cursor.getColumnIndex("anh");

                // Kiểm tra và lấy giá trị
                if (maspIndex != -1 && tenspIndex != -1) {
                    String masp = cursor.getString(maspIndex);
                    String tensp = cursor.getString(tenspIndex);
                    String nguyenlieu = (nguyenlieuIndex != -1) ? cursor.getString(nguyenlieuIndex) : "";
                    String cachlam = (cachlamIndex != -1) ? cursor.getString(cachlamIndex) : "";
                    String ghichu = (ghichuIndex != -1) ? cursor.getString(ghichuIndex) : "";
                    String nguoidang= (nguoidangIndex != -1) ? cursor.getString(nguoidangIndex) : "";
                    String manhomsanpham = (manhomsanphamIndex != -1) ? cursor.getString(manhomsanphamIndex) : "";
                    byte[] anh = (anhIndex != -1) ? cursor.getBlob(anhIndex) : null;

                    SanPham sanPham = new SanPham(masp, tensp, nguyenlieu, cachlam, ghichu, nguoidang, manhomsanpham, anh);
                    sanPhamList.add(sanPham);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sanPhamList;
    }
    public void addLichSuXem(String tendn, String masp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("masp", masp);
        values.put("tendn", tendn);
        db.insert("Lichsuxem", null, values);
        db.close();
    }
    public List<LichSuXem> getLichSuXemByUser(String tendn) {
        List<LichSuXem> lichSuXemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Lichsuxem", null, "tendn = ?", new String[]{tendn}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String idlichsu = cursor.getString(cursor.getColumnIndexOrThrow("idlichsu"));
                String masp = cursor.getString(cursor.getColumnIndexOrThrow("masp"));
                lichSuXemList.add(new LichSuXem(idlichsu, masp, tendn));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lichSuXemList;
    }
}