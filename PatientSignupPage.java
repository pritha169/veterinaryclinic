package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PatientSignupPage extends JFrame implements ActionListener {
    JLabel idLabel, nameLabel, ageLabel, genderLabel, phoneLabel, emailLabel, addressLabel, passLabel;
    JTextField idField, nameField, ageField, phoneField, emailField, addressField;
    JPasswordField passField;
    JComboBox<String> genderCombo;
    JButton signupBtn, backBtn;

    public PatientSignupPage() {
        setTitle("Patient Signup");
        setSize(450, 600);
        setLocation(450, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ Set background image
        ImageIcon bgIcon = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/patient_sign.jpg"));
        Image img = bgIcon.getImage().getScaledInstance(450, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(img));
        background.setBounds(0, 0, 450, 600);
        setContentPane(background);
        background.setLayout(null);

        JLabel heading = new JLabel("Patient Signup");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBounds(140, 20, 200, 30);
        background.add(heading);

        idLabel = new JLabel("Patient ID:");
        idLabel.setBounds(50, 70, 100, 25);
        background.add(idLabel);

        idField = new JTextField(generatePatientId());
        idField.setBounds(160, 70, 200, 25);
        idField.setEditable(false);
        background.add(idField);

        nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(50, 110, 100, 25);
        background.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(160, 110, 200, 25);
        background.add(nameField);

        ageLabel = new JLabel("Age:");
        ageLabel.setBounds(50, 150, 100, 25);
        background.add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(160, 150, 200, 25);
        background.add(ageField);

        genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(50, 190, 100, 25);
        background.add(genderLabel);

        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setBounds(160, 190, 200, 25);
        background.add(genderCombo);

        phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(50, 230, 120, 25);
        background.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(160, 230, 200, 25);
        background.add(phoneField);

        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 270, 100, 25);
        background.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 270, 200, 25);
        background.add(emailField);

        addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 310, 100, 25);
        background.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(160, 310, 200, 25);
        background.add(addressField);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 350, 100, 25);
        background.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(160, 350, 200, 25);
        background.add(passField);

        signupBtn = new JButton("Signup");
        signupBtn.setBounds(100, 410, 90, 35);
        signupBtn.addActionListener(this);
        background.add(signupBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(220, 410, 90, 35);
        backBtn.addActionListener(this);
        background.add(backBtn);

        setVisible(true);
    }

    private String generatePatientId() {
        String newId = "p001";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_1", "root", "Pritha123!");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT patient_id FROM patient ORDER BY patient_id DESC LIMIT 1");
            if (rs.next()) {
                String lastId = rs.getString("patient_id");
                int lastNum = Integer.parseInt(lastId.substring(1));
                newId = String.format("p%03d", lastNum + 1);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error generating patient ID: " + e.getMessage());
        }
        return newId;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupBtn) {
            String patientId = idField.getText();
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String password = new String(passField.getPassword());

            if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all mandatory fields (Name, Age, Phone, Password).");
                return;
            }

            // ✅ Validate age
            int age;
            try {
                age = Integer.parseInt(ageText);
                if (age <= 0) {
                    JOptionPane.showMessageDialog(this, "Age must be a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid age. Please enter a valid number.");
                return;
            }

            // ✅ Validate Bangladeshi phone number
            if (!phone.matches("01[3-9]\\d{8}")) {
                JOptionPane.showMessageDialog(this, "Invalid Bangladeshi phone number.\nIt must start with 013-019 and be 11 digits long.");
                return;
            }

            // ✅ Validate email ends with @gmail.com
            if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Email must end with @gmail.com");
                return;
            }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_1", "root", "Pritha123!");
                String query = "INSERT INTO patient (patient_id, name, age, gender, phone, email, address, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, patientId);
                pst.setString(2, name);
                pst.setInt(3, age);
                pst.setString(4, gender);
                pst.setString(5, phone);
                pst.setString(6, email);
                pst.setString(7, address);
                pst.setString(8, password);

                int rowCount = pst.executeUpdate();
                if (rowCount > 0) {
                    JOptionPane.showMessageDialog(this, "Signup successful! Please log in.");
                    setVisible(false);
                    new PatientLoginPage();
                } else {
                    JOptionPane.showMessageDialog(this, "Signup failed. Please try again.");
                }
                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new Index();
        }
    }

    public static void main(String[] args) {
        new PatientSignupPage();
    }
}
