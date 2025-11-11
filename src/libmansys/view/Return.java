package libmansys.view;

import java.sql.Connection;

import libmansys.dao.ReturnDAO;
import libmansys.utils.Helper;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import libmansys.dao.StudentDAO;
import org.jdesktop.swingx.prompt.PromptSupport;

public class Return extends javax.swing.JFrame {

    private Connection conn;
    private ReturnDAO returnDAO;
    private String currentUsername;
    private String selectedBookID;
    private String studentID;
    private java.sql.Date storedReturnDate;
    private int storedOverdue;
    private StudentDAO studentDAO;

    public Return(Connection conn, String selectedBookID, String currentUsername) {
        initComponents();
        setLocationRelativeTo(null);

        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection is null!");
            return; // 
        }
        this.returnDAO = new ReturnDAO(conn);
        this.studentDAO = new StudentDAO(conn);
        this.conn = conn;
        this.selectedBookID = selectedBookID;
        this.currentUsername = currentUsername;

        // Modify the design of table
        JTableHeader header = btrTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        btrTable.setDefaultEditor(Object.class, null);
        btrTable.setFillsViewportHeight(true);
        btrTable.setRowHeight(28);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10)); // padding

        for (int i = 0; i < btrTable.getColumnCount(); i++) {
            btrTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        //add search
        PromptSupport.setPrompt("Search by Student ID or Student Name", txtSearch);
        PromptSupport.setForeground(Color.GRAY, txtSearch);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, txtSearch);

        autoDisabled();

        //from issue book
        if (selectedBookID != null && !selectedBookID.isEmpty()) {
            loadSelectedBorrowedBook(selectedBookID);
            if (btrTable.getRowCount() == 1) {
                btrTable.setRowSelectionInterval(0, 0); // select first and only row 
                cboCondition.setSelectedIndex(0);
                updatePenaltyAndDate();
            }
        }

        SwingUtilities.invokeLater(() -> {
            if (btrTable.getColumnCount() > 1) {
                btrTable.removeColumn(btrTable.getColumnModel().getColumn(0)); // hide btr_id
            }
        });

        cboCondition.addActionListener(e -> updatePenaltyAndDate());

        btrTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updatePenaltyAndDate();

                //enable return button when a row is selected
                btnReturn.setEnabled(btrTable.getSelectedRow() != -1);
            }
        });

        returnDate.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                updatePenaltyAndDate();
            }
        });
    }

    private void clearBookDetails() {
        DefaultTableModel model = (DefaultTableModel) btrTable.getModel();
        model.setRowCount(0);

        txtSearch.setText("");
        txtStudentID.setText("");
        txtName.setText("");
        txtCourse.setText("");
        txtEmail.setText("");
        returnDate.setDate(new java.util.Date());
        btnReturn.setEnabled(false);
        cboCondition.setSelectedIndex(0);
        storedReturnDate = null;
        storedOverdue = 0;
    }

    private void autoDisabled() {
        txtStudentID.setEnabled(false);
        txtName.setEnabled(false);
        txtCourse.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPenalty.setEnabled(false);
        btnReturn.setEnabled(false);

        txtStudentID.setDisabledTextColor(Color.BLACK);
        txtName.setDisabledTextColor(Color.BLACK);
        txtCourse.setDisabledTextColor(Color.BLACK);
        txtEmail.setDisabledTextColor(Color.BLACK);
        txtPenalty.setDisabledTextColor(Color.BLACK);

        //set current date for return datepicker
        returnDate.setDate(new java.util.Date());
        returnDate.setDateFormatString("MM/dd/yyyy");

        java.util.Date utilDate = returnDate.getDate();
        storedReturnDate = new java.sql.Date(utilDate.getTime());
    }

    private void loadSelectedBorrowedBook(String bookID) {
        try {
            ResultSet rs = returnDAO.getBorrowedBook(bookID);

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No borrowed record found for this Book ID.");
                return;
            }

            // Fill student info
            txtStudentID.setText(rs.getString("student_id"));
            txtName.setText(rs.getString("full_name"));
            txtCourse.setText(rs.getString("course"));
            txtEmail.setText(rs.getString("email_address"));

            studentID = txtStudentID.getText();

            // Fill the table with the selected borrowed book
            DefaultTableModel model = (DefaultTableModel) btrTable.getModel();
            model.setRowCount(0);

            model.addRow(new Object[]{
                rs.getString("btr_id"),
                rs.getString("book_id"),
                rs.getString("book_title"),
                rs.getString("borrow_date"),
                rs.getString("due_date"),
                rs.getString("status")
            });

            // Select the first row in the table
            if (btrTable.getRowCount() > 0) {
                btrTable.setRowSelectionInterval(0, 0);
            }

            //enable the Return button
            btnReturn.setEnabled(true);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading borrowed book details.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField5 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnReturn = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtStudentID = new javax.swing.JTextField();
        txtPenalty = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        returnDate = new com.toedter.calendar.JDateChooser();
        cboCondition = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        txtCourse = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        btrTable = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        jTextField5.setText("jTextField5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 255)), "Return Book", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 24), new java.awt.Color(204, 0, 204))); // NOI18N

        btnReturn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnReturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Rbook.png"))); // NOI18N
        btnReturn.setText("Return");
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel9.setText("Book Return Details");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Return Date");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Penalty (P)");

        txtPenalty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPenaltyActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Condition");

        returnDate.setMaxSelectableDate(new java.util.Date());

        cboCondition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Good", "Lost", "Minor Damage", "Major Damage" }));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Student ID");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Name");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Course");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Email");

        btnSearch.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btrTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btrTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "BTR ID", "Book ID", "Book Title", "Borrowed Date", "Due Date", "Status"
            }
        ));
        jScrollPane1.setViewportView(btrTable);

        btnClear.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnClear.setText("Clear");
        btnClear.setMaximumSize(new java.awt.Dimension(106, 31));
        btnClear.setMinimumSize(new java.awt.Dimension(106, 31));
        btnClear.setPreferredSize(new java.awt.Dimension(106, 31));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
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
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSearch))
                    .addComponent(jLabel9)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnReturn)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(returnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(returnDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboCondition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReturn)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        btnHome.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Home.png"))); // NOI18N
        btnHome.setText("Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Logo3.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(358, 358, 358)
                        .addComponent(btnHome)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnHome)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        Helper.goBackToHome(this, conn);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void txtPenaltyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPenaltyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPenaltyActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Student ID or Student Name");
            return;
        }

        // 1. Check if student exists
        boolean exists = studentDAO.studentExists(keyword);

        if (!exists) {
            JOptionPane.showMessageDialog(this, "Student does not exist.");
            return;
        }

        ArrayList<ArrayList<String>> borrows = returnDAO.searchBorrow(keyword);

        if (borrows == null || borrows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student has no borrowed records.");
            return;
        }

        ArrayList<String> first = borrows.get(0); //get the first row

        String studentId = first.get(6); //get student ID
        String studentName = first.get(7); // get full name

        if ((studentId == null || studentId.trim().isEmpty())
                && (studentName == null || studentName.trim().isEmpty())) {

            JOptionPane.showMessageDialog(this, "Student does not exist.");
            return;
        }

        if (borrows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No borrowed record found for this student.");
            return;
        } else {
            txtStudentID.setText(first.get(6));
            txtName.setText(first.get(7));
            txtCourse.setText(first.get(8));
            txtEmail.setText(first.get(9));

            loadBorrowToTable(btrTable, keyword);
            btrTable.setRowHeight(40);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        int selectedRow = btrTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return.");
            return;
        }

        try {
            DefaultTableModel model = (DefaultTableModel) btrTable.getModel();
            int modelRow = btrTable.convertRowIndexToModel(selectedRow);

            int btrId = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
            String bookId = model.getValueAt(modelRow, 1).toString();

            // Ensure return date was computed
            if (storedReturnDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid return date first.");
                return;
            }

            // Condition            
            String condition = cboCondition.getSelectedItem().toString();

            // Penalty
            double penalty = Double.parseDouble(txtPenalty.getText());

            // Update return record
            boolean updated = returnDAO.returnBook(
                    btrId,
                    storedReturnDate,
                    condition,
                    storedOverdue,
                    penalty,
                    currentUsername
            );

            if (!updated) {
                JOptionPane.showMessageDialog(this, "Failed to update return record.");
                return;
            }

            // If book is returned in good condition and minor damage → add 1 to quantity
            if ("Good".equals(condition) || "Minor Damage".equals(condition)) {
                returnDAO.increaseQty(bookId);
                JOptionPane.showMessageDialog(this, "Book return successfully saved!");
            }

            // Refresh table
            String keyword = txtSearch.getText().trim();
            if (!keyword.isEmpty()) {
                loadBorrowToTable(btrTable, keyword);
            } else {
                loadBorrowToTable(btrTable, studentID);
            }

            txtPenalty.setText("0.00");
            cboCondition.setSelectedIndex(0);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing the return.");
        }
    }//GEN-LAST:event_btnReturnActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearBookDetails();
    }//GEN-LAST:event_btnClearActionPerformed

    public void loadBorrowToTable(JTable table, String keyword) {
        ArrayList<ArrayList<String>> borrows = returnDAO.searchBorrow(keyword);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // clear previous rows

        if (!borrows.isEmpty()) {
            // Fill Student Info
            ArrayList<String> first = borrows.get(0);
            txtStudentID.setText(first.get(6));
            txtName.setText(first.get(7));
            txtCourse.setText(first.get(8));
            txtEmail.setText(first.get(9));

            // Add all borrowed books to the table
            for (ArrayList<String> borrow : borrows) {
                Object[] row = new Object[6];
                row[0] = borrow.get(0); // btr_id
                row[1] = borrow.get(1); // book_id
                row[2] = borrow.get(2); // book_title
                row[3] = borrow.get(3); // borrow_date
                row[4] = borrow.get(4); // due_date
                row[5] = borrow.get(5); // status

                model.addRow(row);
            }
        }
    }

    private void updatePenaltyAndDate() {
        int selectedRow = btrTable.getSelectedRow();
        if (selectedRow == -1) {
            txtPenalty.setText("0.00");
            storedReturnDate = null;
            storedOverdue = 0;
            return;
        }

        // Convert view row to model row
        int modelRow = btrTable.convertRowIndexToModel(selectedRow);

        try {
            // Access hidden BTR ID from the model -- need kasi for calculation
            DefaultTableModel model = (DefaultTableModel) btrTable.getModel();

            String bookId = model.getValueAt(modelRow, 1).toString();
            String borrowStr = model.getValueAt(modelRow, 3).toString();

            // parse borrow date
            DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate borrowDate = LocalDate.parse(borrowStr, dbFormat);

            // due date = borrow + 7 days
            LocalDate dueDate = borrowDate.plusDays(7);

            // get return date from date chooser
            java.util.Date pickedDate = returnDate.getDate();
            if (pickedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a return date.");
                return;
            }

            LocalDate returnDateLd = pickedDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            // validation
            if (returnDateLd.isBefore(borrowDate)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Return date cannot be earlier than the borrow date.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // store return date for button use
            storedReturnDate = new java.sql.Date(pickedDate.getTime());

            // compute overdue
            long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDateLd);
            if (overdueDays < 0) {
                overdueDays = 0;
            }

            // store overdue for button use
            storedOverdue = (int) overdueDays;

            // Get details from book
            double bookPrice = returnDAO.getBookPrice(bookId);
            String condition = cboCondition.getSelectedItem().toString();

            // compute penalty
            double penalty = 0.0;
            switch (condition) {
                case "Good":
                    penalty = overdueDays * 10.0;
                    break;

                case "Lost":
                    penalty = bookPrice + (overdueDays * 10.0);
                    break;

                case "Minor Damage":
                    penalty = 50;
                    break;

                case "Major Damage":
                    penalty = bookPrice;
                    break;
            }

            txtPenalty.setText(String.format("%.2f", penalty));

            // Display penalty in textbox
            txtPenalty.setText(String.format("%.2f", penalty));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error computing penalty.");
        }
    }

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnReturn;
    private javax.swing.JButton btnSearch;
    private javax.swing.JTable btrTable;
    private javax.swing.JComboBox<String> cboCondition;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField5;
    private com.toedter.calendar.JDateChooser returnDate;
    private javax.swing.JTextField txtCourse;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPenalty;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStudentID;
    // End of variables declaration//GEN-END:variables

}
