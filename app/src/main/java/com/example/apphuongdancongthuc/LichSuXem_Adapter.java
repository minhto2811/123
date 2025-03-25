package com.example.apphuongdancongthuc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LichSuXem_Adapter extends BaseAdapter {
     Context context;
   int layout;
   List<LichSuXem> lichSuXemList;
    Database database; // Đối tượng Database


    public LichSuXem_Adapter(Context context, int layout, List<LichSuXem> lichSuXemList) {
        this.context = context;
        this.layout = layout; // khởi tạo layout từ tham số
        this.lichSuXemList = lichSuXemList;
        this.database = new Database(context, "banhang.db", null, 1);
    }
    @Override
    public int getCount() {
        return lichSuXemList.size();
    }

    @Override
    public Object getItem(int position) {
        return lichSuXemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ds_lichsuxem, parent, false);
            holder = new ViewHolder();
            holder.TenSP = convertView.findViewById(R.id.tensp);
            holder.nguyenlieu = convertView.findViewById(R.id.nguyenlieu);
            holder.cachlam = convertView.findViewById(R.id.cachlam);
            holder.ghichu = convertView.findViewById(R.id.ghichu);
            holder.nguoidang = convertView.findViewById(R.id.nguoidang);
            holder.maso = convertView.findViewById(R.id.manhomsanpham);
            holder.masp = convertView.findViewById(R.id.masp);
            holder.tendn = convertView.findViewById(R.id.tendn);
            holder.anh = convertView.findViewById(R.id.imgsp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy thông tin sản phẩm từ lịch sử xem
        LichSuXem lichSuXem = lichSuXemList.get(position);
        String masp = lichSuXem.getMasp();

        // Kiểm tra mã sản phẩm không null hoặc rỗng
        if (masp != null && !masp.isEmpty()) {
            SanPham sanPham = database.getSanPhamByMasp(masp);

            if (sanPham != null) {
                holder.TenSP.setText(sanPham.getTensp());
                holder.nguyenlieu.setText(sanPham.getNguyenlieu());
                holder.cachlam.setText(sanPham.getCachlam());
                holder.ghichu.setText(sanPham.getGhichu());
                holder.nguoidang.setText(sanPham.getNguoidang());
                holder.masp.setText(sanPham.getMasp());
                holder.maso.setText(sanPham.getMansp());

                // Nếu sản phẩm có ảnh, hiển thị ảnh
                if (sanPham.getAnh() != null && sanPham.getAnh().length > 0) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(sanPham.getAnh(), 0, sanPham.getAnh().length);
                    holder.anh.setImageBitmap(bmp);
                } else {
                    holder.anh.setImageResource(R.drawable.login); // Đặt hình ảnh mặc định nếu không có ảnh
                }
            }
        } else {
            // Xử lý trường hợp không có sản phẩm
            holder.TenSP.setText("Sản phẩm không tìm thấy");
            holder.nguyenlieu.setText("");
            holder.cachlam.setText("");
            holder.ghichu.setText("");
            holder.nguoidang.setText("");
            holder.masp.setText("");
            holder.tendn.setText("");
            holder.maso.setText("");
            holder.anh.setImageResource(R.drawable.login); // Hình ảnh mặc định
        }

        return convertView;
    }

    static class ViewHolder {
        TextView TenSP, nguyenlieu, cachlam, ghichu, nguoidang, maso, masp,tendn;

        ImageView anh;
    }
}