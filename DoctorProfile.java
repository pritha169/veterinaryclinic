package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.net.URL;

public class DoctorProfile extends JFrame implements ActionListener {
    private String doctorId;
    private JTextField txtName, txtEmail, txtPhone, txtSpecialization, txtCity;
    private JPasswordField txtPassword;
    private JButton btnSave, btnBack;

    public DoctorProfile(String doctorId) {
        this.doctorId = doctorId;

        setTitle("My Profile - Doctor ID: " + doctorId);
        setSize(420, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load background image from resources
        URL bgUrl = getClass().getResource("/Hospital/icons/profile.jpg"); // Place your image here
        Image backgroundImage = (bgUrl != null) ? new ImageIcon(bgUrl).getImage() : null;
        if (backgroundImage == null) {
            System.out.println("Background image not found!");
        }

        // Custom JPanel to paint background image
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        backgroundPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        backgroundPanel.add(txtName, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        backgroundPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        backgroundPanel.add(txtEmail, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 2;
        backgroundPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        backgroundPanel.add(txtPhone, gbc);

        // Specialization
        gbc.gridx = 0; gbc.gridy = 3;
        backgroundPanel.add(new JLabel("Specialization:"), gbc);
        gbc.gridx = 1;
        txtSpecialization = new JTextField(20);
        backgroundPanel.add(txtSpecialization, gbc);

        // City
        gbc.gridx = 0; gbc.gridy = 4;
        backgroundPanel.add(new JLabel("City:"), gbc);
        gbc.gridx = 1;
        txtCity = new JTextField(20);
        backgroundPanel.add(txtCity, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 5;
        backgroundPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        backgroundPanel.add(txtPassword, gbc);

        // Save Button
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSave = new JButton("Save Profile");
        btnSave.addActionListener(this);
        backgroundPanel.add(btnSave, gbc);

        // Back Button
        gbc.gridy = 7;
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        backgroundPanel.add(btnBack, gbc);

        loadDoctorProfile();
        setVisible(true);
    }

    private void loadDoctorProfile() {
        try (ConnectionClass cc = new ConnectionClass()) {
            String sql = "SELECT name, email, phone, specialization, city, password FROM doctor WHERE doctor_id = ?";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, doctorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("name"));
                txtEmail.setText(rs.getString("email"));
                txtPhone.setText(rs.getString("phone"));
                txtSpecialization.setText(rs.getString("specialization"));
                txtCity.setText(rs.getString("city"));
                txtPassword.setText(rs.getString("password"));
            } else {
                JOptionPane.showMessageDialog(this, "No profile data found.");
            }

            rs.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading profile.");
        }
    }

    private void saveDoctorProfile() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String specialization = txtSpecialization.getText().trim();
        String city = txtCity.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                specialization.isEmpty() || city.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        try (ConnectionClass cc = new ConnectionClass()) {
            String sql = "UPDATE doctor SET name = ?, email = ?, phone = ?, specialization = ?, city = ?, password = ? WHERE doctor_id = ?";
            PreparedStatement pst = cc.con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, specialization);
            pst.setString(5, city);
            pst.setString(6, password);
            pst.setString(7, doctorId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }

            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating profile.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            saveDoctorProfile();
        } else if (e.getSource() == btnBack) {
            dispose();
            new DoctorDashboard(doctorId);
        }
    }
}
