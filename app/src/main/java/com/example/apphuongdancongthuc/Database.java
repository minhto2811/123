package com.example.apphuongdancongthuc;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //truy vấn không trả kết quả
    public void QueryData(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);

    }
    public void QueryDulieu(String sql, byte[]... params) {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement(sql);

        for (int i = 0; i < params.length; i++) {
            statement.bindBlob(i + 1, params[i]); // Gán blob vào câu lệnh
        }

        statement.executeInsert(); // Hoặc executeUpdate/Delete tùy thuộc vào câu lệnh
        database.close();
    }
    //truy vấn có kết quả
    public Cursor GetData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    // Phương thức để thực hiện câu lệnh SQL với một tham số blob
    public void QueryData(String sql, byte[] param) {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindBlob(1, param); // Gán blob vào câu lệnh
        statement.executeInsert();
        database.close();
    }

    //dùng trong trang thêm thông tin nhóm sp
    public void QueryData(String sql, Object... args) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql, args); // Sử dụng args để truyền tham số
        db.close();
    }
    // Thêm phương thức xóa nhóm sản phẩm
    public void deleteNhomSanPham(String maSo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("nhomsanpham", "maso = ?", new String[]{maSo});
        db.close();
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS taikhoan(tendn VARCHAR(20) PRIMARY KEY, matkhau VARCHAR(50), quyen VARCHAR(50))");
        db.execSQL("CREATE TABLE IF NOT EXISTS Lichsuxem (idlichsu INTEGER PRIMARY KEY AUTOINCREMENT, masp INTEGER, tendn VARCHAR(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS nhomsanpham(maso INTEGER PRIMARY KEY AUTOINCREMENT, tennsp NVARCHAR(200), anh BLOB)");
        db.execSQL("CREATE TABLE IF NOT EXISTS sanpham(masp INTEGER PRIMARY KEY AUTOINCREMENT, tensp NVARCHAR(200),nguyenlieu TEXT,cachlam TEXT,ghichu TEXT,nguoidang NVARCHAR(100),maso INTEGER , anh BLOB)");
        db.execSQL("CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT NOT NULL, userId TEXT NOT NULL, productId TEXT, role TEXT NOT NULL,images TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Lichsuxem");
        onCreate(db);
    }

    public void addLichSuXem(String tendn, String masp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("masp", masp);
        values.put("tendn", tendn);
        db.insert("Lichsuxem", null, values);
        db.close();
    }
    public List<LichSuXem> getLichSuXemByUser(String username) {
        List<LichSuXem> lichSuXemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Lichsuxem WHERE tendn = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0); // idlichsu
                String masp = cursor.getString(1); // masp
                String tendn = cursor.getString(2); // tendn

                LichSuXem lichSuXem = new LichSuXem(id, masp, tendn); // Giả sử LichSuXem có constructor này
                lichSuXemList.add(lichSuXem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lichSuXemList;
    }
    public SanPham getSanPhamByMasp(String masp) {
        SanPham sanPham = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM sanpham WHERE masp = ?", new String[]{masp});

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy các thuộc tính của sản phẩm từ cursor
                String tensp = cursor.getString(cursor.getColumnIndexOrThrow("tensp"));
                String nguyenlieu = cursor.getString(cursor.getColumnIndexOrThrow("nguyenlieu"));
                String cachlam = cursor.getString(cursor.getColumnIndexOrThrow("cachlam"));
                String ghichu = cursor.getString(cursor.getColumnIndexOrThrow("ghichu"));
                String nguoidang = cursor.getString(cursor.getColumnIndexOrThrow("nguoidang"));
                String mansp = cursor.getString(cursor.getColumnIndexOrThrow("maso"));
                byte[] anh = cursor.getBlob(cursor.getColumnIndexOrThrow("anh"));

                sanPham = new SanPham(masp, tensp, nguyenlieu, cachlam, ghichu, nguoidang, mansp, anh);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ghi lại lỗi nếu có
        } finally {
            if (cursor != null) {
                cursor.close(); // Đảm bảo đóng cursor
            }
        }

        return sanPham; // Trả về null nếu không tìm thấy sản phẩm
    }
}
