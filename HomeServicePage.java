package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Vector;
import java.util.Date;

import org.jdatepicker.impl.*;

public class HomeServicePage extends JFrame implements ActionListener {
    private String patientId;

    private JComboBox<String> doctorCombo;
    private JComboBox<String> timeCombo;
    private JComboBox<String> petCategoryCombo;
    private JDatePickerImpl datePicker;
    private JButton btnBook;
    private JLabel lblFee;

    private int extraFee = 1000;

    private Image backgroundImage;

    public HomeServicePage(String patientId) {
        this.patientId = patientId;

        backgroundImage = new ImageIcon(getClass().getResource("/Hospital/icons/home.jpg")).getImage();

        setTitle("Home Service Appointment Booking");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new GridLayout(7, 2, 10, 10));
        setContentPane(backgroundPanel);

        JLabel lblDoctor = new JLabel("Select Doctor:");
        lblDoctor.setForeground(Color.WHITE);
        backgroundPanel.add(lblDoctor);

        doctorCombo = new JComboBox<>();
        doctorCombo.setOpaque(false);
        doctorCombo.setForeground(Color.BLACK);
        loadDoctors();
        backgroundPanel.add(doctorCombo);

        JLabel lblDate = new JLabel("Select Date (yyyy-MM-dd):");
        lblDate.setForeground(Color.WHITE);
        backgroundPanel.add(lblDate);

        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        backgroundPanel.add(datePicker);

        JLabel lblTime = new JLabel("Select Time:");
        lblTime.setForeground(Color.WHITE);
        backgroundPanel.add(lblTime);

        timeCombo = new JComboBox<>(new String[]{
                "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"
        });
        timeCombo.setOpaque(false);
        timeCombo.setForeground(Color.BLACK);
        backgroundPanel.add(timeCombo);

        JLabel lblPetCategory = new JLabel("Pet Category:");
        lblPetCategory.setForeground(Color.WHITE);
        backgroundPanel.add(lblPetCategory);

        petCategoryCombo = new JComboBox<>(new String[]{
                "Dog", "Cat", "Bird", "Cow", "Goat","Other"
        });
        petCategoryCombo.setOpaque(false);
        petCategoryCombo.setForeground(Color.BLACK);
        backgroundPanel.add(petCategoryCombo);

        JLabel lblTotalFee = new JLabel("Total Fee (Taka):");
        lblTotalFee.setForeground(Color.WHITE);
        backgroundPanel.add(lblTotalFee);

        lblFee = new JLabel();
        lblFee.setForeground(Color.WHITE);
        backgroundPanel.add(lblFee);

        btnBook = new JButton("Book Home Service");
        btnBook.addActionListener(this);
        backgroundPanel.add(new JLabel());
        backgroundPanel.add(btnBook);

        doctorCombo.addActionListener(e -> updateFee());
        updateFee();

        setResizable(false);
        setVisible(true);
    }

    private void loadDoctors() {
        try (ConnectionClass cc = new ConnectionClass()) {
            String sql = "SELECT doctor_id, name, fees FROM doctor";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            Vector<String> doctors = new Vector<>();
            while (rs.next()) {
                String doctorId = rs.getString("doctor_id");
                String name = rs.getString("name");
                int fee = rs.getInt("fees");
                doctors.add(doctorId + " - " + name + " (Fee: " + fee + ")");
            }
            doctorCombo.setModel(new DefaultComboBoxModel<>(doctors));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctors.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFee() {
        try (ConnectionClass cc = new ConnectionClass()) {
            String selected = (String) doctorCombo.getSelectedItem();
            if (selected == null) {
                lblFee.setText("");
                return;
            }
            String doctorId = selected.split(" - ")[0];

            String sql = "SELECT fees FROM doctor WHERE doctor_id = ?";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, doctorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int fee = rs.getInt("fees");
                int totalFee = fee + extraFee;
                lblFee.setText(String.valueOf(totalFee));
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblFee.setText("N/A");
        }
    }

    private boolean isDoctorAvailable(String doctorId, String date, String formattedTime) {
        try (ConnectionClass cc = new ConnectionClass()) {
            SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
            String time24 = outFormat.format(inFormat.parse(formattedTime));

            String sql = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status != 'completed'";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, doctorId);
            pst.setString(2, date);
            pst.setString(3, time24);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBook) {
            String selected = (String) doctorCombo.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a doctor.");
                return;
            }
            String doctorId = selected.split(" - ")[0];

            Date selectedDate = (Date) datePicker.getModel().getValue();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a date.");
                return;
            }

            String date = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
            String amPmTime = (String) timeCombo.getSelectedItem();

            String petCategory = (String) petCategoryCombo.getSelectedItem();

            if (!isDoctorAvailable(doctorId, date, amPmTime)) {
                JOptionPane.showMessageDialog(this, "Doctor is not available at this date and time. Please choose another time.");
                return;
            }

            try (ConnectionClass cc = new ConnectionClass()) {
                SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
                String time24 = outFormat.format(inFormat.parse(amPmTime));

                String sql = "INSERT INTO appointment(patient_id, doctor_id, appointment_date, appointment_time, type, final_fee, status, reminder, pet_category) " +
                        "VALUES (?, ?, ?, ?, 'home_service', ?, 'pending', 0, ?)";
                PreparedStatement pst = cc.con.prepareStatement(sql);
                pst.setString(1, patientId);
                pst.setString(2, doctorId);
                pst.setString(3, date);
                pst.setString(4, time24);

                int fee = 0;
                try {
                    fee = Integer.parseInt(lblFee.getText());
                } catch (NumberFormatException ex) {
                    fee = extraFee;
                }
                pst.setInt(5, fee);
                pst.setString(6, petCategory);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Home service appointment booked successfully!\nTotal fee: " + fee + " taka.");
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error booking appointment. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeServicePage("P123"));
    }
}
