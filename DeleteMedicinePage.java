package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteMedicinePage extends JFrame implements ActionListener {

    JTextField tfId;
    JButton deleteBtn, backBtn;

    public DeleteMedicinePage() {
        setTitle("Delete Medicine");
        setSize(400, 200);
        setLocation(450, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Delete Medicine");
        heading.setFont(new Font("Tahoma", Font.BOLD, 18));
        heading.setBounds(120, 10, 200, 30);
        add(heading);

        JLabel lblId = new JLabel("Enter Medicine ID:");
        lblId.setBounds(40, 60, 120, 25);
        add(lblId);

        tfId = new JTextField();
        tfId.setBounds(170, 60, 150, 25);
        add(tfId);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(80, 110, 100, 30);
        add(deleteBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(200, 110, 100, 30);
        add(backBtn);

        deleteBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteBtn) {
            String id = tfId.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a Medicine ID.");
                return;
            }

            try {
                ConnectionClass obj = new ConnectionClass();
                String query = "DELETE FROM medicine WHERE medicine_id = ?";
                PreparedStatement pst = obj.con.prepareStatement(query);
                pst.setString(1, id);

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Medicine deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Medicine ID not found.");
                }

                pst.close();
                obj.con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new MedicineCornerPage();
        }
    }

    public static void main(String[] args) {
        new DeleteMedicinePage();
    }
}
