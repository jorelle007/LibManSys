package libmansys.view;

import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import libmansys.dao.StudentDAO;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import libmansys.dao.UserDAO;
import libmansys.model.User;
import libmansys.utils.Helper;
import org.jdesktop.swingx.prompt.PromptSupport;

public class Student extends javax.swing.JFrame {

    private StudentDAO studentDAO;
    private Connection conn;
    private String currentUsername;
    private double originalTotalPenalty = 0.0;

    public Student() {
        this(null, null);
    }

    public Student(Connection conn) {
        this(conn, null);
    }

    public Student(Connection conn, String currentUsername) {
        initComponents();
        setLocationRelativeTo(null); // center window
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection is null!");
            return; // 
        }
        this.studentDAO = new StudentDAO(conn); // pag-set ng DAO as single connection
        this.conn = conn;
        this.currentUsername = currentUsername;

        txtTotalPenalty.setText("0.0000");
        txtTotalPenalty.setEditable(false);
        chkPaid.setEnabled(false);

        // Modify the design of table
        JTableHeader header = tblStudents.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        tblStudents.setDefaultEditor(Object.class, null);
        tblStudents.setFillsViewportHeight(true);
        tblStudents.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblStudents.setRowHeight(28);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10)); // padding

        TableColumn paidColumn = tblStudents.getColumnModel().getColumn(7);
        tblStudents.getColumnModel().removeColumn(paidColumn); // hides Paid column
        Helper.autoResizeColumns(tblStudents);

        for (int i = 0; i < tblStudents.getColumnCount(); i++) {
            tblStudents.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        //add live search
        PromptSupport.setPrompt("Search by First Name, Last Name, or Email", txtSearch);
        PromptSupport.setForeground(Color.GRAY, txtSearch);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, txtSearch);

        Helper.addTableFilter(tblStudents, txtSearch, 1, 2, 5);

        //disable buttons in load up
        disableButtons();

        //get autoincrement ID
        getNextID(); // optional, can be removed

        //fetch student data
        loadStudents();

        //table click behaviour for update/delete
        tableClick();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtStudentID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtContact = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtCourse = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtTotalPenalty = new javax.swing.JTextField();
        chkPaid = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        txtSearch1 = new javax.swing.JTextField();
        txtSearch = new javax.swing.JTextField();
        btnHome = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)), "Student", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24), new java.awt.Color(204, 0, 255))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Course");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Contact Number");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Email address");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("First Name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Last Name");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Student ID");

        txtFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFirstNameActionPerformed(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/update.png"))); // NOI18N
        btnUpdate.setText("UPDATE");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Total Penalty");

        chkPaid.setText("Paid");
        chkPaid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPaidActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel5)
                                .addComponent(jLabel1)
                                .addComponent(jLabel7))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txtTotalPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(chkPaid))
                                .addComponent(txtCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnUpdate)
                            .addGap(18, 18, 18)
                            .addComponent(btnDelete))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalPenalty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(chkPaid))
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete))
                .addGap(28, 28, 28))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 153)), "Student Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 24), new java.awt.Color(204, 0, 205))); // NOI18N

        tblStudents.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Student ID", "First Name", "Last Name", "Course", "Contact Number", "Email Address", "Total Penalty", "Paid"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStudents.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblStudents.setAutoscrolls(false);
        tblStudents.setFillsViewportHeight(true);
        tblStudents.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblStudents.setShowGrid(true);
        jScrollPane1.setViewportView(tblStudents);

        txtSearch1.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(txtSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(txtSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        btnHome.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Home.png"))); // NOI18N
        btnHome.setText("Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        btnClear.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnClear.setText("CLEAR");
        btnClear.setMaximumSize(new java.awt.Dimension(112, 31));
        btnClear.setMinimumSize(new java.awt.Dimension(112, 31));
        btnClear.setPreferredSize(new java.awt.Dimension(112, 31));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnHome)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnHome)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstNameActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        Helper.goBackToHome(this, conn, currentUsername);
        this.dispose();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        try {
            if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || txtCourse.getText().isEmpty()
                    || txtContact.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!");
                return;
            } else {
                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                String course = txtCourse.getText();
                String contact = txtContact.getText();
                String email = txtEmail.getText();
                LocalDate today = LocalDate.now();
                Date dateRegistered = Date.valueOf(today);

                studentDAO.addStudent(firstName, lastName, course, contact, email, dateRegistered);

                JOptionPane.showMessageDialog(this, "Student added successfully!");
                clearTextFields();
                loadStudents();
                getNextID();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding student!");
        }


    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed

        int row = tblStudents.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.");
            return;
        }

        try {
            // Convert view row to model row if sorter is active
            int modelRow = tblStudents.convertRowIndexToModel(row);

            // Get current values from the table
            String tableFirstName = tblStudents.getModel().getValueAt(modelRow, 1).toString();
            String tableLastName = tblStudents.getModel().getValueAt(modelRow, 2).toString();
            String tableCourse = tblStudents.getModel().getValueAt(modelRow, 3).toString();
            String tableContact = tblStudents.getModel().getValueAt(modelRow, 4).toString();
            String tableEmail = tblStudents.getModel().getValueAt(modelRow, 5).toString();
            originalTotalPenalty = ((Number) tblStudents.getModel().getValueAt(modelRow, 6)).doubleValue();
            Boolean tablePaid = (Boolean) tblStudents.getModel().getValueAt(modelRow, 7);

            // Get values from the form
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String course = txtCourse.getText().trim();
            String contact = txtContact.getText().trim();
            String email = txtEmail.getText().trim();
            Boolean isPaid = chkPaid.isSelected();

            // Validate form
            if (firstName.isEmpty() || lastName.isEmpty() || course.isEmpty()
                    || contact.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill out all fields.",
                        "Form Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if any changes were actually made
            if (firstName.equals(tableFirstName)
                    && lastName.equals(tableLastName)
                    && course.equals(tableCourse)
                    && contact.equals(tableContact)
                    && email.equals(tableEmail)
                    && isPaid.equals(tablePaid)) {
                JOptionPane.showMessageDialog(this, "No changes were made.");
                return;
            }

            // Get student ID
            int studentId = Integer.parseInt(tblStudents.getModel().getValueAt(modelRow, 0).toString());

            // Call DAO method that updates student info and unpaid penalties in a transaction
            boolean isUpdated = studentDAO.updateStudent(studentId, firstName, lastName, course, contact, email, isPaid);

            if (isUpdated) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
            }

            // Clear form and reload table
            clearTextFields();
            tblStudents.setRowSorter(null);          // temporarily remove sorter
            loadStudents();                           // reload table from DAO
            tblStudents.setAutoCreateRowSorter(true); // re-enable sorting
            getNextID();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = tblStudents.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int studentId = Integer.parseInt(txtStudentID.getText().trim());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JPasswordField password = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(
                    this, password, "Enter your password to confirm delete",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option != JOptionPane.OK_OPTION) {
                return;
            }
            String enteredPassword = new String(password.getPassword());

            if (enteredPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your password.");
                return;
            }

            try {

                UserDAO userDAO = new UserDAO(conn);
                User user = userDAO.login(currentUsername, enteredPassword);

                if (user == null) {
                    JOptionPane.showMessageDialog(this, "Incorrect password. Delete cancelled.");
                    return;
                } else {
                    int studentID = Integer.parseInt(txtStudentID.getText());
                    String studentName = txtFirstName.getText() + txtLastName.getText();
                    boolean isDeleted = studentDAO.deleteStudent(studentID);

                    if (isDeleted) {
                        JOptionPane.showMessageDialog(this, "Student " + studentName + "  has been successfully deleted.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        //JOptionPane.showMessageDialog(this, "Book " + title + " deleted succesfully");
                        loadStudents();
                        clearTextFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cannot delete this student. Unreturned books or unpaid penalties exist.",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

//                if (user == null) {
//                    JOptionPane.showMessageDialog(this, "Incorrect password. Delete cancelled.");
//                    return;
//                } else {
//                    studentDAO.deleteStudent(studentId);
//                    JOptionPane.showMessageDialog(this, "Student deleted successfully!");
//
//                    clearTextFields();
//                    loadStudents();
//                    getNextID();
//                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting student: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearTextFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void chkPaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPaidActionPerformed
        if (chkPaid.isSelected()) {
            txtTotalPenalty.setText("0.0000");
        } else {
            txtTotalPenalty.setText(String.format("%.4f", originalTotalPenalty));
        }
    }//GEN-LAST:event_chkPaidActionPerformed

    private void tableClick() {
        tblStudents.getSelectionModel().addListSelectionListener(e -> {
            int row = tblStudents.getSelectedRow();
            boolean selected = row != -1;

            btnAdd.setEnabled(!selected);
            btnUpdate.setEnabled(selected);
            btnDelete.setEnabled(selected);

            if (selected) {
                txtStudentID.setText(tblStudents.getValueAt(row, 0).toString());
                txtFirstName.setText(tblStudents.getValueAt(row, 1).toString());
                txtLastName.setText(tblStudents.getValueAt(row, 2).toString());
                txtCourse.setText(tblStudents.getValueAt(row, 3).toString());
                txtContact.setText(tblStudents.getValueAt(row, 4).toString());
                txtEmail.setText(tblStudents.getValueAt(row, 5).toString());
                chkPaid.setSelected((Boolean) tblStudents.getModel().getValueAt(row, 7));

                originalTotalPenalty = ((Number) tblStudents.getModel().getValueAt(row, 6)).doubleValue();

                // Set total penalty text field with 4 decimal places
                txtTotalPenalty.setText(String.format("%.4f", originalTotalPenalty));

                // If totalPenalty is 0, disable and uncheck checkbox
                if (originalTotalPenalty == 0.0) {
                    chkPaid.setSelected(false);
                    chkPaid.setEnabled(false);
                } else {
                    chkPaid.setSelected(false);
                    chkPaid.setEnabled(true);
                }
            }
        });
    }

    private void getNextID() {
        txtStudentID.setText(String.valueOf(studentDAO.getNextStudentId()));
        txtStudentID.setEnabled(false);
    }

    private void disableButtons() {
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    private void loadStudents() {
        studentDAO.loadStudents(tblStudents);
    }

    private void clearTextFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtCourse.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        tblStudents.clearSelection();
        txtTotalPenalty.setText("0.0000");
        chkPaid.setSelected(false);
        chkPaid.setEnabled(false);

    }

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
            java.util.logging.Logger.getLogger(NewBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Student().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox chkPaid;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblStudents;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtCourse;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearch1;
    private javax.swing.JTextField txtStudentID;
    private javax.swing.JTextField txtTotalPenalty;
    // End of variables declaration//GEN-END:variables
}
