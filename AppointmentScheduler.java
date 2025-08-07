package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.Properties;

import org.jdatepicker.impl.*;

public class AppointmentScheduler extends JFrame implements ActionListener {
    private String patientId;
    private JComboBox<String> doctorComboBox;
    private JDatePickerImpl datePicker;
    private JComboBox<String> timeComboBox;
    private JComboBox<String> petCategoryComboBox;
    private JComboBox<String> typeComboBox;
    private JCheckBox reminderCheckBox;
    private JCheckBox firstVisitCheckBox;
    private JButton btnSubmit, btnBack;
    private JTextField feeField;

    public AppointmentScheduler(String patientId) {
        this.patientId = patientId;
        setTitle("Appointment Scheduler");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(10, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon bgIcon = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/appointment.jpg"));
        JLabel background = new JLabel(bgIcon);
        setContentPane(background);
        background.setLayout(new GridLayout(10, 2, 5, 5));

        background.add(new JLabel("Select Doctor:"));
        doctorComboBox = new JComboBox<>();
        loadDoctors();
        background.add(doctorComboBox);

        background.add(new JLabel("Doctor Fee:"));
        feeField = new JTextField("Select a doctor");
        feeField.setEditable(false);
        background.add(feeField);

        background.add(new JLabel("Appointment Date:"));
        datePicker = createDatePicker();
        background.add(datePicker);

        background.add(new JLabel("Appointment Time:"));
        String[] times = {"09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM"};
        timeComboBox = new JComboBox<>(times);
        background.add(timeComboBox);

        background.add(new JLabel("Pet Category:"));
        petCategoryComboBox = new JComboBox<>(new String[]{"Dog", "Cat", "Bird", "Other"});
        background.add(petCategoryComboBox);

        background.add(new JLabel("Type:"));
        typeComboBox = new JComboBox<>(new String[]{"Regular", "Emergency"});
        background.add(typeComboBox);

        background.add(new JLabel("Set Reminder:"));
        reminderCheckBox = new JCheckBox();
        background.add(reminderCheckBox);

        background.add(new JLabel("First Time Visit:"));
        firstVisitCheckBox = new JCheckBox();
        background.add(firstVisitCheckBox);

        btnSubmit = new JButton("Book Appointment");
        btnSubmit.addActionListener(this);
        background.add(btnSubmit);

        btnBack = new JButton("Back");
        btnBack.addActionListener(e -> this.dispose());
        background.add(btnBack);

        doctorComboBox.addActionListener(e -> updateFeeField());
        updateFeeField();

        setVisible(true);
    }

    private void loadDoctors() {
        try (ConnectionClass cc = new ConnectionClass()) {
            PreparedStatement pst = cc.con.prepareStatement("SELECT name FROM doctor");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                doctorComboBox.addItem(rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + e.getMessage());
        }
    }

    private void updateFeeField() {
        String doctorName = (String) doctorComboBox.getSelectedItem();
        if (doctorName == null) {
            feeField.setText("Select a doctor");
            return;
        }

        try (ConnectionClass cc = new ConnectionClass()) {
            PreparedStatement pst = cc.con.prepareStatement("SELECT fees FROM doctor WHERE name = ?");
            pst.setString(1, doctorName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double fee = rs.getDouble("fees");
                feeField.setText("Fee: " + fee + " BDT");
            } else {
                feeField.setText("Doctor not found");
            }
        } catch (Exception e) {
            feeField.setText("Error loading fee");
        }
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String doctorName = (String) doctorComboBox.getSelectedItem();
        Date selectedDate = (Date) datePicker.getModel().getValue();
        String timeStr = (String) timeComboBox.getSelectedItem();
        String petCategory = (String) petCategoryComboBox.getSelectedItem();
        String type = (String) typeComboBox.getSelectedItem();
        boolean reminder = reminderCheckBox.isSelected();
        boolean firstVisit = firstVisitCheckBox.isSelected();

        if (doctorName == null || selectedDate == null || timeStr == null || petCategory == null || type == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate appointmentDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (appointmentDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Appointment date cannot be in the past", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prevent past time selection if today
        if (appointmentDate.isEqual(LocalDate.now())) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
                Date parsedTime = inputFormat.parse(timeStr);
                LocalTime selectedTime = parsedTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                LocalTime now = LocalTime.now();

                if (selectedTime.isBefore(now)) {
                    JOptionPane.showMessageDialog(this, "You cannot select a past time for today.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid time format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if ("Emergency".equalsIgnoreCase(type) && !appointmentDate.isEqual(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Emergency appointments must be for today.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");
            Date parsedTime = inputFormat.parse(timeStr);
            String time24 = outputFormat.format(parsedTime);

            try (ConnectionClass cc = new ConnectionClass()) {
                PreparedStatement pstCheckPatient = cc.con.prepareStatement("SELECT * FROM patient WHERE patient_id = ?");
                pstCheckPatient.setString(1, patientId);
                ResultSet rsCheckPatient = pstCheckPatient.executeQuery();
                if (!rsCheckPatient.next()) {
                    JOptionPane.showMessageDialog(this, "Patient ID not found. Please register first.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement pstDoctor = cc.con.prepareStatement("SELECT doctor_id, fees FROM doctor WHERE name = ?");
                pstDoctor.setString(1, doctorName);
                ResultSet rsDoctor = pstDoctor.executeQuery();

                String doctorId = null;
                double fees = 0;
                if (rsDoctor.next()) {
                    doctorId = rsDoctor.getString("doctor_id");
                    fees = rsDoctor.getDouble("fees");
                } else {
                    JOptionPane.showMessageDialog(this, "Doctor not found", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement pstCheck = cc.con.prepareStatement(
                        "SELECT * FROM appointment WHERE patient_id = ? AND appointment_date = ? AND appointment_time = ?");
                pstCheck.setString(1, patientId);
                pstCheck.setDate(2, java.sql.Date.valueOf(appointmentDate));
                pstCheck.setString(3, time24);
                ResultSet rsCheck = pstCheck.executeQuery();

                if (rsCheck.next()) {
                    JOptionPane.showMessageDialog(this, "You already have an appointment at this time.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if ("Emergency".equalsIgnoreCase(type)) {
                    PreparedStatement pstEmergency = cc.con.prepareStatement(
                            "SELECT COUNT(*) AS cnt FROM appointment WHERE doctor_id = ? AND appointment_date = ? AND is_emergency = TRUE");
                    pstEmergency.setString(1, doctorId);
                    pstEmergency.setDate(2, java.sql.Date.valueOf(appointmentDate));
                    ResultSet rsEmergency = pstEmergency.executeQuery();
                    int count = rsEmergency.next() ? rsEmergency.getInt("cnt") : 0;
                    if (count >= 3) {
                        JOptionPane.showMessageDialog(this, "Max emergency appointments reached for today.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                double finalFee = firstVisit ? fees : fees * 0.7;
                JOptionPane.showMessageDialog(this, (firstVisit ? "First time visit. " : "Returning visit. 30% discount. ") + "Fee: " + finalFee + " BDT");

                PreparedStatement pstInsert = cc.con.prepareStatement(
                        "INSERT INTO appointment (patient_id, doctor_id, appointment_date, appointment_time, pet_category, type, reminder, status, first_visit, final_fee, is_emergency) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, 'pending', ?, ?, ?)");
                pstInsert.setString(1, patientId);
                pstInsert.setString(2, doctorId);
                pstInsert.setDate(3, java.sql.Date.valueOf(appointmentDate));
                pstInsert.setString(4, time24);
                pstInsert.setString(5, petCategory);
                pstInsert.setString(6, type.toLowerCase());
                pstInsert.setBoolean(7, reminder);
                pstInsert.setBoolean(8, firstVisit);
                pstInsert.setDouble(9, finalFee);
                pstInsert.setBoolean(10, "Emergency".equalsIgnoreCase(type));

                int rows = pstInsert.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Booking failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (ParseException pe) {
            JOptionPane.showMessageDialog(this, "Invalid time format", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(this, "Database error: " + sqle.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
