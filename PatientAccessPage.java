package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PatientAccessPage extends JFrame implements ActionListener {
    JButton signupBtn, loginBtn;

    public PatientAccessPage() {
        setTitle("Patient Access");
        setSize(400, 250);
        setLocation(450, 200);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Background image
        JLabel background = new JLabel();
        background.setBounds(0, 0, 400, 250);
        background.setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/admin login.jpg"));
        Image i = img.getImage().getScaledInstance(400, 250, Image.SCALE_SMOOTH);
        background.setIcon(new ImageIcon(i));

        JLabel heading = new JLabel("Welcome Patient");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setBounds(110, 30, 200, 30);
        heading.setForeground(Color.BLACK);
        background.add(heading);

        signupBtn = new JButton("Signup");
        signupBtn.setBounds(90, 100, 100, 40);
        background.add(signupBtn);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(210, 100, 100, 40);
        background.add(loginBtn);

        signupBtn.addActionListener(this);
        loginBtn.addActionListener(this);

        add(background);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupBtn) {
            setVisible(false);
            new PatientSignupPage();
        } else if (e.getSource() == loginBtn) {
            setVisible(false);
            new PatientLoginPage();
        }
    }

    public static void main(String[] args) {
        new PatientAccessPage();
    }
}
