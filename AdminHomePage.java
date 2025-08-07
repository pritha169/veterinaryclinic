package Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminHomePage extends JFrame implements ActionListener {

    JButton addDoctorBtn, viewDoctorBtn, deleteDoctorBtn, logoutBtn, medicineCornerBtn, myProfileBtn;
    JLabel background;

    public AdminHomePage() {
        setTitle("Admin Home Page");
        setSize(600, 500);
        setLocation(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load background image
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/admin home.jpg"));
        Image i1 = img.getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH);
        ImageIcon img2 = new ImageIcon(i1);
        background = new JLabel(img2);
        background.setBounds(0, 0, 600, 500);
        setContentPane(background);
        background.setLayout(null);  // Use null layout on background

        JLabel heading = new JLabel("Admin Dashboard");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(Color.WHITE); // Set readable color depending on image
        heading.setBounds(180, 20, 300, 40);
        background.add(heading);

        addDoctorBtn = new JButton("Add Doctor");
        addDoctorBtn.setBounds(200, 80, 180, 40);
        background.add(addDoctorBtn);

        viewDoctorBtn = new JButton("View Doctors");
        viewDoctorBtn.setBounds(200, 130, 180, 40);
        background.add(viewDoctorBtn);

        deleteDoctorBtn = new JButton("Delete Doctor");
        deleteDoctorBtn.setBounds(200, 180, 180, 40);
        background.add(deleteDoctorBtn);

        medicineCornerBtn = new JButton("Medicine Corner");
        medicineCornerBtn.setBounds(200, 230, 180, 40);
        background.add(medicineCornerBtn);

        myProfileBtn = new JButton("My Profile");
        myProfileBtn.setBounds(200, 280, 180, 40);
        background.add(myProfileBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(200, 330, 180, 40);
        background.add(logoutBtn);

        addDoctorBtn.addActionListener(this);
        viewDoctorBtn.addActionListener(this);
        deleteDoctorBtn.addActionListener(this);
        medicineCornerBtn.addActionListener(this);
        myProfileBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addDoctorBtn) {
            setVisible(false);
            new AddDoctorPage();
        } else if (e.getSource() == viewDoctorBtn) {
            setVisible(false);
            new ViewDoctorPage();
        } else if (e.getSource() == deleteDoctorBtn) {
            setVisible(false);
            new DeleteDoctorPage();
        } else if (e.getSource() == medicineCornerBtn) {
            setVisible(false);
            new MedicineCornerPage();
        } else if (e.getSource() == myProfileBtn) {
            setVisible(false);
            new AdminProfilePage();
        } else if (e.getSource() == logoutBtn) {
            setVisible(false);
            new Index();
        }
    }

    public static void main(String[] args) {
        new AdminHomePage();
    }
}
