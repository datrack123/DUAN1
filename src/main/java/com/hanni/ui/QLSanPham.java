/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hanni.ui;

import com.hanni.dao.SanPhamDAO;
import com.hanni.dao.PhanLoaiSPDAO;
import com.hanni.entity.PhanLoaiSP;
import com.hanni.entity.SanPham;
import com.hanni.utils.JdbcHelper;
import com.hanni.utils.MsgBox;
import com.hanni.utils.XImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author anctWin10
 */
public class QLSanPham extends javax.swing.JDialog {
    JFileChooser fileChooser = new JFileChooser(
            "C:\\Users\\anctWin10\\Documents\\NetBeansProjects\\DuAn1\\src\\main\\resources\\Icon");
    SanPhamDAO dao = new SanPhamDAO();
    PhanLoaiSPDAO daoPL = new PhanLoaiSPDAO();
    int row = 0;

    /**
     * Creates new form QLSanPham
     */
    public QLSanPham(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    void init() {
        setLocationRelativeTo(null);
        fillComboBoxPhanLoaiSP();
        fillTable();

        
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0);

        try {
            List<SanPham> list = dao.selectAll();
            for (SanPham tk : list) {
                Object[] row = {
                        tk.getMaSP(),
                        tk.getTenSP(),
                        tk.getGia(),
                        tk.getSize(),
                        tk.getSoLuong(),
                        tk.getHinhAnh(),
                        tk.getThongTin(),
                        tk.getMaPL()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu tu SQL len Table!");
        }
    }

    void fillComboBoxPhanLoaiSP() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTenPL.getModel();
        model.removeAllElements();
        
        try {
            List<PhanLoaiSP> list = new PhanLoaiSPDAO().selectAll();
            for (PhanLoaiSP pl : list) {
                model.addElement(pl.getTenPL());
            }
            cboTenPL.setSelectedIndex(0);
            loadTenPL();
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu phân loại sản phẩm!");
        }
    }

    void edit() {
        try {
            String maTK = (String) tblSanPham.getValueAt(this.row, 0);
            SanPham model = dao.selectById(maTK);
            if (model != null) {
                setForm(model);
                updateStatus();
                setAnh();
                // tabs.setSelectedIndex(0);

            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu edit!");
        }
    }

    boolean MaSPIsExist() {
        try {
            List<SanPham> list = dao.selectAll();
            for (SanPham tk : list) {
                if (txtMaSP.getText().equalsIgnoreCase(tk.getMaSP())) {

                    return true;
                }
            }

        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu ma san pham ton tai!");
        }
        return false;
    }

    boolean validateInput() {
        String maSP = txtMaSP.getText().trim();
        String tenSP = txtTenSP.getText().trim();
        String giaSP = txtGiaSP.getText().trim();
        String size = txtSize.getText().trim();
        String soLuong = txtSoLuong.getText().trim();
        String phanLoaiSP = cboTenPL.getSelectedItem().toString();

        // Kiểm tra mã sản phẩm
        if (maSP.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã sản phẩm!");
            txtMaSP.requestFocus();
            return false;
        }
        if (maSP.length() > 10) {
            MsgBox.alert(this, "Mã sản phẩm không được quá 10 ký tự!");
            txtMaSP.requestFocus();
            return false;
        }
        if (MaSPIsExist()) {
            MsgBox.alert(this, "Khong duoc trung ma san pham!");
            txtMaSP.requestFocus();
            return false;
        }

        // Kiểm tra tên sản phẩm
        if (tenSP.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập tên sản phẩm!");
            txtTenSP.requestFocus();
            return false;
        }
        if (tenSP.length() > 100) {
            MsgBox.alert(this, "Tên sản phẩm không được quá 100 ký tự!");
            txtTenSP.requestFocus();
            return false;
        }

        // Kiểm tra giá sản phẩm
        if (giaSP.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập giá sản phẩm!");
            txtGiaSP.requestFocus();
            return false;
        }
        try {
            double gia = Double.parseDouble(giaSP);
            if (gia <= 0) {
                MsgBox.alert(this, "Giá sản phẩm phải lớn hơn 0!");
                txtGiaSP.requestFocus();
                return false;
            }
            if (gia > 1000000000) {
                MsgBox.alert(this, "Giá sản phẩm không được quá 1 tỷ!");
                txtGiaSP.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Giá sản phẩm phải là số!");
            txtGiaSP.requestFocus();
            return false;
        }

        // Kiểm tra size
        if (size.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập size sản phẩm!");
            txtSize.requestFocus();
            return false;
        }
        try {
            double sizeValue = Double.parseDouble(size);
            if (sizeValue <= 0) {
                MsgBox.alert(this, "Size sản phẩm phải lớn hơn 0!");
                txtSize.requestFocus();
                return false;
            }
            if (sizeValue > 100) {
                MsgBox.alert(this, "Size sản phẩm không được quá 100!");
                txtSize.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Size sản phẩm phải là số!");
            txtSize.requestFocus();
            return false;
        }

        // Kiểm tra số lượng
        if (soLuong.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập số lượng sản phẩm!");
            txtSoLuong.requestFocus();
            return false;
        }
        try {
            int sl = Integer.parseInt(soLuong);
            if (sl <= 0) {
                MsgBox.alert(this, "Số lượng sản phẩm phải lớn hơn 0!");
                txtSoLuong.requestFocus();
                return false;
            }
            if (sl > 10000) {
                MsgBox.alert(this, "Số lượng sản phẩm không được quá 10000!");
                txtSoLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Số lượng sản phẩm phải là số nguyên!");
            txtSoLuong.requestFocus();
            return false;
        }

        // Kiểm tra phân loại sản phẩm
        if (phanLoaiSP.isEmpty()) {
            MsgBox.alert(this, "Vui lòng chọn phân loại sản phẩm!");
            cboTenPL.requestFocus();
            return false;
        }
        if (phanLoaiSP.length() > 20) {
            MsgBox.alert(this, "Phân loại sản phẩm không được quá 20 ký tự!");
            cboTenPL.requestFocus();
            return false;
        }

        return true;
    }

    SanPham getForm() {
        SanPham nv = new SanPham();
        nv.setMaSP(txtMaSP.getText());
        nv.setTenSP(txtTenSP.getText());
        nv.setGia(Double.valueOf(txtGiaSP.getText()));
        nv.setSize(Double.valueOf(txtSize.getText()));
        nv.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
        nv.setHinhAnh(lblHinhAnh.getToolTipText());
        nv.setThongTin(txtMoTa.getText());
        nv.setMaPL(txtMaPL.getText());
        return nv;
    }

    void setForm(SanPham model) {
        txtMaSP.setText(model.getMaSP());
        txtTenSP.setText(model.getTenSP());
        txtGiaSP.setText(String.valueOf(model.getGia()));
        txtSize.setText(String.valueOf(model.getSize()));
        txtSoLuong.setText(String.valueOf(model.getSoLuong()));
        //cboTenPL.setSelectedItem(model.getMaPL());
        txtMoTa.setText(model.getThongTin());
        lblHinhAnh.setText(model.getHinhAnh());
    }

    void updateStatus() {
        boolean edit = this.row >= 0;
        boolean first = this.row == 0;
        boolean last = this.row == tblSanPham.getRowCount() - 1;
        txtMaSP.setEditable(!edit);
        // khi insert thì k update, delete
        btnLuu.setEnabled(!edit);
        btnCapNhat.setEnabled(edit);
        btnXoa.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);
    }

    void clear() {
        SanPham nv = new SanPham();
        this.setForm(nv);
        this.row = -1;
        this.updateStatus();
    }

    void insert() {
        if (validateInput()) {
            SanPham model = getForm();
            try {
                dao.insert(model);
                this.fillTable();
                this.clear();
                MsgBox.alert(this, "Thêm mới thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Thêm mới thất bại!");
            }
        } else
            MsgBox.alert(this, "Vui lòng điền đầy đủ thông tin!");

    }

    void update() {

        if (validateInput() == true) {
            SanPham model = getForm();
            try {
                dao.update(model);
                this.fillTable();
                MsgBox.alert(this, "Cập nhật thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Cập nhật thất bại!");
            }
        } else
            MsgBox.alert(this, "Vui lòng điền đầy đủ thông tin!");

    }

    void delete() {
        if (MsgBox.confirm(this, "Bạn thực sự muốn xóa sản Phẩm này?")) {
            String maTK = txtMaSP.getText();
            try {
                dao.delete(maTK);
                this.fillTable();
                this.clear();
                MsgBox.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Xóa thất bại!");
            }
        }
    }

    void first() {
        row = 0;
        edit();
    }

    void prev() {
        if (row > 0) {
            row--;
            edit();
        }
    }

    void next() {
        if (row < tblSanPham.getRowCount() - 1) {
            row++;
            edit();
        }
    }

    void last() {
        row = tblSanPham.getRowCount() - 1;
        edit();
    }

    void chonAnh() {
        // lblHinhAnh.setIcon(null);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            ImageIcon icon = XImage.read(file.getName());
            // lblHinhAnh.setText(null);
            lblHinhAnh.setIcon(icon);
            lblHinhAnh.setToolTipText(file.getName());
        }
    }

    void setAnh() {
        try {
            ImageIcon icon = XImage.read(lblHinhAnh.getText());
            lblHinhAnh.setIcon(icon);
            lblHinhAnh.setText("");
        } catch (Exception e) {
            MsgBox.alert(this, "Hình ảnh bị lỗi hoặc không có hình ảnh!");
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnLuu = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtGiaSP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lblHinhAnh = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSize = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        btnMoi = new javax.swing.JButton();
        cboTenPL = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtMaPL = new javax.swing.JTextField();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản Lý Sản Phẩm");

        tblSanPham.setBackground(new java.awt.Color(255, 250, 243));
        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã SP", "Tên SP", "Giá SP", "Size", "Số Lượng", "Hình Ảnh", "ThôngTin", "Phân Loại SP"
            }
        ));
        tblSanPham.setSelectionBackground(new java.awt.Color(0, 102, 255));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSanPhamMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPham);

        jPanel1.setBackground(new java.awt.Color(255, 250, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnLuu.setBackground(new java.awt.Color(255, 255, 204));
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Accept.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnCapNhat.setBackground(new java.awt.Color(255, 255, 204));
        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Edit.png"))); // NOI18N
        btnCapNhat.setText("Cập Nhật");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 255, 204));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        jLabel2.setText("Mã Sản Phẩm:");

        txtMaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaSPActionPerformed(evt);
            }
        });

        jLabel3.setText("Tên SP:");

        txtTenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSPActionPerformed(evt);
            }
        });

        jLabel4.setText("Giá SP:");

        txtGiaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaSPActionPerformed(evt);
            }
        });

        jLabel5.setText("Phân loại SP");

        lblHinhAnh.setText("Hình Ảnh:");
        lblHinhAnh.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblHinhAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblHinhAnhMousePressed(evt);
            }
        });

        jLabel7.setText("Size:");

        jLabel8.setText("Số Lượng:");

        jLabel9.setText("Mô tả:");

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane2.setViewportView(txtMoTa);

        btnMoi.setBackground(new java.awt.Color(255, 255, 204));
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        cboTenPL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTenPLActionPerformed(evt);
            }
        });

        jLabel6.setText("Mã phân loại");

        txtMaPL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMaPLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnMoi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLuu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCapNhat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoa)
                .addGap(18, 18, 18))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGiaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboTenPL, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtMaPL))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(31, 31, 31)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenSP))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtGiaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cboTenPL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtMaPL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapNhat)
                    .addComponent(btnXoa)
                    .addComponent(btnLuu)
                    .addComponent(btnMoi))
                .addGap(14, 14, 14))
        );

        btnFirst.setBackground(new java.awt.Color(255, 255, 204));
        btnFirst.setText("<<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setBackground(new java.awt.Color(255, 255, 204));
        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setBackground(new java.awt.Color(255, 255, 204));
        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setBackground(new java.awt.Color(255, 255, 204));
        btnLast.setText(">>");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(140, 140, 140))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnFirst, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnPrev)
                                .addComponent(btnNext)
                                .addComponent(btnLast))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    void loadTenPL(){
        
        try {
            List<PhanLoaiSP> list = daoPL.selectAll();
            for (PhanLoaiSP tk : list) {
                if(tk.getTenPL().equalsIgnoreCase(cboTenPL.getSelectedItem().toString())){
                    txtMaPL.setText(tk.getMaPL());
                    break;
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu load ten pl!");
        }
    }
    private void cboTenPLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTenPLActionPerformed
       loadTenPL();
    }//GEN-LAST:event_cboTenPLActionPerformed

    private void txtMaPLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMaPLMouseClicked
        // TODO add your handling code here:
        txtMaPL.setEnabled(false);
    }//GEN-LAST:event_txtMaPLMouseClicked

    private void cboPhanLoaiSPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboPhanLoaiSPActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cboPhanLoaiSPActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:

        insert();
    }// GEN-LAST:event_btnLuuActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCapNhatActionPerformed
        // TODO add your handling code here:
        update();
    }// GEN-LAST:event_btnCapNhatActionPerformed

    private void txtMaSPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtMaSPActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtMaSPActionPerformed

    private void txtTenSPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTenSPActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTenSPActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        prev();
    }// GEN-LAST:event_btnPrevActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clear();
        boolean edit = this.row >= 0;
        btnMoi.setEnabled(edit);
        btnLuu.setEnabled(!edit);
    }// GEN-LAST:event_btnMoiActionPerformed

    private void tblSanPhamMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblSanPhamMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblSanPham.rowAtPoint(evt.getPoint());
            edit();
        }
        boolean edit = this.row >= 0;
        btnLuu.setEnabled(!edit);
        btnMoi.setEnabled(edit);
        btnCapNhat.setEnabled(edit);
        btnXoa.setEnabled(edit);
    }// GEN-LAST:event_tblSanPhamMousePressed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        first();
    }// GEN-LAST:event_btnFirstActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        next();
    }// GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        last();
    }// GEN-LAST:event_btnLastActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }// GEN-LAST:event_btnXoaActionPerformed

    private void lblHinhAnhMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_lblHinhAnhMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            chonAnh();
        }
    }// GEN-LAST:event_lblHinhAnhMousePressed

    private void txtGiaSPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtGiaSPActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtGiaSPActionPerformed

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
            java.util.logging.Logger.getLogger(QLSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLSanPham dialog = new QLSanPham(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboTenPL;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblHinhAnh;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txtGiaSP;
    private javax.swing.JTextField txtMaPL;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSize;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenSP;
    // End of variables declaration//GEN-END:variables
}
