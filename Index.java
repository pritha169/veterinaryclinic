package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Index extends JFrame implements ActionListener {
    JFrame f;
    JLabel l1, l2, l3;
    JButton btnGuest, btnUser;
    JButton btnDoctor, btnPatient, btnAdmin, btnBack;

    Index() {
        f = new JFrame("Index Page");
        f.setBackground(Color.WHITE);
        f.setLayout(null);

        l1 = new JLabel();
        l1.setBounds(0, 0, 800, 570);
        l1.setLayout(null);

        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/first page1.jpg"));
        Image i1 = img.getImage().getScaledInstance(800, 570, Image.SCALE_SMOOTH);
        ImageIcon img1 = new ImageIcon(i1);
        l1.setIcon(img1);

        l2 = new JLabel("Vet Clinic");
        l2.setBounds(70, 315, 500, 30);
        l2.setFont(new Font("Arial", Font.BOLD, 30));
        l2.setForeground(Color.BLACK);
        l1.add(l2);

        l3 = new JLabel("Get Best Service From Us");
        l3.setBounds(70, 350, 500, 30);
        l3.setFont(new Font("Arial", Font.BOLD, 13));
        l3.setForeground(Color.BLACK);
        l1.add(l3);

        // Initial Buttons
        btnGuest = new JButton("Guest Mode");
        btnGuest.setBounds(100, 400, 150, 40);
        btnGuest.setBackground(Color.WHITE);
        btnGuest.setForeground(Color.BLACK);
        btnGuest.addActionListener(this);
        l1.add(btnGuest);

        btnUser = new JButton("User Mode");
        btnUser.setBounds(270, 400, 150, 40);
        btnUser.setBackground(Color.WHITE);
        btnUser.setForeground(Color.BLACK);
        btnUser.addActionListener(this);
        l1.add(btnUser);

        // User Role Buttons (Initially hidden)
        btnDoctor = new JButton("Doctor");
        btnDoctor.setBounds(100, 400, 150, 40);
        btnDoctor.setBackground(Color.WHITE);
        btnDoctor.setForeground(Color.BLACK);
        btnDoctor.addActionListener(this);
        btnDoctor.setVisible(false);
        l1.add(btnDoctor);

        btnPatient = new JButton("Patient");
        btnPatient.setBounds(270, 400, 150, 40);
        btnPatient.setBackground(Color.WHITE);
        btnPatient.setForeground(Color.BLACK);
        btnPatient.addActionListener(this);
        btnPatient.setVisible(false);
        l1.add(btnPatient);

        btnAdmin = new JButton("Admin");
        btnAdmin.setBounds(185, 450, 150, 40);
        btnAdmin.setBackground(Color.WHITE);
        btnAdmin.setForeground(Color.BLACK);
        btnAdmin.addActionListener(this);
        btnAdmin.setVisible(false);
        l1.add(btnAdmin);

        btnBack = new JButton("Back");
        btnBack.setBounds(10, 10, 80, 30);
        btnBack.setBackground(Color.DARK_GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        btnBack.setVisible(false);
        l1.add(btnBack);

        f.add(l1);
        f.setSize(800, 570);
        f.setLocation(300, 100);
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();

        if (src == btnGuest) {
            f.setVisible(false);
            new GuestPage(); // Create a new class called GuestPage
        } else if (src == btnUser) {
            showUserModeOptions();
        } else if (src == btnBack) {
            hideUserModeOptions();
        } else if (src == btnDoctor) {
            f.setVisible(false);
            new DoctorLoginPage(); // Separate doctor login page
        } else if (src == btnPatient) {
            f.setVisible(false);
            new PatientAccessPage(); // Patient Signup/Login
        } else if (src == btnAdmin) {
            f.setVisible(false);
            new AdminLoginPage(); // Separate admin login page
        }
    }

    private void showUserModeOptions() {
        btnGuest.setVisible(false);
        btnUser.setVisible(false);
        btnDoctor.setVisible(true);
        btnPatient.setVisible(true);
        btnAdmin.setVisible(true);
        btnBack.setVisible(true);
    }

    private void hideUserModeOptions() {
        btnGuest.setVisible(true);
        btnUser.setVisible(true);
        btnDoctor.setVisible(false);
        btnPatient.setVisible(false);
        btnAdmin.setVisible(false);
        btnBack.setVisible(false);
    }

    public static void main(String[] args) {
        new Index();
    }
}
