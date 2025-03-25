package com.example.apphuongdancongthuc;

public class LichSuXem {
    String idlichsu,masp,tendn;


    public LichSuXem(String idlichsu, String masp, String tendn) {
        this.idlichsu = idlichsu;
        this.masp = masp;
        this.tendn = tendn;
    }

    public String getIdlichsu() {
        return idlichsu;
    }

    public void setIdlichsu(String idlichsu) {
        this.idlichsu = idlichsu;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTendn() {
        return tendn;
    }

    public void setTendn(String tendn) {
        this.tendn = tendn;
    }
}
