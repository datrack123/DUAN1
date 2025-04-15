
package com.hanni.dao;

import com.hanni.entity.DonHangChiTiet;
import com.hanni.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anctWin10
 */
public class DonHangChiTietDAO extends HanniDAO<DonHangChiTiet, String>{
    final String INSERT_SQL = "INSERT INTO DonHangChiTiet(MADHCT, MASP, MADH, DONGIA, SOLUONG) VALUES(?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE DonHangChiTiet SET MASP =?, MADH =?, DONGIA =?, SOLUONG =? WHERE MADHCT = ?";
    final String DELETE_SQL = "DELETE FROM DonHangChiTiet WHERE MADHCT = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM DonHangChiTiet";
    final String SELECT_BY_ID_SQL = "SELECT * FROM DonHangChiTiet WHERE MADHCT = ?";
//    final String SELECT_tenSP = "SELECT SanPham.tenSP\n" +
//                                    "FROM SanPham\n" +
//                                    "INNER JOIN DonHangChiTiet ON SanPham.maSP = DonHangChiTiet.maSP\n" +
//                                    "WHERE DonHangChiTiet.maSP = '?';" ;
//       [MaDHCT]
//      ,[MASP]
//      ,[MaDH]
//      ,[DonGia]
//      ,[Soluong]

    @Override
    public void insert(DonHangChiTiet entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaDHCT(),entity.getMaSP(), entity.getMaDH(),
                entity.getDonGia(),entity.getSoLuong());
    }

    @Override
    public void update(DonHangChiTiet entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getMaSP(), entity.getMaDH(),
                entity.getDonGia(),entity.getSoLuong(),entity.getMaDHCT());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<DonHangChiTiet> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public DonHangChiTiet selectById(String id) {
        List<DonHangChiTiet> list = selectBySql(SELECT_BY_ID_SQL, id);
            if(list.isEmpty()) {
                return null;
            }
            return list.get(0);
    }

    @Override
    public List<DonHangChiTiet> selectBySql(String sql, Object... args) {
        List<DonHangChiTiet> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
                DonHangChiTiet entity = new DonHangChiTiet();
                entity.setMaDHCT(rs.getString("MaDHCT"));
                entity.setMaSP(rs.getString("MaSP"));
                entity.setMaDH(rs.getString("MADH"));
                entity.setDonGia(rs.getDouble("DONGIA"));
                entity.setSoLuong(rs.getInt("SoLuong"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
}
