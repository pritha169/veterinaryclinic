package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PatientLoginPage extends JFrame implements ActionListener {
    JLabel nameLabel, passLabel;
    JTextField nameField;
    JPasswordField passField;
    JButton loginBtn, backBtn;
    JLabel bgLabel;

    public PatientLoginPage() {
        setTitle("Patient Login");
        setSize(400, 300);
        setLocation(450, 200);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ Set background image
        ImageIcon bgIcon = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/patient_sign.jpg"));
        Image bgImage = bgIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        bgLabel = new JLabel(new ImageIcon(bgImage));
        bgLabel.setBounds(0, 0, 400, 300);

        // ✅ Create a panel for components
        JPanel contentPanel = new JPanel();
        contentPanel.setBounds(0, 0, 400, 300);
        contentPanel.setOpaque(false); // transparent panel over image
        contentPanel.setLayout(null);

        JLabel heading = new JLabel("Patient Login");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBounds(140, 30, 200, 30);
        contentPanel.add(heading);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 100, 120, 25);
        contentPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(170, 100, 150, 25);
        contentPanel.add(nameField);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 140, 120, 25);
        contentPanel.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(170, 140, 150, 25);
        contentPanel.add(passField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 200, 90, 35);
        loginBtn.addActionListener(this);
        contentPanel.add(loginBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(220, 200, 90, 35);
        backBtn.addActionListener(this);
        contentPanel.add(backBtn);

        add(contentPanel);
        add(bgLabel); // Add background last so it's at the bottom

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String name = nameField.getText().trim();
            String pass = new String(passField.getPassword());

            if (name.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter name and password");
                return;
            }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/hospital_1",
                        "root",
                        "Pritha123!"
                );

                String query = "SELECT * FROM patient WHERE name = ? AND password = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, pass);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String patientId = rs.getString("patient_id");
                    JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + rs.getString("name"));
                    setVisible(false);
                    new PatientDashboard(patientId);  // Launch Dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid name or password.");
                }

                con.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new Index(); // ✅ Redirects back to main Index page
        }
    }

    public static void main(String[] args) {
        new PatientLoginPage();
    }
}
