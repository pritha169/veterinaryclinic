package Hospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MedicalHistoryPage extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private String patientId;
    private Image backgroundImage;

    public MedicalHistoryPage(String patientId) {
        this.patientId = patientId;

        setTitle("Medical History - Patient ID: " + patientId);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load background image from resource folder
        backgroundImage = new ImageIcon(getClass().getResource("/Hospital/icons/store.jpg")).getImage();

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Appointment ID", "Doctor ID", "Advice", "Prescription", "Pet Category", "Type", "Date"});

        table = new JTable(model);
        // Set table background transparent so image is visible behind empty areas
        table.setOpaque(false);
        table.setForeground(Color.BLACK);  // Text color for visibility

        JScrollPane scrollPane = new JScrollPane(table);
        // Make scroll pane viewport transparent so background image shows through
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        // JPanel with overridden paintComponent to draw background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout(10,10));
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
        backgroundPanel.setOpaque(false);

        setContentPane(backgroundPanel);

        loadMedicalHistory();

        setVisible(true);
    }

    private void loadMedicalHistory() {
        try (ConnectionClass cc = new ConnectionClass()) {
            String sql = "SELECT appointment_id, doctor_id, advice, prescription, pet_category, type, record_date FROM medical_records WHERE patient_id = ? ORDER BY record_date DESC";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, patientId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String rawAdvice = rs.getString("advice");
                String advice = rawAdvice != null ? rawAdvice.replace("Write advice here...", "").trim() : "";

                String rawPrescription = rs.getString("prescription");
                String prescription = rawPrescription != null ? rawPrescription.replace("Write prescription here...", "").trim() : "";

                model.addRow(new Object[]{
                        rs.getInt("appointment_id"),
                        rs.getString("doctor_id"),
                        advice,
                        prescription,
                        rs.getString("pet_category"),
                        rs.getString("type"),
                        rs.getDate("record_date")
                });
            }

            pst.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load medical history.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String testPatientId = JOptionPane.showInputDialog("Enter Patient ID to test:");
            if (testPatientId != null && !testPatientId.trim().isEmpty()) {
                new MedicalHistoryPage(testPatientId.trim());
            }
        });
    }
}
