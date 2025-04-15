/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hanni.ui;

import com.hanni.dao.DonHangChiTietDAO;
import com.hanni.dao.SanPhamDAO;
import com.hanni.entity.DonHangChiTiet;
import com.hanni.entity.SanPham;
import com.hanni.utils.JdbcHelper;
import com.hanni.utils.MsgBox;
import jasima.shopSim.prioRules.setup.MASP;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author anctWin10
 */
public class QLDonHangChiTiet extends javax.swing.JDialog {
    DonHangChiTietDAO dao = new DonHangChiTietDAO();
    SanPhamDAO daoSP = new SanPhamDAO();
    int row = 0;
    /**
     * Creates new form QLHoaDon
     */
    
    void getTenSP() {
        String maSP ;
        if(cboMaSP.getSelectedItem().equals(""))
            maSP="";
        else
            maSP=cboMaSP.getSelectedItem().toString();
        final String SELECT_tenSP = "SELECT SanPham.tenSP\n" +
                                    "FROM SanPham\n" +
                                    "INNER JOIN DonHangChiTiet ON SanPham.maSP = DonHangChiTiet.maSP\n" +
                                    "WHERE DonHangChiTiet.maSP = ?;";
        try {
            ResultSet rs = JdbcHelper.query(SELECT_tenSP, maSP);
            if (rs.next()) {
                String tenSPValue = rs.getString("TenSP");
                txtTenSP.setText(tenSPValue);
            } else {
                txtTenSP.setText("???");
            }
            rs.close();
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
//đổ tất cả mã SP từ SQL lên cbo
void fillComboBoxMaSP() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboMaSP.getModel();
        model.removeAllElements();
        
        try {
            List<SanPham> list = new SanPhamDAO().selectAll();
            for (SanPham pl : list) {
                model.addElement(pl.getMaSP());
            }
            cboMaSP.setSelectedIndex(0);
           // loadTenPL();
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu phân loại sản phẩm!");
        }
    }
//
void loadSPFromCbo(){
        
        try {
            List<SanPham> listSP = daoSP.selectAll();
            for (SanPham tk : listSP) {
                if(tk.getMaSP().equalsIgnoreCase(cboMaSP.getSelectedItem().toString())){
                    txtTenSP.setText(tk.getTenSP());
                    txtDonGia.setText(tk.getGia().toString());
                    break;
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu load San pham tu cbo!");
        }
    }

    
    public QLDonHangChiTiet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
      
        init();
    }
    
    void init() {
        setLocationRelativeTo(null);
        
        
         fillComboBoxMaSP();
         loadSPFromCbo();
         fillTable();
        //setIconImage(XImage.getAppIcon());
    }
    
    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblDonhangCT.getModel();
        model.setRowCount(0);
        
        try {
            List<DonHangChiTiet> list = dao.selectAll();
            
            for(DonHangChiTiet tk : list) {
                Object[] row = {
                    tk.getMaDHCT(),
                    tk.getMaSP(),
                    tk.getMaDH(),   
                    tk.getDonGia(),
                    tk.getSoLuong(),
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
    
    
    
    void edit() {
        try {
            String maTK = (String)tblDonhangCT.getValueAt(this.row, 0);
            DonHangChiTiet model = dao.selectById(maTK);
            if(model != null)
            {
                setForm(model);
                updateStatus();
                getTenSP();
                //tabs.setSelectedIndex(0);
               
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
    
    boolean validateInput() {
        String maTK = txtMaHDCT.getText().trim();
        String MaSP = cboMaSP.getSelectedObjects().toString();
        String TenSP = txtTenSP.getText().trim();
        String donGia = txtDonGia.getText().trim();
        String SoLuong = txtSoLuong.getText().trim();
        //String email = lblMaDH.getText().trim();

        if (maTK.isEmpty() || MaSP.isEmpty() || TenSP.isEmpty() || SoLuong.isEmpty() || donGia.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        } 
        return true;
    }
    
    DonHangChiTiet getForm(){
        DonHangChiTiet DHCT = new DonHangChiTiet();
        DHCT.setMaDHCT(txtMaHDCT.getText());
        DHCT.setMaSP(cboMaSP.getSelectedItem().toString());
        
        DHCT.setMaDH(lblMaDH.getText());
        DHCT.setDonGia(Double.valueOf(txtDonGia.getText()));
        DHCT.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
        return DHCT;
    }
    
    void setForm(DonHangChiTiet model) {
        txtMaHDCT.setText(model.getMaDHCT());
        cboMaSP.setSelectedItem(model.getMaSP());
       // txtTenSP.setText(model.getMaSP());
        txtDonGia.setText(String.valueOf(model.getDonGia()));        
        lblMaDH.setText(model.getMaDH());
        txtSoLuong.setText(String.valueOf(model.getSoLuong()));
    }
    
    void updateStatus() {
        boolean edit = this.row >= 0;
        boolean first = this.row == 0;
        boolean last = this.row == tblDonhangCT.getRowCount() - 1;
        txtMaHDCT.setEditable(!edit);
        //khi insert thì k update, delete
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);
    
        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);
    }
    
    void clear() {
        DonHangChiTiet nv = new DonHangChiTiet();
        this.setForm(nv);
        this.row = -1;
        this.updateStatus();
    }
    
    void insert() {    
        if (validateInput()) {
            DonHangChiTiet model = getForm();       
            try {
                dao.insert(model);
                this.fillTable();
                this.fillComboBoxMaSP();
               
                MsgBox.alert(this, "Cập nhật thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Cập nhật thất bại!");
            }             
        }else MsgBox.alert(this, "Vui lòng điền đầy đủ thông tin!");
    }
    void update() {
        if (validateInput() == true) {
            DonHangChiTiet model = getForm();       
            try {
                dao.update(model);
                this.fillTable();
                this.fillComboBoxMaSP();
                MsgBox.alert(this, "Cập nhật thành công!");
                clear();
            } catch (Exception e) {
                MsgBox.alert(this, "Cập nhật thất bại!");
            }
        }else MsgBox.alert(this, "Vui lòng điền đầy đủ thông tin!");
    }
    void delete() {
        if(MsgBox.confirm(this, "Bạn thực sự muốn xóa chi tiết đơn hàng này?")) {
            String maDHCT =txtMaHDCT.getText();
            try {
                dao.delete(maDHCT);
                this.fillTable();
                this.fillComboBoxMaSP();
                this.clear();
                MsgBox.alert(this, "Xóa thành công chi tiết đơn hàng!");
                clear();
            } catch (Exception e) {
                MsgBox.alert(this, "Xóa thất bại chi tiết đơn hàng!");
            }
        }
    }
    
    void first() {
        row = 0;
        edit();
    }
    void prev() {
        if(row > 0) {
            row--;
            edit();
        }
    }
    void next() {
        if(row < tblDonhangCT.getRowCount() - 1) {
            row++;
            edit();
        }
    }
    void last() {
        row = tblDonhangCT.getRowCount() - 1;
        edit();
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        lblMaDH = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMaHDCT = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        cboMaSP = new javax.swing.JComboBox<>();
        btnMoi = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDonhangCT = new javax.swing.JTable();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HANNI STORE");

        jPanel1.setBackground(new java.awt.Color(255, 250, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnSua.setBackground(new java.awt.Color(255, 255, 204));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Notes.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Mã Đơn Hàng:");

        jLabel3.setText("Tên SP:");

        txtTenSP.setEnabled(false);
        txtTenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSPActionPerformed(evt);
            }
        });

        jLabel4.setText("Mã SP:");

        jLabel5.setText("Số Lượng mua:");

        lblMaDH.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        lblMaDH.setText("...");

        jLabel10.setText("Mã Đơn hàng CT:");

        jLabel6.setText("Đơn Giá:");

        txtDonGia.setEnabled(false);

        cboMaSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SP001\t", "SP002\t", "SP003", "SP100" }));
        cboMaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMaSPActionPerformed(evt);
            }
        });

        btnMoi.setBackground(new java.awt.Color(255, 255, 204));
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnLuu.setBackground(new java.awt.Color(255, 255, 204));
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Accept.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaHDCT)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblMaDH, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMoi))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTenSP)
                                    .addComponent(txtDonGia)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(cboMaSP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLuu)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSua)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnXoa)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblMaDH))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtMaHDCT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(cboMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXoa)
                            .addComponent(btnSua)
                            .addComponent(btnMoi)
                            .addComponent(btnLuu))
                        .addGap(43, 43, 43))))
        );

        tblDonhangCT.setBackground(new java.awt.Color(255, 250, 243));
        tblDonhangCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã DHCT", "Mã SP", "Mã Đơn Hàng", "Đơn Giá", "Số Lượng"
            }
        ));
        tblDonhangCT.setSelectionBackground(new java.awt.Color(0, 102, 255));
        tblDonhangCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDonhangCTMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDonhangCT);

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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(134, 134, 134))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFirst)
                            .addComponent(btnPrev)
                            .addComponent(btnNext)
                            .addComponent(btnLast))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        prev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void txtTenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenSPActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void tblDonhangCTMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDonhangCTMousePressed
        // TODO add your handling code here:
        if(evt.getClickCount() == 2) {
            this.row = tblDonhangCT.rowAtPoint(evt.getPoint());
            edit(); 
            
        }   
    }//GEN-LAST:event_tblDonhangCTMousePressed

    private void cboMaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMaSPActionPerformed
       loadSPFromCbo();
    }//GEN-LAST:event_cboMaSPActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clear();
        boolean edit = this.row >= 0;
        btnMoi.setEnabled(edit);
        btnLuu.setEnabled(!edit);
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnLuuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QLDonHangChiTiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLDonHangChiTiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLDonHangChiTiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLDonHangChiTiet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLDonHangChiTiet dialog = new QLDonHangChiTiet(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboMaSP;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMaDH;
    private javax.swing.JTable tblDonhangCT;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtMaHDCT;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenSP;
    // End of variables declaration//GEN-END:variables
}
