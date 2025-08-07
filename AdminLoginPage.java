package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminLoginPage extends JFrame implements ActionListener {

    JTextField tfUsername;
    JPasswordField pfPassword;
    JButton btnLogin, btnBack;
    JLabel background;

    public AdminLoginPage() {
        setTitle("Admin Login");
        setSize(500, 300);
        setLocation(300, 100);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Background
        background = new JLabel();
        background.setBounds(0, 0, 500, 300);
        background.setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/admin login.jpg"));
        Image i = img.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        background.setIcon(new ImageIcon(i));

        // Title
        JLabel lblTitle = new JLabel("Admin Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(170, 10, 200, 30);
        lblTitle.setForeground(Color.BLACK);
        background.add(lblTitle);

        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(100, 70, 100, 25);
        lblUsername.setForeground(Color.BLACK);
        background.add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(200, 70, 180, 25);
        background.add(tfUsername);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(100, 110, 100, 25);
        lblPassword.setForeground(Color.BLACK);
        background.add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(200, 110, 180, 25);
        background.add(pfPassword);

        // Buttons
        btnLogin = new JButton("Login");
        btnLogin.setBounds(120, 170, 100, 30);
        btnLogin.setBackground(Color.BLACK);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(this);
        background.add(btnLogin);

        btnBack = new JButton("Back");
        btnBack.setBounds(240, 170, 100, 30);
        btnBack.setBackground(Color.BLACK);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        background.add(btnBack);

        add(background);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnLogin) {
            String username = tfUsername.getText();
            String password = new String(pfPassword.getPassword());

            try {
                ConnectionClass obj = new ConnectionClass();
                String query = "SELECT * FROM admin WHERE username=? AND password=?";
                PreparedStatement ps = obj.con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    new AdminHomePage();
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid admin credentials!");
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
        new AdminLoginPage();
    }
}
