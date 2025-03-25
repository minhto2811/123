package com.example.apphuongdancongthuc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class SanPhamAdapter extends BaseAdapter {

    private Context context;
    private Uri selectedImageUri; // Biến lưu trữ URI đã chọn
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // Định nghĩa mã yêu cầu
    private ArrayList<SanPham> spList;
    private boolean showFullDetails; // Biến để xác định xem có hiển thị 7 thuộc tính hay không
    private Database database;
    private boolean isAdmin;

    public SanPhamAdapter(Context context, ArrayList<SanPham> bacsiList, boolean showFullDetails, boolean isAdmin) {
        this.context = context;
        this.spList = bacsiList;
        this.isAdmin = isAdmin;
        this.showFullDetails = showFullDetails; // Khởi tạo biến
        this.database = new Database(context, "banhang.db", null, 1);
    }

    @Override
    public int getCount() {
        return spList.size();
    }

    @Override
    public Object getItem(int position) {
        return spList.get(position);
    }

    public void setSelectedImageUri(Uri uri) {
        this.selectedImageUri = uri; // Setter để cập nhật URI
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (showFullDetails) {
            return getViewWith8Properties(position, convertView, parent);
        } else {
            return getViewWith4Properties(position, convertView, parent);
        }
    }


    public View getViewWith8Properties(int i, View view, ViewGroup parent) {
        View viewtemp;
        if (view == null) {
            viewtemp = LayoutInflater.from(parent.getContext()).inflate(R.layout.ds_sanpham, parent, false);
        } else {
            viewtemp = view;
        }

        SanPham tt = spList.get(i);

        TextView masp = viewtemp.findViewById(R.id.masp);
        TextView tensp = viewtemp.findViewById(R.id.tensp);
        TextView nguyenlieu = viewtemp.findViewById(R.id.nguyenlieu);
        TextView cachlam = viewtemp.findViewById(R.id.cachlam);
        TextView ghichu = viewtemp.findViewById(R.id.ghichu);
        TextView nguoidang = viewtemp.findViewById(R.id.nguoidang);
        TextView manhomsanpham = viewtemp.findViewById(R.id.manhomsanpham);

        ImageView anh = viewtemp.findViewById(R.id.imgsp);
        ImageButton sua = viewtemp.findViewById(R.id.imgsua);
        ImageButton xoa = viewtemp.findViewById(R.id.imgxoa);


        masp.setText(tt.getMasp());
        tensp.setText(tt.getTensp());
        nguyenlieu.setText(String.valueOf(tt.getNguyenlieu()));
        cachlam.setText(tt.getCachlam());
        ghichu.setText(tt.getGhichu());
        nguoidang.setText(tt.getNguoidang());
        manhomsanpham.setText(tt.getMansp());

        // Hiển thị ảnh bác sĩ
        byte[] anhByteArray = tt.getAnh();
        if (anhByteArray != null && anhByteArray.length > 0) {
            Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
            anh.setImageBitmap(imganhbs);
        } else {
            anh.setImageResource(R.drawable.vest);
        }

        // Sự kiện cho nút "Sửa"
        sua.setOnClickListener(view1 -> showEditDialog(tt));

        // Sự kiện cho nút "Xóa"
        xoa.setOnClickListener(v -> {
            new AlertDialog.Builder(parent.getContext())
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        SQLiteDatabase db = database.getWritableDatabase();
                        int rowsAffected = db.delete("sanpham", "masp = ?", new String[]{tt.getMasp()});
                        if (rowsAffected > 0) {
                            spList.remove(i);
                            notifyDataSetChanged(); // Cập nhật giao diện
                            Toast.makeText(parent.getContext(), "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(parent.getContext(), "Không tìm thấy sản phẩm để xóa", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        return viewtemp;
    }

    public View getViewWith4Properties(int i, View view, ViewGroup parent) {
        View viewtemp;
        if (view == null) {
            viewtemp = LayoutInflater.from(parent.getContext()).inflate(R.layout.ds_hienthi_gridview1_nguoidung, parent, false);
        } else {
            viewtemp = view;
        }

        SanPham tt = spList.get(i);
        TextView masp = viewtemp.findViewById(R.id.masp);
        TextView tensp = viewtemp.findViewById(R.id.tensp);
        TextView nguyenlieu = viewtemp.findViewById(R.id.nguyenlieu);
        TextView cachlam = viewtemp.findViewById(R.id.cachlam);
        TextView ghichu = viewtemp.findViewById(R.id.ghichu);
        TextView nguoidang = viewtemp.findViewById(R.id.nguoidang);
        TextView manhomsanpham = viewtemp.findViewById(R.id.manhomsanpham);
        ImageView anh = viewtemp.findViewById(R.id.imgsp);

        masp.setText(tt.getMasp());
        tensp.setText(tt.getTensp());
        nguyenlieu.setText(String.valueOf(tt.getNguyenlieu()));
        cachlam.setText(tt.getCachlam());
        ghichu.setText(tt.getGhichu());
        nguoidang.setText(tt.getNguoidang());
        manhomsanpham.setText(tt.getMansp());

        // Hiển thị ảnh sản phẩm
        byte[] anhByteArray = tt.getAnh();
        if (anhByteArray != null && anhByteArray.length > 0) {
            Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
            anh.setImageBitmap(imganhbs);
        } else {
            anh.setImageResource(R.drawable.vest);
        }

        // Thêm sự kiện click để chuyển đến trang chi tiết
        viewtemp.setOnClickListener(v -> {
            Intent intent = new Intent(parent.getContext(), ChiTietSanPham_Activity.class);
            ChiTietSanPham chiTietSanPham = new ChiTietSanPham(
                    tt.getMasp(),
                    tt.getTensp(),
                    tt.getNguyenlieu(),
                    tt.getCachlam(),
                    tt.getGhichu(),
                    tt.getNguoidang(),
                    tt.getMansp(),
                    tt.getAnh()
            );
            intent.putExtra("chitietsanpham", chiTietSanPham); // Truyền đối tượng ChiTietSanPham
            parent.getContext().startActivity(intent);
        });

        return viewtemp;
    }

    // Hàm hiển thị dialog sửa thông tin bác sĩ
    private void showEditDialog(SanPham tt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_sua_san_pham, null);
        builder.setView(dialogView);

        // Các trường EditText và Spinner
        EditText editMasp = dialogView.findViewById(R.id.masp);
        EditText editTensp = dialogView.findViewById(R.id.tensp);
        EditText editNguyenlieu = dialogView.findViewById(R.id.nguyenlieu);
        EditText editCachlam = dialogView.findViewById(R.id.cachlam);
        EditText editGhichu = dialogView.findViewById(R.id.ghichu);
        EditText editNguoidang = dialogView.findViewById(R.id.nguoidang);
        Spinner mansp = dialogView.findViewById(R.id.manhomsanpham);
        ImageView imgsp = dialogView.findViewById(R.id.imgsp);

        // Load danh sách nhóm sản phẩm
        loadTenNhomSanPham(mansp);

        // Điền dữ liệu hiện tại vào các trường
        editMasp.setText(tt.getMasp());
        editTensp.setText(tt.getTensp());
        editNguyenlieu.setText(tt.getNguyenlieu());
        editCachlam.setText(tt.getCachlam());
        editGhichu.setText(tt.getGhichu());
        editNguoidang.setText(tt.getNguoidang());
        editNguoidang.setEnabled(isAdmin);

        // Chọn nhóm sản phẩm hiện tại (dựa trên maso)
        for (int i = 0; i < mangNSPList.size(); i++) {
            if (mangNSPList.get(i).getMa().equals(tt.getMansp())) {
                mansp.setSelection(i);
                break;
            }
        }

        // Hiển thị ảnh sản phẩm
        byte[] anhByteArray = tt.getAnh();
        if (anhByteArray != null && anhByteArray.length > 0) {
            Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
            imgsp.setImageBitmap(imganhbs);
        } else {
            imgsp.setImageResource(R.drawable.vest);
        }
        // Sự kiện chọn ảnh từ drawable
        Button imgAddanh = dialogView.findViewById(R.id.btnAddImg);
        imgAddanh.setOnClickListener(v1 -> openDrawableImagePicker(imgsp));
        // Sự kiện chọn ảnh từ bộ nhớ
        imgsp.setOnClickListener(imgView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật thông tin sản phẩm
            updateSanPham(tt, editMasp, editTensp, editNguyenlieu, editCachlam, editGhichu, editNguoidang, mansp);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Phương thức cập nhật thông tin sản phẩm
    private void updateSanPham(SanPham tt, EditText editMasp, EditText editTensp, EditText editNguyenlieu, EditText editCachlam, EditText editGhichu, EditText editNguoidang, Spinner editMansp) {
        String newMasp = editMasp.getText().toString().trim();
        String newTensp = editTensp.getText().toString().trim();
        String newNguyenlieu = editNguyenlieu.getText().toString().trim();
        String newCachlam = editCachlam.getText().toString().trim();
        String newGhichu = editGhichu.getText().toString().trim();
        String newNguoidang = editNguoidang.getText().toString().trim();

        // Lấy maso từ spinner
        String newMansp = ((NhomSanPham) editMansp.getSelectedItem()).getMa(); // Lấy maso thay vì tennhom

        // Cập nhật ảnh nếu có
        byte[] newAnh = selectedImageUri != null ? getBytesFromUri(selectedImageUri) : null;

        // Cập nhật vào cơ sở dữ liệu
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("masp", newMasp);
        values.put("tensp", newTensp);
        values.put("nguyenlieu", newNguyenlieu);
        values.put("cachlam", newCachlam);
        values.put("ghichu", newGhichu);
        values.put("nguoidang", newNguoidang);
        values.put("maso", newMansp); // Lưu maso

        if (newAnh != null) {
            values.put("anh", newAnh); // Cập nhật ảnh nếu có
        }

        // Cập nhật dữ liệu
        db.update("sanpham", values, "masp = ?", new String[]{tt.getMasp()});

        // Cập nhật đối tượng SanPham
        tt.setMasp(newMasp);
        tt.setTensp(newTensp);
        tt.setNguyenlieu(newNguyenlieu);
        tt.setCachlam(newCachlam);
        tt.setGhichu(newGhichu);
        tt.setNguoidang(newNguoidang);
        tt.setMansp(newMansp);
        if (newAnh != null) {
            tt.setAnh(newAnh); // Cập nhật ảnh nếu có
        }

        notifyDataSetChanged(); // Cập nhật giao diện
    }

    // Phương thức để mở hộp thoại chọn ảnh từ drawable
    private void openDrawableImagePicker(ImageView imgBacSi) {
        final String[] imageNames = {"bobittet", "bosotvang", "bunbohue", "bunmoc", "bunrieucua", "cakho", "casotcachua", "casotcam", "changanuong", "galuoc", "ganuong", "mucnhoithit", "mucnuong", "mucxaoraucu", "phobo", "salat1", "salat2", "salat3", "salattom", "thitboxaoraumuong", "thitboxaoxa", "thitnuong", "tomnuong"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Chọn ảnh từ drawable");
        builder.setItems(imageNames, (dialog, which) -> {
            // Lấy tên hình ảnh đã chọn
            String selectedImageName = imageNames[which];

            // Lấy ID tài nguyên drawable
            int resourceId = context.getResources().getIdentifier(selectedImageName, "drawable", context.getPackageName());

            // Cập nhật ImageView
            imgBacSi.setImageResource(resourceId);

            // Cập nhật URI
            selectedImageUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId);
        });
        builder.show();
    }

    private ArrayList<NhomSanPham> mangNSPList;

    private void loadTenNhomSanPham(Spinner mansp) {
        mangNSPList = new ArrayList<>();
        Cursor cursor = database.GetData("SELECT maso, tennsp FROM nhomsanpham"); // Lấy maso và tennsp

        while (cursor.moveToNext()) {
            String maso = cursor.getString(0); // Cột 0
            String tennhom = cursor.getString(1); // Cột 1
            mangNSPList.add(new NhomSanPham(maso, tennhom, null)); // Thêm vào danh sách
        }

        // Tạo adapter cho Spinner
        ArrayAdapter<NhomSanPham> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mangNSPList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mansp.setAdapter(adapter);
    }

    // Chuyển đổi URI thành mảng byte
    private byte[] getBytesFromUri(Uri uri) {
        if (uri == null) {
            return null; // Trả về null nếu URI không hợp lệ
        }
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
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
