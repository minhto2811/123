package com.example.apphuongdancongthuc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChiTietSanPham_Adapter extends BaseAdapter {
    private Context context;

    private ArrayList<SanPham> spList;
    private boolean showFullDetails; // Biến để xác định xem có hiển thị 7 thuộc tính hay không


    public ChiTietSanPham_Adapter(Context context, ArrayList<SanPham> bacsiList, boolean showFullDetails) {
        this.context = context;
        this.spList = bacsiList;
        this.showFullDetails = showFullDetails; // Khởi tạo biến

    }

    @Override
    public int getCount() {
        return spList.size();
    }

    @Override
    public Object getItem(int position) {
        return spList.get(position);
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
}
