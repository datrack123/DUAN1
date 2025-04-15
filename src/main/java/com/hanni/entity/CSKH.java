package com.hanni.entity;

import java.util.Date;


/**
 *
 * @author Admin
 */
public class CSKH {

//MaCSKH, NoiDung, MaTK, MaKH, Ngay
    public CSKH() {
    }

    public CSKH(String maCSKH, String noiDung, String maKH, String maTK, Date ngay) {
        this.maCSKH = maCSKH;
        this.noiDung = noiDung;
        this.maKH = maKH;
        this.maTK = maTK;
        this.ngay = ngay;
    }

    public String getMaCSKH() {
        return maCSKH;
    }

    public void setMaCSKH(String maCSKH) {
        this.maCSKH = maCSKH;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }
    private String maCSKH;
    private String noiDung;
    private String maKH;
    private String maTK;
    private Date ngay;
}
