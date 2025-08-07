package Hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;

public class DoctorAppointments extends JFrame implements ActionListener {

    // Fields
    private final String doctorId;
    private JTable appointmentTable;
    private JButton btnCompleteAppointment;
    private JTextArea txtAdvice, txtPrescription;

    // Constructor
    public DoctorAppointments(String doctorId) {
        this.doctorId = doctorId;
        initializeUI();
        updatePastAppointmentsStatus();
        fetchAppointments();
        setVisible(true);
    }

    // UI Initialization
    private void initializeUI() {
        setTitle("Upcoming Appointments - Doctor ID: " + doctorId);
        setSize(600, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load background image using ClassLoader
        ImageIcon bgIcon = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/doctor appoint.jpg"));

        JLabel backgroundLabel = new JLabel(bgIcon);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // Create transparent panel for components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);  // Make transparent so background shows through

        JLabel heading = new JLabel("Upcoming Appointments for Doctor " + doctorId, JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        heading.setForeground(Color.BLACK);  // Make text visible on background
        mainPanel.add(heading, BorderLayout.NORTH);

        appointmentTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(appointmentTable);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        bottomPanel.setOpaque(false);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        backgroundLabel.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setOpaque(false);

        txtAdvice = new JTextArea("Write advice here...");
        txtPrescription = new JTextArea("Write prescription here...");

        txtAdvice.setEnabled(false);
        txtPrescription.setEnabled(false);

        panel.add(new JScrollPane(txtAdvice));
        panel.add(new JScrollPane(txtPrescription));

        btnCompleteAppointment = new JButton("Complete Appointment");
        btnCompleteAppointment.setEnabled(false);
        btnCompleteAppointment.addActionListener(this);
        panel.add(btnCompleteAppointment);

        appointmentTable.getSelectionModel().addListSelectionListener(e -> {
            boolean isSelected = appointmentTable.getSelectedRow() != -1;
            txtAdvice.setEnabled(isSelected);
            txtPrescription.setEnabled(isSelected);
            btnCompleteAppointment.setEnabled(isSelected);
        });

        return panel;
    }

    // Fetch upcoming appointments from database
    private void fetchAppointments() {
        try (ConnectionClass cc = new ConnectionClass()) {
            String sql = "SELECT a.appointment_id, a.patient_id, p.name AS patient_name, " +
                    "a.appointment_date, a.appointment_time, a.pet_category, a.type " +
                    "FROM appointment a JOIN patient p ON a.patient_id = p.patient_id " +
                    "WHERE a.doctor_id = ? AND a.status <> 'completed' " +
                    "AND (a.type = 'regular' OR (a.type = 'home_service' AND a.status = 'accepted')) " +
                    "ORDER BY a.appointment_date, a.appointment_time";

            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, doctorId);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Appointment ID");
            model.addColumn("Patient ID");
            model.addColumn("Patient Name");
            model.addColumn("Date");
            model.addColumn("Time");
            model.addColumn("Pet Category");
            model.addColumn("Type");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("appointment_id"),
                        rs.getString("patient_id"),
                        rs.getString("patient_name"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("pet_category"),
                        rs.getString("type")
                });
            }

            appointmentTable.setModel(model);

            rs.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch appointments.");
        }
    }

    // Update status of past appointments to 'completed'
    private void updatePastAppointmentsStatus() {
        try (ConnectionClass cc = new ConnectionClass()) {
            String sql = "UPDATE appointment SET status = 'completed' WHERE appointment_date < CURDATE()";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ActionListener method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCompleteAppointment) {
            completeAppointment();
        }
    }

    // Mark selected appointment as completed and save medical records
    private void completeAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.");
            return;
        }

        int appointmentId = (int) appointmentTable.getValueAt(selectedRow, 0);
        String patientId = (String) appointmentTable.getValueAt(selectedRow, 1);
        java.sql.Date appointmentDate = (java.sql.Date) appointmentTable.getValueAt(selectedRow, 3);
        java.sql.Time appointmentTime = (java.sql.Time) appointmentTable.getValueAt(selectedRow, 4);
        String petCategory = (String) appointmentTable.getValueAt(selectedRow, 5);
        String type = (String) appointmentTable.getValueAt(selectedRow, 6);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduled = LocalDateTime.of(appointmentDate.toLocalDate(), appointmentTime.toLocalTime());

        if (now.isBefore(scheduled)) {
            JOptionPane.showMessageDialog(this, "You can only complete this appointment after the scheduled time.");
            return;
        }

        String advice = txtAdvice.getText().trim();
        String prescription = txtPrescription.getText().trim();

        if (advice.isEmpty() || prescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both advice and prescription.");
            return;
        }

        try (ConnectionClass cc = new ConnectionClass()) {
            // Insert medical record
            String insertSql = "INSERT INTO medical_records (appointment_id, patient_id, doctor_id, advice, prescription, pet_category, type, record_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE())";
            PreparedStatement insertStmt = cc.con.prepareStatement(insertSql);
            insertStmt.setInt(1, appointmentId);
            insertStmt.setString(2, patientId);
            insertStmt.setString(3, doctorId);
            insertStmt.setString(4, advice);
            insertStmt.setString(5, prescription);
            insertStmt.setString(6, petCategory);
            insertStmt.setString(7, type);
            insertStmt.executeUpdate();
            insertStmt.close();

            // Update appointment status
            String updateSql = "UPDATE appointment SET status = 'completed' WHERE appointment_id = ?";
            PreparedStatement updateStmt = cc.con.prepareStatement(updateSql);
            updateStmt.setInt(1, appointmentId);
            updateStmt.executeUpdate();
            updateStmt.close();

            JOptionPane.showMessageDialog(this, "Appointment marked as completed and medical record saved.");

            // Reset form and refresh
            fetchAppointments();
            resetForm();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to complete appointment.");
        }
    }

    private void resetForm() {
        txtAdvice.setText("Write advice here...");
        txtPrescription.setText("Write prescription here...");
        txtAdvice.setEnabled(false);
        txtPrescription.setEnabled(false);
        btnCompleteAppointment.setEnabled(false);
    }
}
