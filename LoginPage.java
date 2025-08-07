package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener {

    JTextField tf;
    JPasswordField pf;
    JButton bt1, bt2;

    public LoginPage() {
        setTitle("Login Page");
        setSize(500, 350);
        setLocation(300, 100);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Background image
        JLabel background = new JLabel();
        background.setBounds(0, 0, 500, 350);
        background.setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/admin login.jpg"));
        Image i = img.getImage().getScaledInstance(500, 350, Image.SCALE_SMOOTH);
        background.setIcon(new ImageIcon(i));

        // Title
        JLabel l2 = new JLabel("Login Page");
        l2.setBounds(170, 10, 200, 40);
        l2.setFont(new Font("Arial", Font.BOLD, 24));
        l2.setForeground(Color.BLACK);
        background.add(l2);

        // Username
        JLabel l3 = new JLabel("Username/Email:");
        l3.setBounds(100, 80, 130, 30);
        l3.setFont(new Font("Arial", Font.BOLD, 14));
        l3.setForeground(Color.BLACK);
        background.add(l3);

        tf = new JTextField();
        tf.setBounds(230, 80, 180, 30);
        background.add(tf);

        // Password
        JLabel l4 = new JLabel("Password:");
        l4.setBounds(100, 130, 100, 30);
        l4.setFont(new Font("Arial", Font.BOLD, 14));
        l4.setForeground(Color.BLACK);
        background.add(l4);

        pf = new JPasswordField();
        pf.setBounds(230, 130, 180, 30);
        background.add(pf);

        // Buttons
        bt1 = new JButton("Login");
        bt1.setBounds(120, 200, 100, 40);
        bt1.setBackground(Color.BLACK);
        bt1.setForeground(Color.WHITE);
        background.add(bt1);

        bt2 = new JButton("Back");
        bt2.setBounds(240, 200, 100, 40);
        bt2.setBackground(Color.BLACK);
        bt2.setForeground(Color.WHITE);
        background.add(bt2);

        bt1.addActionListener(this);
        bt2.addActionListener(this);

        add(background);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bt1) {
            try {
                ConnectionClass obj = new ConnectionClass();
                String name = tf.getText();
                String pass = pf.getText();

                // First check admin table
                String q1 = "SELECT * FROM admin WHERE username=? AND password=?";
                PreparedStatement ps1 = obj.con.prepareStatement(q1);
                ps1.setString(1, name);
                ps1.setString(2, pass);
                ResultSet rs1 = ps1.executeQuery();

                if (rs1.next()) {
                    new AdminHomePage(); // Create or connect to your Admin page
                    setVisible(false);
                } else {
                    String q2 = "SELECT * FROM doctor WHERE (name=? OR email=?) AND password=?";
                    PreparedStatement ps2 = obj.con.prepareStatement(q2);
                    ps2.setString(1, name);
                    ps2.setString(2, name);
                    ps2.setString(3, pass);

                    ResultSet rs2 = ps2.executeQuery();

                    if (rs2.next()) {
                        String doctorId = rs2.getString("doctor_id");  // still needed to track doctor uniquely
                        new DoctorDashboard(doctorId);

                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong username/email or password");
                        setVisible(false);
                        new LoginPage();
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (ae.getSource() == bt2) {
            setVisible(false);
            new Index();  // Go back to main page
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
