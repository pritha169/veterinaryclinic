package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PatientDashboard extends JFrame implements ActionListener {
    private String patientId;
    private JButton btnBookAppointment, btnViewAppointments, btnMedicineCorner,
            btnMedicalHistory, btnViewProfile, btnHomeService, btnLogout;

    public PatientDashboard(String patientId) {
        this.patientId = patientId;

        setTitle("Patient Dashboard - ID: " + patientId);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ Set background image
        ImageIcon bgIcon = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/patient panel.jpg"));
        Image img = bgIcon.getImage().getScaledInstance(700, 550, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(img));
        setContentPane(background);
        background.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBackground(new Color(0, 0, 0, 80));
        JLabel welcomeLabel = new JLabel("Welcome, Patient ID: " + patientId);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(welcomeLabel);
        background.add(headerPanel, BorderLayout.NORTH);

        // Buttons
        JPanel centerPanel = new JPanel(new GridLayout(7, 1, 15, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 180, 40, 180));

        btnBookAppointment = new JButton("Book Appointment");
        styleButton(btnBookAppointment);
        btnBookAppointment.addActionListener(this);
        centerPanel.add(btnBookAppointment);

        btnHomeService = new JButton("Home Service");
        styleButton(btnHomeService);
        btnHomeService.addActionListener(this);
        centerPanel.add(btnHomeService);

        btnViewAppointments = new JButton("View My Appointments");
        styleButton(btnViewAppointments);
        btnViewAppointments.addActionListener(this);
        centerPanel.add(btnViewAppointments);

        btnMedicineCorner = new JButton("Medicine Corner");
        styleButton(btnMedicineCorner);
        btnMedicineCorner.addActionListener(this);
        centerPanel.add(btnMedicineCorner);

        btnMedicalHistory = new JButton("Medical History");
        styleButton(btnMedicalHistory);
        btnMedicalHistory.addActionListener(this);
        centerPanel.add(btnMedicalHistory);

        btnViewProfile = new JButton("View Profile");
        styleButton(btnViewProfile);
        btnViewProfile.addActionListener(this);
        centerPanel.add(btnViewProfile);

        btnLogout = new JButton("Logout");
        styleButton(btnLogout);
        btnLogout.addActionListener(this);
        centerPanel.add(btnLogout);

        background.add(centerPanel, BorderLayout.CENTER);

        checkReminders();
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(173, 216, 230)); // Light blue
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    }

    private void checkReminders() {
        try {
            ConnectionClass cc = new ConnectionClass();
            String sql = "SELECT appointment_date, appointment_time, type, pet_category " +
                    "FROM appointment WHERE patient_id = ? AND reminder = 1 AND " +
                    "(appointment_date > CURDATE() OR (appointment_date = CURDATE() AND appointment_time > CURTIME()))";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, patientId);
            ResultSet rs = pst.executeQuery();

            StringBuilder reminders = new StringBuilder();
            while (rs.next()) {
                reminders.append("Reminder: ")
                        .append(rs.getString("type")).append(" appointment for your ")
                        .append(rs.getString("pet_category")).append(" on ")
                        .append(rs.getString("appointment_date")).append(" at ")
                        .append(rs.getString("appointment_time")).append("\n");
            }
            cc.con.close();

            if (reminders.length() > 0) {
                JOptionPane.showMessageDialog(this, reminders.toString(), "Appointment Reminders", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBookAppointment) {
            new AppointmentScheduler(patientId);
        } else if (e.getSource() == btnHomeService) {
            new HomeServicePage(patientId);
        } else if (e.getSource() == btnViewAppointments) {
            new ViewAppointments(patientId);
        } else if (e.getSource() == btnMedicineCorner) {
            new MedicineCorner(patientId);
        } else if (e.getSource() == btnMedicalHistory) {
            new MedicalHistoryPage(patientId);
        } else if (e.getSource() == btnViewProfile) {
            new PatientProfile(patientId);
        } else if (e.getSource() == btnLogout) {
            setVisible(false);
            new PatientLoginPage();
        }
    }

    public static void main(String[] args) {
        new PatientDashboard("P123");
    }
}
