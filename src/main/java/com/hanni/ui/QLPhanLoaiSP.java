/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.hanni.ui;

import com.hanni.dao.PhanLoaiSPDAO;
import com.hanni.entity.PhanLoaiSP;
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
public class QLPhanLoaiSP extends javax.swing.JDialog {
    JFileChooser fileChooser = new JFileChooser(
            "C:\\Users\\anctWin10\\Documents\\NetBeansProjects\\DuAn1\\src\\main\\resources\\Icon");
    PhanLoaiSPDAO dao = new PhanLoaiSPDAO();
    int row = 0;

    /**
     * Creates new form QLPhanLoaiSP
     */
    public QLPhanLoaiSP(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    void init() {
        setLocationRelativeTo(null);
        fillTable();
        // fillComboBoxPhanLoaiSP();
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblPhanLoaiSP.getModel();
        model.setRowCount(0);

        try {
            List<PhanLoaiSP> list = dao.selectAll();
            for (PhanLoaiSP tk : list) {
                Object[] row = {
                        tk.getMaPL(),
                        tk.getTenPL(),
                        tk.getThongTin()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    // void fillComboBoxPhanLoaiSP() {
    //
    // DefaultComboBoxModel model = (DefaultComboBoxModel) cboPhanLoaiSP.getModel();
    // model.removeAllElements();
    //
    //
    // }

    void edit() {
        try {
            String maTK = (String) tblPhanLoaiSP.getValueAt(this.row, 0);
            PhanLoaiSP model = dao.selectById(maTK);
            if (model != null) {
                setForm(model);
                updateStatus();
                // tabs.setSelectedIndex(0);

            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    boolean maPLIsExist() {
        try {
            List<PhanLoaiSP> list = dao.selectAll();
            for (PhanLoaiSP tk : list) {
                if (txtMaPL.getText().equalsIgnoreCase(tk.getMaPL())) {

                    return true;
                }
            }

        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
        return false;
    }

    boolean validateInput() {
        String maPL = txtMaPL.getText().trim();
        String tenPL = txtTenPL.getText().trim();
        String thongTin = txtThongTin.getText().trim();

        // Kiểm tra mã sản phẩm
        if (maPL.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mã phân loại!");
            txtMaPL.requestFocus();
            return false;
        }
        if (maPL.length() > 10) {
            MsgBox.alert(this, "Mã phân loại không được quá 10 ký tự!");
            txtMaPL.requestFocus();
            return false;
        }
        if (maPLIsExist()) {
            MsgBox.alert(this, "Không được trùng mã phân loại!");
            txtMaPL.requestFocus();
            return false;
        }

        // Kiểm tra tên sản phẩm
        if (tenPL.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập tên sản phẩm!");
            txtTenPL.requestFocus();
            return false;
        }
        if (tenPL.length() > 100) {
            MsgBox.alert(this, "Tên sản phẩm không được quá 100 ký tự!");
            txtTenPL.requestFocus();
            return false;
        }
        return true;
    }

    PhanLoaiSP getForm() {
        PhanLoaiSP nv = new PhanLoaiSP();
        nv.setMaPL(txtMaPL.getText());
        nv.setTenPL(txtTenPL.getText());
        nv.setThongTin(txtThongTin.getText());
        return nv;
    }

    void setForm(PhanLoaiSP model) {
        txtMaPL.setText(model.getMaPL());
        txtTenPL.setText(model.getTenPL());
        txtThongTin.setText(model.getThongTin());
    }

    void updateStatus() {
        boolean edit = this.row >= 0;
        boolean first = this.row == 0;
        boolean last = this.row == tblPhanLoaiSP.getRowCount() - 1;
        txtMaPL.setEditable(!edit);
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
        PhanLoaiSP nv = new PhanLoaiSP();
        this.setForm(nv);
        this.row = -1;
        this.updateStatus();
    }

    void insert() {
        if (validateInput()) {
            PhanLoaiSP model = getForm();
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
            PhanLoaiSP model = getForm();
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
        if (MsgBox.confirm(this, "Bạn thực sự muốn xóa phan loai này?")) {
            String maTK = txtMaPL.getText();
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
        if (row < tblPhanLoaiSP.getRowCount() - 1) {
            row++;
            edit();
        }
    }

    void last() {
        row = tblPhanLoaiSP.getRowCount() - 1;
        edit();
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
        tblPhanLoaiSP = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnLuu = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtMaPL = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTenPL = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtThongTin = new javax.swing.JTextArea();
        btnMoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản Lý Sản Phẩm");

        tblPhanLoaiSP.setBackground(new java.awt.Color(255, 250, 243));
        tblPhanLoaiSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã PL", "Tên PL", "Thong tin"
            }
        ));
        tblPhanLoaiSP.setSelectionBackground(new java.awt.Color(0, 102, 255));
        tblPhanLoaiSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPhanLoaiSPMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblPhanLoaiSP);

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

        jLabel2.setText("Mã phân loại");

        txtMaPL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaPLActionPerformed(evt);
            }
        });

        jLabel3.setText("Tên phân loại:");

        txtTenPL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenPLActionPerformed(evt);
            }
        });

        jLabel9.setText("Thông tin:");

        txtThongTin.setColumns(20);
        txtThongTin.setRows(5);
        jScrollPane2.setViewportView(txtThongTin);

        btnMoi.setBackground(new java.awt.Color(255, 255, 204));
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/Add.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaPL)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnMoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(btnLuu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCapNhat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoa))
                    .addComponent(txtTenPL)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaPL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTenPL, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
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

    private void txtMaPLActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtMaPLActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtMaPLActionPerformed

    private void txtTenPLActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTenPLActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtTenPLActionPerformed

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

    private void tblPhanLoaiSPMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblPhanLoaiSPMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblPhanLoaiSP.rowAtPoint(evt.getPoint());
            edit();
        }
        boolean edit = this.row >= 0;
        btnLuu.setEnabled(!edit);
        btnMoi.setEnabled(edit);
        btnCapNhat.setEnabled(edit);
        btnXoa.setEnabled(edit);
    }// GEN-LAST:event_tblPhanLoaiSPMousePressed

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
            java.util.logging.Logger.getLogger(QLPhanLoaiSP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLPhanLoaiSP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLPhanLoaiSP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLPhanLoaiSP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>
        // </editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLPhanLoaiSP dialog = new QLPhanLoaiSP(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblPhanLoaiSP;
    private javax.swing.JTextField txtMaPL;
    private javax.swing.JTextField txtTenPL;
    private javax.swing.JTextArea txtThongTin;
    // End of variables declaration//GEN-END:variables
}
