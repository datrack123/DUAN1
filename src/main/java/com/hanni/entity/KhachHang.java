
package com.hanni.entity;

/**
 *
 * @author Admin
 */
public class KhachHang {

    public KhachHang() {
    }

    public KhachHang(String maKH, String hoTen, String DiaChi, String email, String sdt) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.DiaChi = DiaChi;
        this.email = email;
        this.sdt = sdt;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    private String maKH;
    private String hoTen;
    private String DiaChi;
    private String email;
    private String sdt;
    

   
}

