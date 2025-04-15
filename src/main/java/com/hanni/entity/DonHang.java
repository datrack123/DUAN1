/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hanni.entity;

import java.util.Date;

/**
 *
 * @author anctWin10
 */
public class DonHang {

    public DonHang() {
    }

    public DonHang(String MaDH, String MaKH, Date NgayLap, Double tongTien, Boolean trangThai) {
        this.MaDH = MaDH;
        this.MaKH = MaKH;
        this.NgayLap = NgayLap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public String getMaDH() {
        return MaDH;
    }

    public void setMaDH(String MaDH) {
        this.MaDH = MaDH;
    }

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String MaKH) {
        this.MaKH = MaKH;
    }

    public Date getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(Date NgayLap) {
        this.NgayLap = NgayLap;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }
    private String MaDH;
    private String MaKH;
    private Date NgayLap;
    private Double tongTien;
    private Boolean trangThai;

}
