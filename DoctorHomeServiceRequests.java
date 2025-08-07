package Hospital;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import java.util.Vector;

public class DoctorHomeServiceRequests extends JFrame implements ActionListener {
    private String doctorId;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAccept, btnDecline;

    public DoctorHomeServiceRequests(String doctorId) {
        this.doctorId = doctorId;

        setTitle("Home Service Requests - Doctor: " + doctorId);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Background panel
        BackgroundPanel bgPanel = new BackgroundPanel("/Hospital/icons/home service.jpg");
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel); // Set it as the content pane

        // Table columns
        String[] columns = {"Appointment ID", "Patient ID", "Date", "Time", "Pet Category", "Fee", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        table.setOpaque(false);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);

        bgPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false); // Transparent background
        btnAccept = new JButton("Accept");
        btnDecline = new JButton("Decline");
        btnAccept.addActionListener(this);
        btnDecline.addActionListener(this);
        btnPanel.add(btnAccept);
        btnPanel.add(btnDecline);

        bgPanel.add(btnPanel, BorderLayout.SOUTH);

        loadHomeServiceRequests();

        setVisible(true);
    }

    private void loadHomeServiceRequests() {
        model.setRowCount(0);

        try (ConnectionClass cc = new ConnectionClass()) {
            String sql = "SELECT appointment_id, patient_id, appointment_date, appointment_time, pet_category, final_fee, status " +
                    "FROM appointment " +
                    "WHERE doctor_id = ? AND type = 'home_service' AND status = 'pending'";

            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, doctorId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("appointment_id"));
                row.add(rs.getString("patient_id"));
                row.add(rs.getDate("appointment_date"));
                row.add(rs.getTime("appointment_time"));
                row.add(rs.getString("pet_category"));
                row.add(rs.getDouble("final_fee"));
                row.add(rs.getString("status"));

                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading home service requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int appointmentId = (int) model.getValueAt(selectedRow, 0);

        if (e.getSource() == btnAccept) {
            updateAppointmentStatus(appointmentId, "accepted");
        } else if (e.getSource() == btnDecline) {
            updateAppointmentStatus(appointmentId, "declined");
        }
    }

    private void updateAppointmentStatus(int appointmentId, String newStatus) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + newStatus + " this appointment?",
                "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (ConnectionClass cc = new ConnectionClass()) {
            String sql = "UPDATE appointment SET status = ? WHERE appointment_id = ?";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, newStatus);
            pst.setInt(2, appointmentId);
            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Appointment " + newStatus + " successfully.");
                loadHomeServiceRequests(); // refresh list
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update appointment status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating appointment status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // For testing
    public static void main(String[] args) {
        new DoctorHomeServiceRequests("D001");
    }

    // Custom JPanel for background image
    class BackgroundPanel extends JPanel {
        private Image bgImage;

        public BackgroundPanel(String imagePath) {
            try {
                URL bgUrl = getClass().getResource(imagePath);
                if (bgUrl != null) {
                    ImageIcon icon = new ImageIcon(bgUrl);
                    bgImage = icon.getImage();
                } else {
                    System.err.println("Image not found: " + imagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
