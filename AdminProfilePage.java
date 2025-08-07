package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminProfilePage extends JFrame implements ActionListener {

    JTextField tfAdminId, tfUsername, tfName, tfAge, tfPhone, tfCity, tfEmail;
    JComboBox<String> genderBox;
    JPasswordField pfPassword;
    JButton btnUpdate, btnBack;
    JLabel background;

    public AdminProfilePage() {
        setTitle("Admin Profile");
        setSize(450, 500);
        setLocation(350, 150);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load background image
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/admin profile.jpg"));
        Image i1 = img.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        ImageIcon img2 = new ImageIcon(i1);
        background = new JLabel(img2);
        background.setBounds(0, 0, 450, 500);
        setContentPane(background);
        background.setLayout(null);

        JLabel heading = new JLabel("Admin Profile");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(Color.WHITE); // adjust for visibility
        heading.setBounds(140, 10, 200, 30);
        background.add(heading);

        JLabel lblAdminId = new JLabel("Admin ID:");
        lblAdminId.setBounds(50, 60, 100, 25);
        lblAdminId.setForeground(Color.WHITE);
        background.add(lblAdminId);

        tfAdminId = new JTextField();
        tfAdminId.setBounds(160, 60, 200, 25);
        tfAdminId.setEditable(false);
        background.add(tfAdminId);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 100, 100, 25);
        lblUsername.setForeground(Color.WHITE);
        background.add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(160, 100, 200, 25);
        background.add(tfUsername);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 140, 100, 25);
        lblName.setForeground(Color.WHITE);
        background.add(lblName);

        tfName = new JTextField();
        tfName.setBounds(160, 140, 200, 25);
        background.add(tfName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setBounds(50, 180, 100, 25);
        lblAge.setForeground(Color.WHITE);
        background.add(lblAge);

        tfAge = new JTextField();
        tfAge.setBounds(160, 180, 200, 25);
        background.add(tfAge);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(50, 220, 100, 25);
        lblPhone.setForeground(Color.WHITE);
        background.add(lblPhone);

        tfPhone = new JTextField();
        tfPhone.setBounds(160, 220, 200, 25);
        background.add(tfPhone);

        JLabel lblCity = new JLabel("City:");
        lblCity.setBounds(50, 260, 100, 25);
        lblCity.setForeground(Color.WHITE);
        background.add(lblCity);

        tfCity = new JTextField();
        tfCity.setBounds(160, 260, 200, 25);
        background.add(tfCity);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 300, 100, 25);
        lblEmail.setForeground(Color.WHITE);
        background.add(lblEmail);

        tfEmail = new JTextField();
        tfEmail.setBounds(160, 300, 200, 25);
        background.add(tfEmail);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(50, 340, 100, 25);
        lblGender.setForeground(Color.WHITE);
        background.add(lblGender);

        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setBounds(160, 340, 200, 25);
        background.add(genderBox);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 380, 100, 25);
        lblPassword.setForeground(Color.WHITE);
        background.add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(160, 380, 200, 25);
        background.add(pfPassword);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(90, 420, 100, 30);
        btnUpdate.setBackground(Color.BLACK);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(this);
        background.add(btnUpdate);

        btnBack = new JButton("Back");
        btnBack.setBounds(230, 420, 100, 30);
        btnBack.setBackground(Color.BLACK);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        background.add(btnBack);

        // Load admin data for hardcoded ID here
        loadAdminData("558574");

        setVisible(true);
    }

    private void loadAdminData(String adminId) {
        try {
            ConnectionClass obj = new ConnectionClass();
            String query = "SELECT * FROM admin WHERE admin_id = ?";
            PreparedStatement pst = obj.con.prepareStatement(query);
            pst.setString(1, adminId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                tfAdminId.setText(rs.getString("admin_id"));
                tfUsername.setText(rs.getString("username"));
                tfName.setText(rs.getString("name"));
                tfAge.setText(rs.getString("age"));
                tfPhone.setText(rs.getString("phone"));
                tfCity.setText(rs.getString("city"));
                tfEmail.setText(rs.getString("email"));
                String gender = rs.getString("gender");
                genderBox.setSelectedItem(gender != null ? gender : "Male");
                pfPassword.setText(rs.getString("password"));
            }

            rs.close();
            pst.close();
            obj.con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnUpdate) {
            String username = tfUsername.getText().trim();
            String name = tfName.getText().trim();
            String age = tfAge.getText().trim();
            String phone = tfPhone.getText().trim();
            String city = tfCity.getText().trim();
            String email = tfEmail.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            String password = new String(pfPassword.getPassword());

            if (username.isEmpty() || name.isEmpty() || age.isEmpty() || phone.isEmpty() || city.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields.");
                return;
            }

            try {
                ConnectionClass obj = new ConnectionClass();
                String query = "UPDATE admin SET username=?, name=?, age=?, phone=?, city=?, email=?, gender=?, password=? WHERE admin_id=?";
                PreparedStatement pst = obj.con.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, name);
                pst.setString(3, age);
                pst.setString(4, phone);
                pst.setString(5, city);
                pst.setString(6, email);
                pst.setString(7, gender);
                pst.setString(8, password);
                pst.setString(9, tfAdminId.getText());

                int updated = pst.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(null, "Profile updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed.");
                }

                pst.close();
                obj.con.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error updating profile: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (ae.getSource() == btnBack) {
            setVisible(false);
            // Optional: new AdminHomePage();
        }
    }

    public static void main(String[] args) {
        new AdminProfilePage();
    }
}
