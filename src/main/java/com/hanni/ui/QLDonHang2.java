package com.hanni.ui;

import com.hanni.dao.DonHangChiTietDAO;
import com.hanni.dao.DonHangDAO;
import com.hanni.dao.SanPhamDAO;
import com.hanni.entity.DonHang;
import com.hanni.entity.DonHangChiTiet;
import com.hanni.entity.SanPham;
import com.hanni.utils.JdbcHelper;
import com.hanni.utils.MsgBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author anctWin10
 */
public class QLDonHang2 extends javax.swing.JDialog {

    DonHangDAO dao = new DonHangDAO();
    SanPhamDAO spDao = new SanPhamDAO();
    DonHangChiTietDAO dhctDao = new DonHangChiTietDAO();
    int row = 0;

    /**
     * Creates new form QLDonHang2
     */
    public QLDonHang2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fillTable();
        setLocationRelativeTo(null);

        new Timer(1000, new ActionListener() {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss a");

            public void actionPerformed(ActionEvent e) {
                lblDongHo.setText(format.format(new Date()));
                lblDongHo1.setText(format.format(new Date()));
            }
        }).start();
    }

    void fillTable() {
        DefaultTableModel model1 = (DefaultTableModel) tblDonHang.getModel();
        DefaultTableModel model2 = (DefaultTableModel) tblSanPham.getModel();
        DefaultTableModel model3 = (DefaultTableModel) tblDonhangCT.getModel();
        model2.setRowCount(0);
        model1.setRowCount(0);
        model3.setRowCount(0);

        try {
            List<DonHang> list = dao.selectAll();
            List<SanPham> list2 = spDao.selectAll();
            List<DonHangChiTiet> list3 = dhctDao.selectAll();

            // Load đơn hàng
            for (DonHang tk : list) {
                Object[] row = {
                        tk.getMaDH(),
                        tk.getMaKH(),
                        tk.getNgayLap(),
                        tk.getTongTien(),
                        tk.getTrangThai() ? "Đã thanh toán" : "Chưa thanh toán"
                };
                model1.addRow(row);
            }

            // Load sản phẩm
            for (SanPham sp : list2) {
                Object[] row2 = {
                        sp.getMaSP(),
                        sp.getTenSP(),
                        sp.getGia(),
                        sp.getSize(),
                        sp.getSoLuong(),
                        sp.getHinhAnh(),
                        sp.getThongTin(),
                        sp.getMaPL()
                };
                model2.addRow(row2);
            }

            // Load chi tiết đơn hàng
            for (DonHangChiTiet dhct : list3) {
                // Lấy thông tin sản phẩm
                SanPham sp = spDao.selectById(dhct.getMaSP());
                String tenSP = sp != null ? sp.getTenSP() : "Không tìm thấy";

                Object[] row3 = {
                        dhct.getMaDHCT(),
                        dhct.getMaSP(),
                        dhct.getMaDH(),
                        dhct.getDonGia(),
                        dhct.getSoLuong(),
                        tenSP,
                        dhct.getDonGia() * dhct.getSoLuong() // Thêm thành tiền
                };
                model3.addRow(row3);
            }

            // Cập nhật model cho bảng chi tiết đơn hàng
            tblDonhangCT.setModel(model3);

            // Đặt tên cột cho bảng chi tiết đơn hàng
            String[] columnNames = {
                    "Mã DHCT", "Mã SP", "Mã Đơn Hàng", "Đơn Giá", "Số Lượng", "Tên Sản Phẩm", "Thành Tiền"
            };
            model3.setColumnIdentifiers(columnNames);

        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void getHoten() {
        // DonHangDAO sp = new DonHangDAO();
        String tenKH = txtMaKH.getText();
        final String SELECT_tenKH = "SELECT KhachHang.HoTen\n" +
                "FROM KhachHang\n" +
                "INNER JOIN DonHang ON DonHang.MaKH = KhachHang.MAKH \n" +
                "WHERE KhachHang.MaKH = ?;";
        try {
            ResultSet rs = JdbcHelper.query(SELECT_tenKH, tenKH);
            if (rs.next()) {
                String tenKHValue = rs.getString("HoTen");
                lblHoTen.setText("" + tenKHValue);
            } else {
                lblHoTen.setText("???");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void getTenSP() {
        // DonHangDAO sp = new DonHangDAO();
        String tenSP = txtMaSP.getText();
        final String SELECT_tenKH = "SELECT tenSP FROM SanPham Where maSP = ?";
        try {
            ResultSet rs = JdbcHelper.query(SELECT_tenKH, tenSP);
            if (rs.next()) {
                String tenKHValue = rs.getString("tenSP");
                lblTenSP.setText("" + tenKHValue);
            } else {
                lblTenSP.setText("???");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void updateStatus() {
        boolean edit = this.row >= 0;
        boolean first = this.row == 0;
        boolean last = this.row == tblDonHang.getRowCount() - 1;
        // txtMaDH.setEditable(!edit);
        // khi insert thì k update, delete
        btnLuuDH.setEnabled(edit);
        btnHuyDH.setEnabled(edit);
        btnLuu.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);
    }

    void editDH() {
        try {
            String maTK = (String) tblDonHang.getValueAt(this.row, 0);
            DonHang model = dao.selectById(maTK);
            {
                setForm(model);
                updateStatus();
                getHoten();
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu1!");
        }
    }

    void editSP() {
        try {
            String maSP = (String) tblSanPham.getValueAt(this.row, 0);
            SanPham model2 = spDao.selectById(maSP);
            {
                setFormSP(model2);
                updateStatus();
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu2!");
        }
    }

    void editDHCT() {
        try {
            String maDHCT = (String) tblDonhangCT.getValueAt(this.row, 0);
            DonHangChiTiet model3 = dhctDao.selectById(maDHCT);
            {
                setFormDhct(model3);
                updateStatus();
                getTenSP();
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu3!");
        }
    }

    boolean validateInputDonHang() {
        String maKH = txtMaKH.getText().trim();
        String maDH = txtMaDH.getText().trim();

        if (maDH.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã đơn hàng!");
            txtMaDH.requestFocus();
            return false;
        }

        if (maKH.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã khách hàng!");
            txtMaKH.requestFocus();
            return false;
        }

        // Kiểm tra mã đơn hàng đã tồn tại chưa
        try {
            DonHang existingDH = dao.selectById(maDH);
            if (existingDH != null) {
                MsgBox.alert(this, "Mã đơn hàng đã tồn tại!");
                txtMaDH.requestFocus();
                return false;
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi kiểm tra mã đơn hàng: " + e.getMessage());
            return false;
        }

        return true;
    }

    boolean validateInputDHCT() {
        String maDHCT = txtMaHDCT.getText().trim();
        String maDH = txtMaDonHang.getText().trim();
        String maSP = txtMaSP.getText().trim();
        String soLuong = txtSoLuong.getText().trim();

        if (maDHCT.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã đơn hàng chi tiết!");
            txtMaHDCT.requestFocus();
            return false;
        }

        if (maDH.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã đơn hàng!");
            txtMaDonHang.requestFocus();
            return false;
        }

        if (maSP.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã sản phẩm!");
            txtMaSP.requestFocus();
            return false;
        }

        if (soLuong.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập số lượng!");
            txtSoLuong.requestFocus();
            return false;
        }

        try {
            // Kiểm tra số lượng có phải là số hợp lệ không
            int sl = Integer.parseInt(soLuong);
            if (sl <= 0) {
                MsgBox.alert(this, "Số lượng phải lớn hơn 0!");
                txtSoLuong.requestFocus();
                return false;
            }

            // Kiểm tra mã đơn hàng chi tiết đã tồn tại chưa
            DonHangChiTiet existingDHCT = dhctDao.selectById(maDHCT);
            if (existingDHCT != null) {
                MsgBox.alert(this, "Mã đơn hàng chi tiết đã tồn tại!");
                txtMaHDCT.requestFocus();
                return false;
            }

            // Kiểm tra mã đơn hàng có tồn tại không
            DonHang dh = dao.selectById(maDH);
            if (dh == null) {
                MsgBox.alert(this, "Mã đơn hàng không tồn tại!");
                txtMaDonHang.requestFocus();
                return false;
            }

            // Kiểm tra mã sản phẩm có tồn tại không
            SanPham sp = spDao.selectById(maSP);
            if (sp == null) {
                MsgBox.alert(this, "Mã sản phẩm không tồn tại!");
                txtMaSP.requestFocus();
                return false;
            }

            // Kiểm tra số lượng sản phẩm có đủ không
            if (sl > sp.getSoLuong()) {
                MsgBox.alert(this, "Số lượng sản phẩm trong kho không đủ!");
                txtSoLuong.requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Số lượng phải là số!");
            txtSoLuong.requestFocus();
            return false;
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi kiểm tra dữ liệu: " + e.getMessage());
            return false;
        }

        return true;
    }

    void time() {
        // Lấy và chạy thời gian hiện tại
        new Timer(1000, new ActionListener() {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            public void actionPerformed(ActionEvent e) {
                // lblTime.setText(format.format(new Date()));
            }
        }).start();
    }

    // get/set Don hang------------------------------------------
    DonHang getForm() {
        // lấy time
        LocalDateTime currentTime = LocalDateTime.now();
        Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

        DonHang nv = new DonHang();
        nv.setMaDH(txtMaDH.getText());
        nv.setMaKH(txtMaKH.getText());
        nv.setNgayLap(date);
        nv.setTrangThai(cbkTrangThai.isSelected());
        return nv;
    }

    void setForm(DonHang model) {
        Date currentTime = new Date();
        txtMaDH.setText(model.getMaDH());
        txtMaKH.setText(model.getMaKH());
        cbkTrangThai.setSelected(model.getTrangThai());
        lblNgayLap.setText(String.valueOf(model.getNgayLap()));
    }

    // get/set Don hang Chi Tiet------------------------------------------
    DonHangChiTiet getFormDhct() {
        DonHangChiTiet DHCT = new DonHangChiTiet();
        DHCT.setMaDHCT(txtMaHDCT.getText());
        DHCT.setMaSP(txtMaSP.getText());
        DHCT.setMaDH(txtMaDonHang.getText());
        DHCT.setDonGia(Double.valueOf(lblGia.getText()));
        DHCT.setSoLuong(Integer.parseInt(txtSoLuong.getText()));

        return DHCT;
    }

    void setFormDhct(DonHangChiTiet model) {
        txtMaHDCT.setText(model.getMaDHCT());
        txtMaSP.setText(model.getMaSP());
        lblGia.setText(String.valueOf(model.getDonGia()));
        txtMaDonHang.setText(model.getMaDH());
        txtSoLuong.setText(String.valueOf(model.getSoLuong()));
    }

    // set San Pham------------------------------------------
    void setFormSP(SanPham model) {
        txtMaSP.setText(model.getMaSP());
        lblTenSP.setText(model.getTenSP());
        lblGia.setText(String.valueOf(model.getGia()));
    }

    void getGiaSP() {
        String maSP = txtMaSP.getText();
        final String SELECT_giaSP = "select gia from SanPham where MaSP = ?;";
        try {
            ResultSet rs = JdbcHelper.query(SELECT_giaSP, maSP);
            if (rs.next()) {
                String tenSPValue = rs.getString("TenSP");
                lblGia.setText(tenSPValue);
            } else {
                lblGia.setText("!???");
            }
            rs.close();
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void clear() {
        try {
            DonHang nv = new DonHang();
            SanPham sp = new SanPham();
            DonHangChiTiet dhct = new DonHangChiTiet();
            this.setForm(nv);
            this.setFormSP(sp);
            this.setFormDhct(dhct);
            this.row = -1;
            this.updateStatus();
        } catch (Exception e) {
        }

        // soLuong = 0;
        txtMaDH.setText("");
        txtMaKH.setText("");
        txtSoLuong.setText("");
        txtMaDonHang.setText("");
        txtMaHDCT.setText("");
        txtMaSP.setText("");
        lblTenSP.setText("???");
        lblHoTen.setText("???");
        lblNgayLap.setText("???");
        lblGia.setText("???");
    }

    void insert() {
        if (validateInputDonHang() == true) {
            DonHang model = getForm();
            try {
                dao.insert(model);
                this.fillTable();
                this.clear();
                MsgBox.alert(this, "Thêm mới đơn hàng thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Thêm mới đơn hàng thất bại!");
            }
        }
    }

    void update() {
        if (validateInputDonHang() == true) {
            DonHang model = getForm();
            try {
                dao.update(model);
                this.fillTable();
                MsgBox.alert(this, "Cập nhật đơn hàng thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Cập nhật đơn hàng thất bại!");
            }
        }
    }

    void delete() {
        if (MsgBox.confirm(this, "Bạn thực sự muốn Hủy đơn hàng này?")) {

            String maTK = txtMaDH.getText();
            try {
                dao.delete(maTK);
                this.fillTable();
                this.clear();
                MsgBox.alert(this, "Hủy thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Hủy thất bại!");
            }
        }
    }

    // in, up, de Don Hang Chi Tiet----------------------------------------
    void insertDHCT() {
        if (validateInputDHCT() == true) {
            DonHangChiTiet model2 = getFormDhct();
            System.out.println(model2.getMaDH());
            try {
                dhctDao.insert(model2);
                this.fillTable();
                this.clear();
                MsgBox.alert(this, "Thêm mới thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Thêm mới thất bại!");
                System.out.println("" + e);
            }
        }
    }

    void updateDHCT() {
            DonHangChiTiet model2 = getFormDhct();
            try {
                dhctDao.update(model2);
                this.fillTable();
                MsgBox.alert(this, "Cập nhật đơn hàng chi tiết thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Cập nhật đơn hàng chi tiết thất bại!");
            }
    }

    void deleteDHCT() {
        if (MsgBox.confirm(this, "Bạn thực sự muốn Hủy đơn hàng chi tiết này1?")) {
            String maTK = txtMaHDCT.getText();
            try {
                dhctDao.delete(maTK);
                this.fillTable();
                this.clear();
                MsgBox.alert(this, "Hủy đơn hàng chi tiết thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Hủy đơn hàng chi tiết thất bại!");
            }
        }
    }

    void firstDH() {
        row = 0;
        editDH();
    }

    void prevDH() {
        if (row > 0) {
            row--;
            editDH();
        }
    }

    void nextDH() {
        if (row < tblDonHang.getRowCount() - 1) {
            row++;
            editDH();
        }
    }

    void lastDH() {
        row = tblDonHang.getRowCount() - 1;
        editDH();
    }

    // ---------------------------------------------------------------------------

    void firstDHCT() {
        int selectedIndex = tbpDHCT.getSelectedIndex();
        row = 0;
        if (selectedIndex == 0) {
            editSP();
        } else {
            editDHCT();
        }

    }

    void prevDHCT() {
        int selectedIndex = tbpDHCT.getSelectedIndex();
        if (row > 0) {
            row--;
            if (selectedIndex == 0) {
                editSP();
            } else {
                editDHCT();
            }
        }
    }

    void nextDHCT() {
        int selectedIndex = tbpDHCT.getSelectedIndex();
        if (row < tblDonHang.getRowCount() - 1) {
            row++;
            if (selectedIndex == 0) {
                editSP();
            } else {
                editDHCT();
            }
        }
    }

    void lastDHCT() {
        int selectedIndex = tbpDHCT.getSelectedIndex();
        row = tblDonHang.getRowCount() - 1;
        if (selectedIndex == 0) {
            editSP();
        } else {
            editDHCT();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDonHang = new javax.swing.JTable();
        btnFirst = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        btnMoiDH = new javax.swing.JButton();
        btnLuuDH = new javax.swing.JButton();
        btnXuatHoaDon = new javax.swing.JButton();
        btnHuyDH = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        txtMaDH = new javax.swing.JTextField();
        txtMaKH = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        lblNgayLap = new javax.swing.JLabel();
        cbkTrangThai = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        lblDongHo = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        tbpDHCT = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDonhangCT = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtMaDonHang = new javax.swing.JTextField();
        lblTenSP = new javax.swing.JLabel();
        lblGia = new javax.swing.JLabel();
        btnMoi = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        txtMaSP = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtMaHDCT = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        lblDongHo1 = new javax.swing.JLabel();
        btnNext1 = new javax.swing.JButton();
        btnLast1 = new javax.swing.JButton();
        btnFirst1 = new javax.swing.JButton();
        btnPrev1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QL Đơn Hàng");

        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setText(">>");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        tblDonHang.setBackground(new java.awt.Color(255, 250, 243));
        tblDonHang.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Mã Đơn Hàng", "Mã Khách Hàng", "Ngày Lập", "Tổng Tiền", "Trạng Thái"
                }));
        tblDonHang.setSelectionBackground(new java.awt.Color(0, 153, 255));
        tblDonHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDonHangMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblDonHang);

        btnFirst.setText("<<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 250, 243));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnMoiDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btnMoiDH.setText("Đơn hàng mới");
        btnMoiDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiDHActionPerformed(evt);
            }
        });

        btnLuuDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Accept.png"))); // NOI18N
        btnLuuDH.setText("Lưu đơn hàng");
        btnLuuDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuDHActionPerformed(evt);
            }
        });

        btnXuatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Fax.png"))); // NOI18N
        btnXuatHoaDon.setText("Xuất Hóa Đơn");
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        btnHuyDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Delete.png"))); // NOI18N
        btnHuyDH.setText("Hủy");
        btnHuyDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyDHActionPerformed(evt);
            }
        });

        jLabel1.setText("Mã Đơn Hàng: ");

        jLabel7.setText("Mã Khách hàng:");

        jLabel8.setText("Họ Tên: ");

        lblHoTen.setText("???");

        jLabel9.setText("Ngày Lập:");

        lblNgayLap.setText("???");

        cbkTrangThai.setText("Trạng Thái thanh toán");

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblDongHo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Clock.png"))); // NOI18N
        lblDongHo.setText("00:00:00");
        lblDongHo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 164,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblDongHo)));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Search.png"))); // NOI18N
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 71,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                                .addComponent(jLabel9,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 80,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel6Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(lblHoTen, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                jPanel6Layout.createSequentialGroup()
                                                                        .addComponent(txtMaKH)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jLabel11))
                                                        .addComponent(txtMaDH, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                315, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap())
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnMoiDH)
                                        .addComponent(btnLuuDH))
                                .addGap(56, 56, 56)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnHuyDH, javax.swing.GroupLayout.PREFERRED_SIZE, 143,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbkTrangThai)
                                        .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 143,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(61, Short.MAX_VALUE))
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(txtMaDH, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel6Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel7)
                                                .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(41, 41, 41)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(lblHoTen))
                                .addGap(35, 35, 35)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(lblNgayLap))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144,
                                        Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnMoiDH, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnHuyDH, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addComponent(cbkTrangThai)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLuuDH)
                                        .addComponent(btnXuatHoaDon))
                                .addGap(34, 34, 34)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 597,
                                                        Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                .createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 55,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 53,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 52,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 54,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(182, 182, 182)))));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnFirst)
                                        .addComponent(btnPrev)
                                        .addComponent(btnNext)
                                        .addComponent(btnLast))
                                .addContainerGap())
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        jTabbedPane1.addTab("Đơn Hàng", jPanel1);

        tblSanPham.setBackground(new java.awt.Color(255, 250, 243));
        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null }
                },
                new String[] {
                        "Mã SP", "Tên SP", "Giá SP", "Size", "Số Lượng", "Hình Ảnh", "ThôngTin", "Phân Loại SP"
                }));
        tblSanPham.setSelectionBackground(new java.awt.Color(0, 153, 255));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSanPhamMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblSanPham);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 588, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 447, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 441,
                                                Short.MAX_VALUE))));

        tbpDHCT.addTab("Sản Phẩm", jPanel3);

        tblDonhangCT.setBackground(new java.awt.Color(255, 250, 243));
        tblDonhangCT.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Mã DHCT", "Mã SP", "Mã Đơn Hàng", "Đơn Giá", "Số Lượng"
                }));
        tblDonhangCT.setSelectionBackground(new java.awt.Color(0, 153, 255));
        tblDonhangCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDonhangCTMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDonhangCT);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 562,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 26, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 441,
                                        Short.MAX_VALUE)));

        tbpDHCT.addTab("Thông Tin Đơn Hàng Chi Tiết", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 250, 243));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Mã Đơn Hàng:");

        jLabel3.setText("Tên SP:");

        jLabel4.setText("Mã SP:");

        jLabel5.setText("Số Lượng:");

        jLabel6.setText("Đơn Giá:");

        lblTenSP.setText("???");

        lblGia.setText("???");

        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Accept.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Edit.png"))); // NOI18N
        btnCapNhat.setText("Cập Nhật");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Mã Đơn hàng CT:");

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblDongHo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Clock.png"))); // NOI18N
        lblDongHo1.setText("00:00:00");
        lblDongHo1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(lblDongHo1, javax.swing.GroupLayout.PREFERRED_SIZE, 164,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblDongHo1)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(jPanel5Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(jLabel4,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)
                                                                                .addComponent(jLabel5)
                                                                                .addComponent(jLabel3,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        63, Short.MAX_VALUE))
                                                                        .addComponent(jLabel6,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                53,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                                .addGap(49, 49, 49)
                                                                                .addComponent(txtSoLuong,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        120,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                                .addGap(51, 51, 51)
                                                                                .addGroup(jPanel5Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(lblTenSP,
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                        .addComponent(txtMaSP)
                                                                                        .addComponent(lblGia,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)))))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel2)
                                                                        .addComponent(jLabel10))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtMaDonHang)
                                                                        .addComponent(txtMaHDCT))))
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout
                                                .createSequentialGroup()
                                                .addGap(73, 73, 73)
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(btnMoi, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55,
                                                        Short.MAX_VALUE)
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(btnCapNhat, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(70, 70, 70))))
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtMaHDCT, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtMaDonHang, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(lblTenSP))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(lblGia))
                                .addGap(35, 35, 35)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90,
                                        Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnMoi)
                                        .addComponent(btnCapNhat))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnXoa)
                                        .addComponent(btnLuu))
                                .addGap(25, 25, 25)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

        btnNext1.setText(">");
        btnNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNext1ActionPerformed(evt);
            }
        });

        btnLast1.setText(">>");
        btnLast1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLast1ActionPerformed(evt);
            }
        });

        btnFirst1.setText("<<");
        btnFirst1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirst1ActionPerformed(evt);
            }
        });

        btnPrev1.setText("<");
        btnPrev1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrev1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tbpDHCT, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 588,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout
                                                .createSequentialGroup()
                                                .addComponent(btnFirst1, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnPrev1, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnLast1, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(170, 170, 170)))));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tbpDHCT, javax.swing.GroupLayout.PREFERRED_SIZE, 478,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnNext1)
                                        .addComponent(btnLast1)
                                        .addComponent(btnFirst1)
                                        .addComponent(btnPrev1))
                                .addContainerGap()));

        jTabbedPane1.addTab("Đơn Hàng Chi Tiết", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        prevDH();
    }// GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        nextDH();
    }// GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        lastDH();
    }// GEN-LAST:event_btnLastActionPerformed

    private void tblDonHangMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblDonHangMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblDonHang.rowAtPoint(evt.getPoint());
            editDH();
        }
    }// GEN-LAST:event_tblDonHangMousePressed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        firstDH();
    }// GEN-LAST:event_btnFirstActionPerformed

    private void tblDonhangCTMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblDonhangCTMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblDonhangCT.rowAtPoint(evt.getPoint());
            editDHCT();
        }
        // txtMaHDCT.setText("");
        // txtMaDH.setText("");
        // txtSoLuong.setText("0");
    }// GEN-LAST:event_tblDonhangCTMousePressed

    private void tblSanPhamMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblSanPhamMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblSanPham.rowAtPoint(evt.getPoint());
            editSP();
        }
        // boolean edit = this.row >= 0;
        // btnLuu.setEnabled(!edit);
        // btnMoi.setEnabled(edit);
        // btnCapNhat.setEnabled(edit);
        // btnXoa.setEnabled(edit);
    }// GEN-LAST:event_tblSanPhamMousePressed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clear();
        boolean edit = this.row >= 0;
        btnMoi.setEnabled(edit);
        btnLuu.setEnabled(!edit);
    }// GEN-LAST:event_btnMoiActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        insertDHCT();
    }// GEN-LAST:event_btnLuuActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCapNhatActionPerformed
        // TODO add your handling code here:
        updateDHCT();
    }// GEN-LAST:event_btnCapNhatActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        deleteDHCT();
    }// GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiDHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnMoiDHActionPerformed
        // TODO add your handling code here:
        clear();
    }// GEN-LAST:event_btnMoiDHActionPerformed

    private void btnLuuDHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuDHActionPerformed
        // TODO add your handling code here:
        insert();
    }// GEN-LAST:event_btnLuuDHActionPerformed

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (!cbkTrangThai.isSelected()) {
                MsgBox.alert(this, "Đơn Hàng Chưa Được Thanh Toán!");
                return;
            }

            String maDH = txtMaDH.getText().trim();
            if (maDH.isEmpty()) {
                MsgBox.alert(this, "Vui lòng chọn đơn hàng cần xuất hóa đơn!");
                return;
            }

            // Kiểm tra đơn hàng có tồn tại không
            DonHang dh = dao.selectById(maDH);
            if (dh == null) {
                MsgBox.alert(this, "Không tìm thấy thông tin đơn hàng!");
                return;
            }

            // Tính tổng số lượng và tổng tiền từ chi tiết đơn hàng
            int tongSoLuong = 0;
            double tongTien = 0;
            List<DonHangChiTiet> list = dhctDao.selectAll();
            boolean coChiTiet = false;

            for (DonHangChiTiet dhct : list) {
                if (dhct.getMaDH().equals(maDH)) {
                    coChiTiet = true;
                    tongSoLuong += dhct.getSoLuong();
                    tongTien += dhct.getDonGia() * dhct.getSoLuong();
                }
            }

            if (!coChiTiet) {
                MsgBox.alert(this, "Đơn hàng chưa có chi tiết sản phẩm!");
                return;
            }

            // Hiển thị hóa đơn
            StringBuilder hoaDon = new StringBuilder();
            hoaDon.append("-------------< Hóa Đơn >--------------\n");
            hoaDon.append("Mã Đơn Hàng: ").append(maDH).append("\n");
            hoaDon.append("Họ Tên: ").append(lblHoTen.getText()).append("\n");
            hoaDon.append("Ngày Lập: ").append(lblNgayLap.getText()).append("\n");
            hoaDon.append("Tổng Số Lượng: ").append(tongSoLuong).append("\n");
            hoaDon.append("Tổng Tiền: ").append(String.format("%,.0f", tongTien)).append(" VNĐ\n");
            hoaDon.append("------------------------------------");

            MsgBox.alert(this, hoaDon.toString());

        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi xuất hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void btnHuyDHActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHuyDHActionPerformed
        // TODO add your handling code here:
        delete();
    }// GEN-LAST:event_btnHuyDHActionPerformed

    private void btnNext1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNext1ActionPerformed
        // TODO add your handling code here:
        nextDHCT();
    }// GEN-LAST:event_btnNext1ActionPerformed

    private void btnFirst1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnFirst1ActionPerformed
        // TODO add your handling code here:
        firstDHCT();
    }// GEN-LAST:event_btnFirst1ActionPerformed

    private void btnPrev1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPrev1ActionPerformed
        // TODO add your handling code here:
        prevDHCT();
    }// GEN-LAST:event_btnPrev1ActionPerformed

    private void btnLast1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLast1ActionPerformed
        // TODO add your handling code here:
        lastDHCT();
    }// GEN-LAST:event_btnLast1ActionPerformed

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel11MouseClicked
        // TODO add your handling code here:
        getHoten();
    }// GEN-LAST:event_jLabel11MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QLDonHang2.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLDonHang2.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLDonHang2.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLDonHang2.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLDonHang2 dialog = new QLDonHang2(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnFirst1;
    private javax.swing.JButton btnHuyDH;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnLast1;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnLuuDH;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnMoiDH;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNext1;
    private javax.swing.JButton btnPrev;
    public javax.swing.JButton btnPrev1;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JCheckBox cbkTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblDongHo;
    private javax.swing.JLabel lblDongHo1;
    private javax.swing.JLabel lblGia;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblNgayLap;
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JTable tblDonHang;
    private javax.swing.JTable tblDonhangCT;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTabbedPane tbpDHCT;
    private javax.swing.JTextField txtMaDH;
    private javax.swing.JTextField txtMaDonHang;
    private javax.swing.JTextField txtMaHDCT;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables
}
