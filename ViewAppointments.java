package Hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewAppointments extends JFrame implements ActionListener {
    private String patientId;
    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnPending, btnFuture, btnCompleted;
    private JButton btnViewMedicalHistory;

    public ViewAppointments(String patientId) {
        this.patientId = patientId;

        setTitle("My Appointments");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        btnPending = new JButton("Pending Appointments");
        btnFuture = new JButton("Future Appointments");
        btnCompleted = new JButton("Previously Visited");

        btnPending.addActionListener(this);
        btnFuture.addActionListener(this);
        btnCompleted.addActionListener(this);

        buttonPanel.add(btnPending);
        buttonPanel.add(btnFuture);
        buttonPanel.add(btnCompleted);

        add(buttonPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        // Define columns
        tableModel.addColumn("Appointment ID");
        tableModel.addColumn("Doctor");
        tableModel.addColumn("Date");
        tableModel.addColumn("Time");
        tableModel.addColumn("Pet Category");
        tableModel.addColumn("Type");
        tableModel.addColumn("Status");

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Medical History button
        JPanel bottomPanel = new JPanel();
        btnViewMedicalHistory = new JButton("View Medical History");
        btnViewMedicalHistory.addActionListener(e -> new MedicalHistoryPage(patientId));
        bottomPanel.add(btnViewMedicalHistory);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load default view: pending appointments
        loadAppointments("pending");

        setVisible(true);
    }

    private void loadAppointments(String category) {
        try {
            ConnectionClass cc = new ConnectionClass();

            // ✅ Automatically update past appointments to completed
            String updateSql = "UPDATE appointment SET status = 'completed' " +
                    "WHERE appointment_date < CURDATE() AND status = 'pending'";
            Statement updateStmt = cc.con.createStatement();
            updateStmt.executeUpdate(updateSql);
            updateStmt.close();

            // Define the query
            String sql = "";
            switch (category) {
                case "pending":
                    sql = "SELECT a.appointment_id, d.name AS doctor_name, a.appointment_date, a.appointment_time, " +
                            "a.pet_category, a.type, a.status " +
                            "FROM appointment a JOIN doctor d ON a.doctor_id = d.doctor_id " +
                            "WHERE a.patient_id = ? AND a.status = 'pending' " +
                            "AND a.appointment_date >= CURDATE() AND a.appointment_date <= DATE_ADD(CURDATE(), INTERVAL 6 DAY) " +
                            "ORDER BY a.appointment_date ASC";
                    break;

                case "future":
                    sql = "SELECT a.appointment_id, d.name AS doctor_name, a.appointment_date, a.appointment_time, " +
                            "a.pet_category, a.type, a.status " +
                            "FROM appointment a JOIN doctor d ON a.doctor_id = d.doctor_id " +
                            "WHERE a.patient_id = ? AND a.status = 'pending' " +
                            "AND a.appointment_date > DATE_ADD(CURDATE(), INTERVAL 6 DAY) " +
                            "ORDER BY a.appointment_date ASC";
                    break;

                case "completed":
                    sql = "SELECT a.appointment_id, d.name AS doctor_name, a.appointment_date, a.appointment_time, " +
                            "a.pet_category, a.type, a.status " +
                            "FROM appointment a JOIN doctor d ON a.doctor_id = d.doctor_id " +
                            "WHERE a.patient_id = ? AND a.status = 'completed' " +
                            "ORDER BY a.appointment_date DESC";
                    break;
            }

            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, patientId);

            ResultSet rs = pst.executeQuery();

            // Clear previous table data
            tableModel.setRowCount(0);

            // Populate table with result
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                tableModel.addRow(new Object[]{
                        rs.getInt("appointment_id"),
                        rs.getString("doctor_name"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("pet_category"),
                        rs.getString("type"),
                        rs.getString("status")
                });
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(this, "No " + category + " appointments found.");
            }

            rs.close();
            pst.close();
            cc.con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPending) {
            loadAppointments("pending");
        } else if (e.getSource() == btnFuture) {
            loadAppointments("future");
        } else if (e.getSource() == btnCompleted) {
            loadAppointments("completed");
        }
    }

    public static void main(String[] args) {
        new ViewAppointments("P123");  // Replace with actual patient ID
    }
}
