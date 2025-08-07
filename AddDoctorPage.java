package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddDoctorPage extends JFrame implements ActionListener {

    JTextField tfName, tfAge, tfSpec, tfPhone, tfEmail, tfCity, tfPassword, tfFees;
    JComboBox<String> genderBox;
    JButton submitBtn, backBtn;
    JLabel background;

    public AddDoctorPage() {
        setTitle("Add New Doctor");
        setSize(500, 550);
        setLocation(300, 100);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set layout and background image
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/add doctor.jpg"));
        background = new JLabel(img);
        background.setBounds(0, 0, 500, 550);
        setContentPane(background);
        background.setLayout(null);

        JLabel l1 = new JLabel("Add Doctor Details");
        l1.setFont(new Font("Arial", Font.BOLD, 20));
        l1.setBounds(150, 10, 300, 30);
        background.add(l1);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 60, 100, 25);
        background.add(lblName);

        tfName = new JTextField();
        tfName.setBounds(180, 60, 200, 25);
        background.add(tfName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setBounds(50, 100, 100, 25);
        background.add(lblAge);

        tfAge = new JTextField();
        tfAge.setBounds(180, 100, 200, 25);
        background.add(tfAge);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(50, 140, 100, 25);
        background.add(lblGender);

        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setBounds(180, 140, 200, 25);
        background.add(genderBox);

        JLabel lblSpec = new JLabel("Specialization:");
        lblSpec.setBounds(50, 180, 100, 25);
        background.add(lblSpec);

        tfSpec = new JTextField();
        tfSpec.setBounds(180, 180, 200, 25);
        background.add(tfSpec);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(50, 220, 100, 25);
        background.add(lblPhone);

        tfPhone = new JTextField();
        tfPhone.setBounds(180, 220, 200, 25);
        background.add(tfPhone);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 260, 100, 25);
        background.add(lblEmail);

        tfEmail = new JTextField();
        tfEmail.setBounds(180, 260, 200, 25);
        background.add(tfEmail);

        JLabel lblCity = new JLabel("City:");
        lblCity.setBounds(50, 300, 100, 25);
        background.add(lblCity);

        tfCity = new JTextField();
        tfCity.setBounds(180, 300, 200, 25);
        background.add(tfCity);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 340, 100, 25);
        background.add(lblPassword);

        tfPassword = new JTextField();
        tfPassword.setBounds(180, 340, 200, 25);
        background.add(tfPassword);

        JLabel lblFees = new JLabel("Fees:");
        lblFees.setBounds(50, 380, 100, 25);
        background.add(lblFees);

        tfFees = new JTextField();
        tfFees.setBounds(180, 380, 200, 25);
        background.add(tfFees);

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(100, 430, 100, 30);
        submitBtn.setBackground(Color.BLACK);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(this);
        background.add(submitBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(220, 430, 100, 30);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(this);
        background.add(backBtn);

        setVisible(true);
    }

    private String generateDoctorId() {
        String id = "D001";
        try {
            ConnectionClass obj = new ConnectionClass();
            String query = "SELECT doctor_id FROM doctor ORDER BY doctor_id DESC LIMIT 1";
            ResultSet rs = obj.stm.executeQuery(query);
            if (rs.next()) {
                String lastId = rs.getString("doctor_id");
                int num = Integer.parseInt(lastId.substring(1)) + 1;
                id = String.format("D%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitBtn) {
            String id = generateDoctorId();
            String name = tfName.getText();
            String age = tfAge.getText();
            String gender = (String) genderBox.getSelectedItem();
            String spec = tfSpec.getText();
            String phone = tfPhone.getText();
            String email = tfEmail.getText();
            String city = tfCity.getText();
            String password = tfPassword.getText();
            String fees = tfFees.getText();

            if (!phone.matches("01\\d{9}")) {
                JOptionPane.showMessageDialog(null, "Phone number must be a valid 11-digit Bangladeshi number starting with 01.");
                return;
            }

            if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(null, "Email must end with @gmail.com.");
                return;
            }

            try {
                ConnectionClass obj = new ConnectionClass();
                String query = "INSERT INTO doctor VALUES('" + id + "', '" + name + "', " + age + ", '" + gender + "', '" + spec + "', '" + phone + "', '" + email + "', '" + city + "','" + password + "', " + fees + ")";
                obj.stm.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Doctor added successfully.\nGenerated ID: " + id);
                setVisible(false);
                new AdminHomePage();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (ae.getSource() == backBtn) {
            setVisible(false);
            new AdminHomePage();
        }
    }

    public static void main(String[] args) {
        new AddDoctorPage();
    }
}
