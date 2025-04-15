/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hanni.dao;

import com.hanni.entity.PhanLoaiSP;
import com.hanni.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anctWin10
 */
public class PhanLoaiSPDAO extends HanniDAO<PhanLoaiSP, String> {
    final String INSERT_SQL = "INSERT INTO PhanLoaiSP VALUES(?,?,?)";
    final String UPDATE_SQL = "UPDATE PhanLoaiSP SET MaPL =?, TenPL =?, ThongTin =? WHERE MaPL = ?";
    final String DELETE_SQL = "DELETE FROM PhanLoaiSP WHERE MaPL = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM PhanLoaiSP";
    final String SELECT_BY_ID_SQL = "SELECT * FROM PhanLoaiSP WHERE MaPL = ?";


    @Override
    public void insert(PhanLoaiSP entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaPL(),entity.getTenPL(), entity.getThongTin());
    }

    @Override
    public void update(PhanLoaiSP entity) {
        JdbcHelper.update(UPDATE_SQL,entity.getMaPL(),entity.getTenPL(), entity.getThongTin());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<PhanLoaiSP> selectAll() {
        return selectBySql(SELECT_ALL_SQL); 
    }

    @Override
    public PhanLoaiSP selectById(String id) {
        List<PhanLoaiSP> list = selectBySql(SELECT_BY_ID_SQL, id);
            if(list.isEmpty()) {
                return null;
            }
            return list.get(0);
    }

    @Override
    public List<PhanLoaiSP> selectBySql(String sql, Object... args) {
        List<PhanLoaiSP> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
                PhanLoaiSP entity = new PhanLoaiSP();
                entity.setMaPL(rs.getString("MaPL"));
                entity.setTenPL(rs.getString("TenPL"));
                entity.setThongTin(rs.getString("ThongTin"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
}
