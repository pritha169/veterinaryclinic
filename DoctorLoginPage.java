package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DoctorLoginPage extends JFrame implements ActionListener {

    JTextField tfDoctorId, tfUsername;
    JPasswordField pfPassword;
    JButton btnLogin, btnBack;

    public DoctorLoginPage() {
        setTitle("Doctor Login");
        setSize(500, 350);
        setLocation(300, 100);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel background = new JLabel();
        background.setBounds(0, 0, 500, 350);
        background.setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/admin login.jpg"));
        Image i = img.getImage().getScaledInstance(500, 350, Image.SCALE_SMOOTH);
        background.setIcon(new ImageIcon(i));

        JLabel lblTitle = new JLabel("Doctor Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(170, 20, 200, 30);
        lblTitle.setForeground(Color.BLACK);
        background.add(lblTitle);

        JLabel lblDoctorId = new JLabel("Doctor ID:");
        lblDoctorId.setBounds(100, 70, 100, 25);
        lblDoctorId.setForeground(Color.BLACK);
        background.add(lblDoctorId);

        tfDoctorId = new JTextField();
        tfDoctorId.setBounds(230, 70, 180, 25);
        background.add(tfDoctorId);

        JLabel lblUsername = new JLabel("Username/Email:");
        lblUsername.setBounds(100, 110, 130, 25);
        lblUsername.setForeground(Color.BLACK);
        background.add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(230, 110, 180, 25);
        background.add(tfUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(100, 150, 100, 25);
        lblPassword.setForeground(Color.BLACK);
        background.add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(230, 150, 180, 25);
        background.add(pfPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(120, 220, 100, 30);
        btnLogin.setBackground(Color.BLACK);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(this);
        background.add(btnLogin);

        btnBack = new JButton("Back");
        btnBack.setBounds(240, 220, 100, 30);
        btnBack.setBackground(Color.BLACK);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        background.add(btnBack);

        add(background);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnLogin) {
            String doctorId = tfDoctorId.getText();
            String username = tfUsername.getText();
            String password = new String(pfPassword.getPassword());

            try {
                ConnectionClass obj = new ConnectionClass();
                String query = "SELECT * FROM doctor WHERE doctor_id=? AND (name=? OR email=?) AND password=?";
                PreparedStatement ps = obj.con.prepareStatement(query);
                ps.setString(1, doctorId);
                ps.setString(2, username);
                ps.setString(3, username);
                ps.setString(4, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    new DoctorDashboard(doctorId); // open dashboard with ID
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid doctor ID or credentials!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (ae.getSource() == btnBack) {
            setVisible(false);
            new Index();
        }
    }

    public static void main(String[] args) {
        new DoctorLoginPage();
    }
}
