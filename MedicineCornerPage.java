package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MedicineCornerPage extends JFrame implements ActionListener {

    JTable table;
    JButton addBtn, updateBtn, deleteBtn, backBtn;
    JScrollPane scrollPane;

    public MedicineCornerPage() {
        setTitle("Medicine Corner");
        setSize(800, 400);
        setLocation(200, 150);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel heading = new JLabel("Pet Medicine Corner");
        heading.setBounds(300, 10, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(heading);

        addBtn = new JButton("Add Medicine");
        addBtn.setBounds(50, 320, 150, 30);
        add(addBtn);

        updateBtn = new JButton("Update Medicine");
        updateBtn.setBounds(230, 320, 150, 30);
        add(updateBtn);

        deleteBtn = new JButton("Delete Medicine");
        deleteBtn.setBounds(410, 320, 150, 30);
        add(deleteBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(590, 320, 150, 30);
        add(backBtn);

        // Load data from DB
        loadMedicineData();

        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    private void loadMedicineData() {
        try {
            ConnectionClass obj = new ConnectionClass();
            String query = "SELECT * FROM medicine";
            ResultSet rs = obj.stm.executeQuery(query);

            String[] columns = {"ID", "Name", "Type", "Price", "Description", "Quantity", "Expiry"};
            String[][] data = new String[100][7];

            int row = 0;
            while (rs.next()) {
                data[row][0] = rs.getString("medicine_id");
                data[row][1] = rs.getString("name");
                data[row][2] = rs.getString("type");
                data[row][3] = rs.getString("price");
                data[row][4] = rs.getString("description");
                data[row][5] = rs.getString("quantity");
                data[row][6] = rs.getString("expiry_date");
                row++;
            }

            table = new JTable(data, columns);
            scrollPane = new JScrollPane(table);
            scrollPane.setBounds(30, 60, 720, 240);
            add(scrollPane);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            setVisible(false);
            new AddMedicinePage();
        } else if (e.getSource() == updateBtn) {
            setVisible(false);
            new UpdateMedicinePage();
        } else if (e.getSource() == deleteBtn) {
            setVisible(false);
            new DeleteMedicinePage();
        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new AdminHomePage(); // Or your previous main menu page
        }
    }

    public static void main(String[] args) {
        new MedicineCornerPage();
    }
}
