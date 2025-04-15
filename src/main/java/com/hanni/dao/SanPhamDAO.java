/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hanni.dao;

import com.hanni.entity.SanPham;
import com.hanni.entity.TaiKhoan;
import com.hanni.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anctWin10
 */
public class SanPhamDAO extends HanniDAO<SanPham, String> {
    final String INSERT_SQL = "INSERT INTO SANPHAM(MASP, TENSP, GIA, SIZE, SOLUONG, HINHANH, THONGTIN, MAPL) VALUES(?,?,?,?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE SANPHAM SET TENSP =?, GIA =?, SIZE =?, SOLUONG =?, HINHANH =?, THONGTIN =?, MAPL =? WHERE MaSP = ?";
    final String DELETE_SQL = "DELETE FROM SANPHAM WHERE MaSP = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM SANPHAM";
    final String SELECT_BY_ID_SQL = "SELECT * FROM SANPHAM WHERE MaSP = ?";


    @Override
    public void insert(SanPham entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaSP(),entity.getTenSP(), entity.getGia(),
                entity.getSize(),entity.getSoLuong(),entity.getHinhAnh(), entity.getThongTin(), entity.getMaPL());
    }

    @Override
    public void update(SanPham entity) {
        JdbcHelper.update(UPDATE_SQL,entity.getTenSP(), entity.getGia(),
                entity.getSize(),entity.getSoLuong(),entity.getHinhAnh(),
                entity.getThongTin(), entity.getMaPL(), entity.getMaSP());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<SanPham> selectAll() {
        return selectBySql(SELECT_ALL_SQL); 
    }

    @Override
    public SanPham selectById(String id) {
        List<SanPham> list = selectBySql(SELECT_BY_ID_SQL, id);
            if(list.isEmpty()) {
                return null;
            }
            return list.get(0);
    }

    @Override
    public List<SanPham> selectBySql(String sql, Object... args) {
        List<SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
                SanPham entity = new SanPham();
                entity.setMaSP(rs.getString("MaSP"));
                entity.setTenSP(rs.getString("TenSP"));
                entity.setGia(rs.getDouble("Gia"));
                entity.setSize(rs.getDouble("Size"));
                entity.setSoLuong(rs.getInt("SoLuong"));
                entity.setHinhAnh(rs.getString("HinhAnh"));
                entity.setThongTin(rs.getString("ThongTin"));
                entity.setMaPL(rs.getString("MaPL"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
}
