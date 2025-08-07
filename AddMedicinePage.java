package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddMedicinePage extends JFrame implements ActionListener {

    JLabel lblId, lblName, lblType, lblPrice, lblDescription, lblQuantity, lblExpiry;
    JTextField tfId, tfName, tfType, tfPrice, tfQuantity, tfExpiry;
    JTextArea taDescription;
    JButton addBtn, backBtn;

    public AddMedicinePage() {
        setTitle("Add New Medicine");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set background image
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/add medicine.jpg"));
        JLabel background = new JLabel(img);
        background.setLayout(null);
        setContentPane(background);

        // Transparent panel for components
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setOpaque(false);
        panel.setBounds(20, 20, 440, 470);
        background.add(panel);

        // Labels
        lblId = new JLabel("Medicine ID:");
        lblId.setBounds(30, 20, 120, 25);
        panel.add(lblId);

        lblName = new JLabel("Name:");
        lblName.setBounds(30, 60, 120, 25);
        panel.add(lblName);

        lblType = new JLabel("Type:");
        lblType.setBounds(30, 100, 120, 25);
        panel.add(lblType);

        lblPrice = new JLabel("Price:");
        lblPrice.setBounds(30, 140, 120, 25);
        panel.add(lblPrice);

        lblDescription = new JLabel("Description:");
        lblDescription.setBounds(30, 180, 120, 25);
        panel.add(lblDescription);

        lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(30, 280, 120, 25);
        panel.add(lblQuantity);

        lblExpiry = new JLabel("Expiry Date (YYYY-MM-DD):");
        lblExpiry.setBounds(30, 320, 200, 25);
        panel.add(lblExpiry);

        // Input fields
        tfId = new JTextField();
        tfId.setBounds(200, 20, 200, 25);
        panel.add(tfId);

        tfName = new JTextField();
        tfName.setBounds(200, 60, 200, 25);
        panel.add(tfName);

        tfType = new JTextField();
        tfType.setBounds(200, 100, 200, 25);
        panel.add(tfType);

        tfPrice = new JTextField();
        tfPrice.setBounds(200, 140, 200, 25);
        panel.add(tfPrice);

        taDescription = new JTextArea();
        taDescription.setBounds(200, 180, 200, 80);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        panel.add(taDescription);

        tfQuantity = new JTextField();
        tfQuantity.setBounds(200, 280, 200, 25);
        panel.add(tfQuantity);

        tfExpiry = new JTextField();
        tfExpiry.setBounds(200, 320, 200, 25);
        panel.add(tfExpiry);

        // Buttons
        addBtn = new JButton("Add Medicine");
        addBtn.setBounds(80, 380, 130, 35);
        panel.add(addBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(230, 380, 130, 35);
        panel.add(backBtn);

        addBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            String id = tfId.getText().trim();
            String name = tfName.getText().trim();
            String type = tfType.getText().trim();
            String priceStr = tfPrice.getText().trim();
            String description = taDescription.getText().trim();
            String quantityStr = tfQuantity.getText().trim();
            String expiry = tfExpiry.getText().trim();

            if (id.isEmpty() || name.isEmpty() || type.isEmpty() || priceStr.isEmpty()
                    || quantityStr.isEmpty() || expiry.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all required fields.");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                ConnectionClass obj = new ConnectionClass();

                String query = "INSERT INTO medicine (medicine_id, name, type, price, description, quantity, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement pst = obj.con.prepareStatement(query);
                pst.setString(1, id);
                pst.setString(2, name);
                pst.setString(3, type);
                pst.setDouble(4, price);
                pst.setString(5, description);
                pst.setInt(6, quantity);
                pst.setDate(7, Date.valueOf(expiry));  // format: YYYY-MM-DD

                int inserted = pst.executeUpdate();

                if (inserted > 0) {
                    JOptionPane.showMessageDialog(null, "Medicine added successfully!");
                    setVisible(false);
                    new MedicineCornerPage();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add medicine.");
                }

                pst.close();
                obj.con.close();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for price and quantity.");
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(null, "Please enter a valid expiry date in the format YYYY-MM-DD.");
            } catch (SQLException se) {
                JOptionPane.showMessageDialog(null, "Database error: " + se.getMessage());
                se.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new MedicineCornerPage();
        }
    }

    public static void main(String[] args) {
        new AddMedicinePage();
    }
}
