package com.hanni.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class JdbcHelper {
    private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String url ="jdbc:sqlserver://localhost:1433;database=HANNI_STORE2;encrypt=true;trustServerCertificate=true";
    private static String user = "sa";
    private static String pass = "Nam123123@";
     
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static PreparedStatement getStmt(String sql, Object... args) throws SQLException
    {
        Connection connection = DriverManager.getConnection(url, user, pass);
        PreparedStatement pstmt = null;
        if(sql.trim().startsWith("{"))
        {
            pstmt = connection.prepareCall(sql);
        }else
        {
            pstmt = connection.prepareStatement(sql);
        }
        for(int i = 0; i < args.length; i++)
        {
            pstmt.setObject(i + 1, args[i]);
        }
        return pstmt;
    }
    
    public static int update (String sql, Object... args)
    {
        try {
            PreparedStatement stmt = getStmt(sql, args);
            try {
                return stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static ResultSet query(String sql, Object... args)
    {
        try {
            PreparedStatement stmt = getStmt(sql, args);
            return stmt.executeQuery();
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
    public static Object value(String sql, Object... args)
    {
        try {
         ResultSet rs = query(sql, args);
         if(rs.next())
         {
             return rs.getObject(0);
         }
         rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    
}
