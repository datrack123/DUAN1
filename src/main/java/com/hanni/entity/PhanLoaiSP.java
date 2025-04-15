/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hanni.entity;

/**
 *
 * @author anctWin10
 */
public class PhanLoaiSP {

    public PhanLoaiSP() {
    }

    public PhanLoaiSP(String maPL, String tenPL, String thongTin) {
        this.maPL = maPL;
        this.tenPL = tenPL;
        this.thongTin = thongTin;
    }

    public String getMaPL() {
        return maPL;
    }

    public void setMaPL(String maPL) {
        this.maPL = maPL;
    }

    public String getTenPL() {
        return tenPL;
    }

    public void setTenPL(String tenPL) {
        this.tenPL = tenPL;
    }

    public String getThongTin() {
        return thongTin;
    }

    public void setThongTin(String thongTin) {
        this.thongTin = thongTin;
    }

    
    private String maPL;
    private String tenPL;
    private String thongTin;

   
}
