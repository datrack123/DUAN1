
package com.hanni.dao;

import com.hanni.entity.CSKH;
import com.hanni.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ChamSocKhachHangDAO extends HanniDAO<CSKH, String>{
 
    final String INSERT_SQL = "INSERT INTO CSKH(MaCSKH, NoiDung, MaTK, MaKH, Ngay) VALUES(?,?,?,?,?)";
    final String UPDATE_SQL = "UPDATE CSKH SET NoiDung =?, MaTK =?, MaKH =?, Ngay =? WHERE MaCSKH =?";
    final String DELETE_SQL = "DELETE FROM CSKH WHERE MaCSKH = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM CSKH";
    final String SELECT_BY_ID_SQL = "SELECT * FROM CSKH WHERE MaCSKH = ?";

    @Override
    public void insert(CSKH entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaCSKH(),entity.getNoiDung()
                ,entity.getMaTK(),entity.getMaKH(),entity.getNgay());
    }

    @Override
    public void update(CSKH entity) {
        JdbcHelper.update(UPDATE_SQL,entity.getNoiDung()
                ,entity.getMaTK(),entity.getMaKH(),entity.getNgay(),entity.getMaCSKH());    
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);    
    }

    @Override
    public List<CSKH> selectAll() {
        return selectBySql(SELECT_ALL_SQL);    
    }

    @Override
    public CSKH selectById(String id) {
        List<CSKH> list = selectBySql(SELECT_BY_ID_SQL, id);
            if(list.isEmpty()) {
                return null;
            }
            return list.get(0);    
    }

    @Override
    public List<CSKH> selectBySql(String sql, Object... args) {
        List<CSKH> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
                CSKH entity = new CSKH();
                entity.setMaCSKH(rs.getString("MaCSKH"));
                entity.setMaTK(rs.getString("MaTK"));
                entity.setMaKH(rs.getString("MaKH"));
                entity.setNgay(rs.getDate("Ngay"));
                entity.setNoiDung(rs.getString("NoiDung"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;    
    }
}
 