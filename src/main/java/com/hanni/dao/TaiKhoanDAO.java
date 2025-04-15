package com.hanni.dao;

import com.hanni.entity.TaiKhoan;
import com.hanni.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO extends HanniDAO<TaiKhoan, String>{
    final String INSERT_SQL = "INSERT INTO TaiKhoan(MATK, MATKHAU, HOTEN, DiaChi, SDT, Email, VAITRO) VALUES(?,?,?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE TaiKhoan SET MATKHAU = ?, HOTEN = ?,DiaChi = ?, SDT = ?, Email = ?, VAITRO = ? WHERE MaTK = ?";
    final String DELETE_SQL = "DELETE FROM TaiKhoan WHERE MaTK = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM TaiKhoan";
    final String SELECT_BY_ID_SQL = "SELECT * FROM TaiKhoan WHERE MaTK = ?";

    @Override
    public void insert(TaiKhoan entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaTK(),entity.getMatKhau(), entity.getHoTen(),
                entity.getDiaChi(),entity.getSdt(),entity.getEmail(), entity.isVaiTro());
    }

    @Override
    public void update(TaiKhoan entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getMatKhau(), entity.getHoTen(),
                entity.getDiaChi(),entity.getSdt(),entity.getEmail(), entity.isVaiTro(), entity.getMaTK());    
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);    
    }

    @Override
    public List<TaiKhoan> selectAll() {
        return selectBySql(SELECT_ALL_SQL);    
    }

    @Override
    public TaiKhoan selectById(String id) {
        List<TaiKhoan> list = selectBySql(SELECT_BY_ID_SQL, id);
            if(list.isEmpty()) {
                return null;
            }
            return list.get(0);    
    }

    @Override
    public List<TaiKhoan> selectBySql(String sql, Object... args) {
        List<TaiKhoan> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
                TaiKhoan entity = new TaiKhoan();
                entity.setMaTK(rs.getString("MaTK"));
                entity.setMatKhau(rs.getString("MatKhau"));
                entity.setHoTen(rs.getString("HoTen"));
                entity.setDiaChi(rs.getString("Diachi"));
                entity.setSdt(rs.getString("SDT"));
                entity.setEmail(rs.getString("Email"));
                entity.setVaiTro(rs.getBoolean("VaiTro"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;    
    }
    
    
}
