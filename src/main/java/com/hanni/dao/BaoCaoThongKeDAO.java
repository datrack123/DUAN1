
package com.hanni.dao;

import com.hanni.entity.BaoCaoThongKe;
import com.hanni.utils.JdbcHelper;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Hoangdai
 */
public class BaoCaoThongKeDAO extends HanniDAO<BaoCaoThongKe, String> {
    
    final String INSERT_SQL = "INSERT INTO THONGKEDONHANG (MADH, NGAYLAP, TENKHACHHANG, TONGTIEN, TRANGTHAI, TENSANPHAM, DONGIA, SOLUONG) VALUES(?,?,?,?,?,?,?,?)";
    final String Date_SQL = "EXEC HienThiDoanhThu @Ngay = '2023-01-15';";
    final String Month_SQL = "EXEC HienThiDoanhThu @Thang = ?;";
    final String SELECT_ALL_SQL = "EXEC ThongKeDonHang";
    final String Year_SQL = "EXEC HienThiDoanhThu @Nam = 2023;";
    //final String SalesReport_SQL = "EXEC ProductSalesReport @StartDate = '2023-01-01', @EndDate = '2023-12-31';";
    final String OrderHistory_SQL = "EXEC CustomerOrderHistory @CustomerID = 'kh002';";
    
    // Lấy ngày, tháng, năm hiện tại
    LocalDate now = LocalDate.now();
    // Lấy ngày hôm sau
    LocalDate tomorrow = now.plusDays(1);
    // Thay thế giá trị của biến @StartDate và @EndDate
    private final String SalesReport_Day_SQL = "EXEC ProductSalesReport   '" + now + "', '" + tomorrow + "';";
    
    private static final String SalesReport_Month_SQL;
    
    Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
    // Thay thế giá trị của biến @StartDate và @EndDate
    final String SalesReport_Year_SQL = "EXEC ProductSalesReport  '" + year + "-01-01', '" + year + "-12-31';";

    static {
        // Khối static để khởi tạo giá trị của biến
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();

        // Câu SQL cho báo cáo sản phẩm
        SalesReport_Month_SQL = String.format("EXEC ProductSalesReport '%s', '%s';",
                currentDate.withDayOfMonth(1).format(formatter),
                currentDate.withDayOfMonth(currentDate.lengthOfMonth()).format(formatter));
    }
    
    @Override
    public void insert(BaoCaoThongKe entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaDH(),entity.getNgayLap(), entity.gettenKhachHang(),
                entity.getTongTien(),entity.getTrangThai(),entity.getTenSanPham(), entity.getDonGia(), entity.getSoLuong());
    }

    @Override
    public void update(BaoCaoThongKe entity) {
        JdbcHelper.update(Date_SQL,entity.getNgay(),entity.getThang(),entity.getNam());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(Month_SQL, id);
    }

    @Override
    public List<BaoCaoThongKe> selectAll() {
        return selectBySql(SELECT_ALL_SQL); 
    }
    
    public List<BaoCaoThongKe> selectAllDoanhThuThang() {
        return selectDoanhThu(SalesReport_Month_SQL); 
    }
    
    public List<BaoCaoThongKe> selectAllDoanhThuNam() {
        return selectDoanhThu(SalesReport_Year_SQL); 
    }
    
    public List<BaoCaoThongKe> selectAllDoanhThuNgay() {
        return selectDoanhThu(SalesReport_Day_SQL); 
    }
    
    public List<BaoCaoThongKe> selectDoanhThu(String sql, Object... args) {
        List<BaoCaoThongKe> list = new ArrayList<>();
            try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
               BaoCaoThongKe entity = new BaoCaoThongKe();
                entity.setTenSanPham(rs.getString("TenSanPham"));
                entity.setDoanhThu(rs.getDouble("DoanhThu"));
                entity.setSoLuong(rs.getInt("SoLuongBan"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
            return list;
    }

    @Override
    public BaoCaoThongKe selectById(String id) {
        List<BaoCaoThongKe> list = selectBySql(Year_SQL, id);
            if(list.isEmpty()) {
                return null;
            }
            return list.get(0); 
    }

    @Override
    public List<BaoCaoThongKe> selectBySql(String sql, Object... args) {
        List<BaoCaoThongKe> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()) {
               BaoCaoThongKe entity = new BaoCaoThongKe();
                entity.setMaDH(rs.getString("MaDH"));
                entity.setNgayLap(rs.getDate("NgayLap"));
                entity.settenKhachHang(rs.getString("tenKhachHang"));
                entity.setTongTien(rs.getDouble("TongTien"));
                entity.setTrangThai(rs.getBoolean("TrangThai"));
                entity.setTenSanPham(rs.getString("TenSanPham"));
                entity.setDonGia(rs.getDouble("DonGia"));
                entity.setSoLuong(rs.getInt("SoLuong"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
