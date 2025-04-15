
package com.hanni.entity;

/**
 *
 * @author anctWin10
 */
public class DonHangChiTiet {

    public DonHangChiTiet(String maDHCT, String maSP, String maDH, Double donGia, int soLuong) {
        this.maDHCT = maDHCT;
        this.maSP = maSP;
        this.maDH = maDH;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public DonHangChiTiet() {
    }

    public String getMaDHCT() {
        return maDHCT;
    }

    public void setMaDHCT(String maDHCT) {
        this.maDHCT = maDHCT;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return "DonHangChiTiet{" + "maDHCT=" + maDHCT + ", maSP=" + maSP + ", maDH=" + maDH + ", donGia=" + donGia + ", soLuong=" + soLuong + '}';
    }
    private String maDHCT;
    private String maSP;
    private String maDH;
    private Double donGia;
    private int soLuong;

    public void setTenSP(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
