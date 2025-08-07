package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteDoctorPage extends JFrame implements ActionListener {
    JTextField tfDoctorId;
    JButton deleteBtn, backBtn;
    JLabel background;

    public DeleteDoctorPage() {
        setTitle("Delete Doctor");
        setSize(400, 250);
        setLocation(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load background image
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/delete doc.jpg"));
        background = new JLabel(img);
        background.setBounds(0, 0, 400, 250);
        setContentPane(background);
        background.setLayout(null);

        // UI Components on top of background
        JLabel label = new JLabel("Enter Doctor ID to Delete:");
        label.setBounds(50, 30, 200, 30);
        background.add(label);

        tfDoctorId = new JTextField();
        tfDoctorId.setBounds(50, 70, 200, 30);
        background.add(tfDoctorId);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(50, 120, 80, 30);
        background.add(deleteBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(170, 120, 80, 30);
        background.add(backBtn);

        deleteBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteBtn) {
            String doctorId = tfDoctorId.getText().trim();

            if (doctorId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a Doctor ID.");
                return;
            }

            try {
                ConnectionClass obj = new ConnectionClass();

                String query = "DELETE FROM doctor WHERE doctor_id = ?";
                PreparedStatement pst = obj.con.prepareStatement(query);

                try {
                    int idInt = Integer.parseInt(doctorId);
                    pst.setInt(1, idInt);
                } catch (NumberFormatException nfe) {
                    pst.setString(1, doctorId);
                }

                int affectedRows = pst.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Doctor deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "No doctor ID found with this ID.");
                }

                pst.close();
                obj.con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new AdminHomePage();
        }
    }

    public static void main(String[] args) {
        new DeleteDoctorPage();
    }
}
