package com.hanni.dao;


import com.hanni.entity.KhachHang;
import com.hanni.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class KhachHangDAO extends HanniDAO<KhachHang, String>{
    final String INSERT_SQL = "INSERT INTO KhachHang(MAKH, HOTEN, DiaChi, SDT, Email) VALUES(?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE KhachHang SET HOTEN =?, DiaChi =?, SDT =?, Email =? WHERE MAKH = ?";
    final String DELETE_SQL = "DELETE FROM KhachHang WHERE MaKH = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM KhachHang";
    final String SELECT_BY_ID_SQL = "SELECT * FROM KhachHang WHERE MaKH = ?";

    @Override
    public void insert(KhachHang entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaKH(), entity.getHoTen(),
                entity.getDiaChi(),entity.getSdt(),entity.getEmail());
    }

    @Override
    public void update(KhachHang entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getHoTen(),
                entity.getDiaChi(),entity.getSdt(),entity.getEmail(), entity.getMaKH());    
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);    
    }

    @Override
    public List<KhachHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);    
    }

    @Override
    public KhachHang selectById(String id) {
        List<KhachHang> list = selectBySql(SELECT_BY_ID_SQL, id);
            if(list.isEmpty()) {
                return null;
            }
            return list.get(0);    
    }

    @Override//lay dlieu tu sql do len list
    public List<KhachHang> selectBySql(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
                KhachHang entity = new KhachHang();
                entity.setMaKH(rs.getString("MaKH"));          
                entity.setHoTen(rs.getString("HoTen"));
                entity.setDiaChi(rs.getString("Diachi"));
                entity.setSdt(rs.getString("SDT"));
                entity.setEmail(rs.getString("Email"));     
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;    
    }
    
    
}