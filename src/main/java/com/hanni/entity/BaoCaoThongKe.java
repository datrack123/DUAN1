
package com.hanni.entity;

import java.util.Date;

/**
 *
 * @author Hoangdai
 */
public class BaoCaoThongKe {

    public BaoCaoThongKe() {
    }

    public BaoCaoThongKe(String maDH, String tenKhachHang, String TenSanPham, Date NgayLap, Double TongTien, Boolean TrangThai, int soLuong, Double donGia) {
        this.maDH = maDH;
        this.tenKhachHang = tenKhachHang;
        this.TenSanPham = TenSanPham;
        this.NgayLap = NgayLap;
        this.TongTien = TongTien;
        this.TrangThai = TrangThai;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public String gettenKhachHang() {
        return tenKhachHang;
    }

    public void settenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public void setTenSanPham(String TenSanPham) {
        this.TenSanPham = TenSanPham;
    }

    public Date getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(Date NgayLap) {
        this.NgayLap = NgayLap;
    }

    public Double getTongTien() {
        return TongTien;
    }

    public void setTongTien(Double TongTien) {
        this.TongTien = TongTien;
    }

    public Boolean getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(Boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }
    
    
    private String maDH;
    private String tenKhachHang;
    private String TenSanPham;
    private Date NgayLap;
    private Double TongTien;
    private Boolean TrangThai;
    private int soLuong;
    private Double donGia;
    private Double doanhThu;

    public BaoCaoThongKe(Double doanhThu) {
        this.doanhThu = doanhThu;
    }

    public Double getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(Double doanhThu) {
        this.doanhThu = doanhThu;
    }
    
    public BaoCaoThongKe(String ngay, String thang) {
        this.ngay = ngay;
        this.thang = thang;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getThang() {
        return thang;
    }

    public void setThang(String thang) {
        this.thang = thang;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }
    
    private String ngay, thang, nam;
    
}
