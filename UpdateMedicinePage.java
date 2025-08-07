package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateMedicinePage extends JFrame implements ActionListener {

    JTextField tfId, tfName, tfType, tfPrice, tfQuantity, tfExpiry;
    JTextArea taDescription;
    JButton searchBtn, updateBtn, backBtn;
    JLabel background;

    public UpdateMedicinePage() {
        setTitle("Update Medicine");
        setSize(500, 500);
        setLocation(350, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout to null for absolute positioning
        setLayout(null);

        // Background image setup
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/update medicine.jpg"));
        Image scaledImg = img.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        background = new JLabel(scaledIcon);
        background.setBounds(0, 0, 500, 500);
        add(background);

        // Adding components on top of background
        JLabel heading = new JLabel("Update Medicine");
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        heading.setBounds(150, 10, 200, 30);
        background.add(heading);

        JLabel lblId = new JLabel("Enter Medicine ID:");
        lblId.setBounds(30, 60, 150, 25);
        background.add(lblId);

        tfId = new JTextField();
        tfId.setBounds(180, 60, 150, 25);
        background.add(tfId);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(340, 60, 100, 25);
        background.add(searchBtn);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 110, 100, 25);
        background.add(lblName);

        tfName = new JTextField();
        tfName.setBounds(150, 110, 250, 25);
        background.add(tfName);

        JLabel lblType = new JLabel("Type:");
        lblType.setBounds(30, 150, 100, 25);
        background.add(lblType);

        tfType = new JTextField();
        tfType.setBounds(150, 150, 250, 25);
        background.add(tfType);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setBounds(30, 190, 100, 25);
        background.add(lblPrice);

        tfPrice = new JTextField();
        tfPrice.setBounds(150, 190, 250, 25);
        background.add(tfPrice);

        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setBounds(30, 230, 100, 25);
        background.add(lblDescription);

        taDescription = new JTextArea();
        taDescription.setBounds(150, 230, 250, 60);
        background.add(taDescription);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(30, 310, 100, 25);
        background.add(lblQuantity);

        tfQuantity = new JTextField();
        tfQuantity.setBounds(150, 310, 250, 25);
        background.add(tfQuantity);

        JLabel lblExpiry = new JLabel("Expiry Date (YYYY-MM-DD):");
        lblExpiry.setBounds(30, 350, 200, 25);
        background.add(lblExpiry);

        tfExpiry = new JTextField();
        tfExpiry.setBounds(230, 350, 170, 25);
        background.add(tfExpiry);

        updateBtn = new JButton("Update");
        updateBtn.setBounds(150, 400, 100, 30);
        background.add(updateBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(270, 400, 100, 30);
        background.add(backBtn);

        searchBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            String id = tfId.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a Medicine ID.");
                return;
            }

            try {
                ConnectionClass obj = new ConnectionClass();
                String query = "SELECT * FROM medicine WHERE medicine_id = ?";
                PreparedStatement pst = obj.con.prepareStatement(query);
                pst.setString(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    tfName.setText(rs.getString("name"));
                    tfType.setText(rs.getString("type"));
                    tfPrice.setText(rs.getString("price"));
                    taDescription.setText(rs.getString("description"));
                    tfQuantity.setText(rs.getString("quantity"));
                    tfExpiry.setText(rs.getString("expiry_date"));
                } else {
                    JOptionPane.showMessageDialog(null, "Medicine ID not found.");
                }

                rs.close();
                pst.close();
                obj.con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == updateBtn) {
            try {
                String id = tfId.getText().trim();
                String name = tfName.getText().trim();
                String type = tfType.getText().trim();
                String price = tfPrice.getText().trim();
                String desc = taDescription.getText().trim();
                String qty = tfQuantity.getText().trim();
                String expiry = tfExpiry.getText().trim();

                ConnectionClass obj = new ConnectionClass();
                String query = "UPDATE medicine SET name=?, type=?, price=?, description=?, quantity=?, expiry_date=? WHERE medicine_id=?";
                PreparedStatement pst = obj.con.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, type);
                pst.setString(3, price);
                pst.setString(4, desc);
                pst.setString(5, qty);
                pst.setString(6, expiry);
                pst.setString(7, id);

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Medicine updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed. Medicine ID not found.");
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
        new UpdateMedicinePage();
    }
}
