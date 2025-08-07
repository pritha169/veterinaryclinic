package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class DoctorDashboard extends JFrame implements ActionListener {
    private String doctorId;
    private JButton btnViewProfile, btnViewAppointments, btnHomeServiceRequests, btnLogout;

    public DoctorDashboard(String doctorId) {
        this.doctorId = doctorId;

        setTitle("Doctor Dashboard - ID: " + doctorId);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load background image from resources
        URL bgUrl = getClass().getResource("/Hospital/icons/doctor panel.jpg"); // Make sure image is placed here
        Image backgroundImage = (bgUrl != null) ? new ImageIcon(bgUrl).getImage() : null;
        if (backgroundImage == null) {
            System.out.println("Background image not found!");
        }

        // Custom JPanel to paint the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new GridLayout(5, 1, 20, 20));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setContentPane(backgroundPanel);

        // Add heading label
        JLabel heading = new JLabel("Welcome Doctor " + doctorId, JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(Color.BLACK);  // Change if your background is dark, e.g., Color.WHITE
        backgroundPanel.add(heading);

        btnViewProfile = new JButton("View Profile");
        btnViewProfile.addActionListener(this);
        backgroundPanel.add(btnViewProfile);

        btnViewAppointments = new JButton("View Upcoming Appointments");
        btnViewAppointments.addActionListener(this);
        backgroundPanel.add(btnViewAppointments);

        btnHomeServiceRequests = new JButton("View Home Service Requests");
        btnHomeServiceRequests.addActionListener(this);
        backgroundPanel.add(btnHomeServiceRequests);

        btnLogout = new JButton("Logout");
        btnLogout.addActionListener(this);
        backgroundPanel.add(btnLogout);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnViewProfile) {
            new DoctorProfile(doctorId);
        } else if (e.getSource() == btnViewAppointments) {
            new DoctorAppointments(doctorId);
        } else if (e.getSource() == btnHomeServiceRequests) {
            new DoctorHomeServiceRequests(doctorId);
        } else if (e.getSource() == btnLogout) {
            dispose();
            new LoginPage();
        }
    }

    // For testing
    public static void main(String[] args) {
        new DoctorDashboard("D001");
    }
}
