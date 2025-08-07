package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;

public class PatientProfile extends JFrame implements ActionListener {

    private String patientId;
    private JTextField tfName, tfAge, tfGender, tfPhone, tfEmail, tfAddress, tfPassword;
    private JButton btnUpdate, btnBack;

    public PatientProfile(String patientId) {
        this.patientId = patientId;

        setTitle("Patient Profile - ID: " + patientId);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load background image from resources
        URL bgUrl = getClass().getResource("/Hospital/icons/profile.jpg");
        Image backgroundImage = (bgUrl != null) ? new ImageIcon(bgUrl).getImage() : null;
        if (backgroundImage == null) {
            System.out.println("Background image not found!");
        }

        // Create a JPanel with overridden paintComponent to draw background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(null); // use absolute positioning
        setContentPane(backgroundPanel);

        // Create and add components with black text color
        JLabel lblTitle = new JLabel("Patient Profile");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(170, 10, 200, 30);
        lblTitle.setForeground(Color.BLACK);
        backgroundPanel.add(lblTitle);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 60, 100, 25);
        lblName.setForeground(Color.BLACK);
        backgroundPanel.add(lblName);
        tfName = new JTextField();
        tfName.setBounds(150, 60, 250, 25);
        tfName.setForeground(Color.BLACK);
        backgroundPanel.add(tfName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setBounds(50, 100, 100, 25);
        lblAge.setForeground(Color.BLACK);
        backgroundPanel.add(lblAge);
        tfAge = new JTextField();
        tfAge.setBounds(150, 100, 250, 25);
        tfAge.setForeground(Color.BLACK);
        backgroundPanel.add(tfAge);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(50, 140, 100, 25);
        lblGender.setForeground(Color.BLACK);
        backgroundPanel.add(lblGender);
        tfGender = new JTextField();
        tfGender.setBounds(150, 140, 250, 25);
        tfGender.setForeground(Color.BLACK);
        backgroundPanel.add(tfGender);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(50, 180, 100, 25);
        lblPhone.setForeground(Color.BLACK);
        backgroundPanel.add(lblPhone);
        tfPhone = new JTextField();
        tfPhone.setBounds(150, 180, 250, 25);
        tfPhone.setForeground(Color.BLACK);
        backgroundPanel.add(tfPhone);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 220, 100, 25);
        lblEmail.setForeground(Color.BLACK);
        backgroundPanel.add(lblEmail);
        tfEmail = new JTextField();
        tfEmail.setBounds(150, 220, 250, 25);
        tfEmail.setForeground(Color.BLACK);
        backgroundPanel.add(tfEmail);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(50, 260, 100, 25);
        lblAddress.setForeground(Color.BLACK);
        backgroundPanel.add(lblAddress);
        tfAddress = new JTextField();
        tfAddress.setBounds(150, 260, 250, 25);
        tfAddress.setForeground(Color.BLACK);
        backgroundPanel.add(tfAddress);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 300, 100, 25);
        lblPassword.setForeground(Color.BLACK);
        backgroundPanel.add(lblPassword);
        tfPassword = new JTextField();
        tfPassword.setBounds(150, 300, 250, 25);
        tfPassword.setForeground(Color.BLACK);
        backgroundPanel.add(tfPassword);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(150, 340, 100, 30);
        btnUpdate.addActionListener(this);
        backgroundPanel.add(btnUpdate);

        btnBack = new JButton("Back");
        btnBack.setBounds(270, 340, 100, 30);
        btnBack.addActionListener(this);
        backgroundPanel.add(btnBack);

        loadPatientDetails();

        setVisible(true);
    }

    private void loadPatientDetails() {
        try {
            ConnectionClass cc = new ConnectionClass();
            String sql = "SELECT * FROM patient WHERE patient_id = ?";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, patientId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tfName.setText(rs.getString("name"));
                tfAge.setText(String.valueOf(rs.getInt("age")));
                tfGender.setText(rs.getString("gender"));
                tfPhone.setText(rs.getString("phone"));
                tfEmail.setText(rs.getString("email"));
                tfAddress.setText(rs.getString("address"));
                tfPassword.setText(rs.getString("password"));
            }
            cc.con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient details.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUpdate) {
            try {
                ConnectionClass cc = new ConnectionClass();
                String sql = "UPDATE patient SET name = ?, age = ?, gender = ?, phone = ?, email = ?, address = ?, password = ? WHERE patient_id = ?";
                PreparedStatement pst = cc.con.prepareStatement(sql);
                pst.setString(1, tfName.getText());
                pst.setInt(2, Integer.parseInt(tfAge.getText()));
                pst.setString(3, tfGender.getText());
                pst.setString(4, tfPhone.getText());
                pst.setString(5, tfEmail.getText());
                pst.setString(6, tfAddress.getText());
                pst.setString(7, tfPassword.getText());
                pst.setString(8, patientId);

                int updated = pst.executeUpdate();
                cc.con.close();

                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating profile.");
            }
        } else if (e.getSource() == btnBack) {
            dispose();
        }
    }
}
