package com.example.apphuongdancongthuc;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ChiTietSanPham implements Parcelable {
    String masp;
    String tensp;
    String nguyenlieu;
    String cachlam,ghichu,nguoidang;
    String mansp;
    byte[] anh;

    public ChiTietSanPham(String masp, String tensp, String nguyenlieu, String cachlam, String ghichu, String nguoidang, String mansp, byte[] anh) {
        this.masp = masp;
        this.tensp = tensp;
        this.nguyenlieu = nguyenlieu;
        this.cachlam = cachlam;
        this.ghichu = ghichu;
        this.nguoidang = nguoidang;
        this.mansp = mansp;
        this.anh = anh;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getNguyenlieu() {
        return nguyenlieu;
    }

    public void setNguyenlieu(String nguyenlieu) {
        this.nguyenlieu = nguyenlieu;
    }

    public String getCachlam() {
        return cachlam;
    }

    public void setCachlam(String cachlam) {
        this.cachlam = cachlam;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getNguoidang() {
        return nguoidang;
    }

    public void setNguoidang(String nguoidang) {
        this.nguoidang = nguoidang;
    }

    public String getMansp() {
        return mansp;
    }

    public void setMansp(String mansp) {
        this.mansp = mansp;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    protected ChiTietSanPham(Parcel in) {
        masp = in.readString();
        tensp = in.readString();
        nguyenlieu = in.readString();
        cachlam = in.readString();
        ghichu = in.readString();
        nguoidang = in.readString();
       mansp = in.readString();
        anh = in.createByteArray();
    }
    public static final Creator<ChiTietSanPham> CREATOR = new Creator<ChiTietSanPham>   () {
        @Override
        public ChiTietSanPham createFromParcel(Parcel in) {
            return new ChiTietSanPham(in);
        }

        @Override
        public ChiTietSanPham[] newArray(int size) {
            return new ChiTietSanPham[size];
        }
    };
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(masp);
        dest.writeString(tensp);
        dest.writeString(nguyenlieu);
        dest.writeString(cachlam);
        dest.writeString(ghichu);
        dest.writeString(nguoidang);
        dest.writeString(mansp);
        dest.writeByteArray(anh);
    }

}
