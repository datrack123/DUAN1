
package com.hanni.dao;

import com.hanni.entity.DonHang;
import com.hanni.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anctWin10
 */
public class DonHangDAO extends HanniDAO<DonHang, String>{
    final String INSERT_SQL = "INSERT INTO DonHang(MADH, MAKH, NgayLap, TongTien, TrangThai) VALUES(?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE DonHang SET MAKH =?, NgayLap =?, TongTien =?, TrangThai =? WHERE MADH = ?";
    final String DELETE_SQL = "DELETE FROM DonHang WHERE MADH = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM DonHang";
    final String SELECT_BY_ID_SQL = "SELECT * FROM DonHang WHERE MADH = ?";
//       [MaDH]
//      ,[MaKH]
//      ,[NgayLap]
//      ,[TongTien]
//      ,[TrangThai]

    @Override
    public void insert(DonHang entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaDH(),entity.getMaKH(), entity.getNgayLap(),
                entity.getTongTien(),entity.getTrangThai());
    }

    @Override
    public void update(DonHang entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getMaKH(), entity.getNgayLap(),
                entity.getTongTien(),entity.getTrangThai(),entity.getMaDH());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<DonHang> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public DonHang selectById(String id) {
        List<DonHang> list = selectBySql(SELECT_BY_ID_SQL, id);
            if(list.isEmpty()) {
                return null;
            }
            return list.get(0);
    }

    @Override
    public List<DonHang> selectBySql(String sql, Object... args) {
        List<DonHang> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
                DonHang entity = new DonHang();
                entity.setMaDH(rs.getString("MaDH"));
                entity.setMaKH(rs.getString("MaKH"));
                entity.setNgayLap(rs.getDate("NgayLap"));
                entity.setTongTien(rs.getDouble("TONGTIEN"));
                entity.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
}
