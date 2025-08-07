package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewDoctorPage extends JFrame implements ActionListener {

    JTable table;
    JScrollPane scrollPane;
    JButton backBtn;
    JLabel background;

    public ViewDoctorPage() {
        setTitle("View Doctors");
        setSize(800, 450);
        setLocation(200, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Load background image
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/view doctor.jpg"));
        background = new JLabel(img);
        background.setBounds(50, 100, 1500, 800);
        setContentPane(background);
        background.setLayout(new BorderLayout());

        try {
            ConnectionClass obj = new ConnectionClass();

            Statement stmt = obj.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "SELECT * FROM doctor";
            ResultSet rs = stmt.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            String[] columnNames = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                columnNames[i - 1] = rsmd.getColumnName(i);
            }

            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();

            String[][] data = new String[rowCount][colCount];
            int rowIndex = 0;

            while (rs.next()) {
                for (int col = 0; col < colCount; col++) {
                    data[rowIndex][col] = rs.getString(col + 1);
                }
                rowIndex++;
            }

            table = new JTable(data, columnNames);
            scrollPane = new JScrollPane(table);
            background.add(scrollPane, BorderLayout.CENTER);

            rs.close();
            stmt.close();
            obj.con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
            e.printStackTrace();
        }

        // Back button panel at the bottom
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Make panel transparent to see background
        backBtn = new JButton("Back");
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(this);
        panel.add(backBtn);
        background.add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backBtn) {
            setVisible(false);
            new AdminHomePage();
        }
    }

    public static void main(String[] args) {
        new ViewDoctorPage();
    }
}
