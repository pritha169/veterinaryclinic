package Hospital;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * GuestModeScreen provides a modern interface for guests visiting the veterinary hospital system.
 * This class creates its own JFrame and manages the guest view lifecycle.
 */
public class GuestPage {
    // Window components
    private JFrame frame;
    private JPanel mainPanel;

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private final Color ACCENT_COLOR = new Color(39, 174, 96);      // Green
    private final Color TEXT_COLOR = new Color(44, 62, 80);         // Dark blue/gray
    private final Color LIGHT_BG = new Color(236, 240, 241);        // Light gray
    private final Color WHITE = new Color(255, 255, 255);           // White

    // UI Components
    private JPanel headerPanel;
    private JPanel mainContentPanel;
    private JPanel featuresPanel;
    private JPanel footerPanel;

    /**
     * Constructor creates and shows the guest mode screen
     */
    public GuestPage() {
        initializeFrame();
        initializeUI();
        frame.setVisible(true);
    }

    /**
     * Initialize the main application frame
     */
    private void initializeFrame() {
        frame = new JFrame("VetConnect Hospital - Guest Mode");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIManager.getColor("Panel.background"));
        frame.setContentPane(mainPanel);
    }

    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        createHeaderPanel();
        createMainContentPanel();
        createFooterPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Create the header panel with logo and title
     */
    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Hospital name with custom font and color
        JLabel hospitalName = new JLabel("VetConnect Hospital");
        hospitalName.setFont(new Font("Segoe UI", Font.BOLD, 28));
        hospitalName.setForeground(WHITE);

        // Tagline
        JLabel tagline = new JLabel("Professional Care for Your Pets");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        tagline.setForeground(new Color(214, 234, 248));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(hospitalName);
        titlePanel.add(tagline);

        // Add home button
        JButton homeButton = createTextLinkButton("← Back to Home Screen");
        homeButton.setForeground(WHITE);
        homeButton.addActionListener(e -> {
            frame.dispose();
            new Index();
        });

        // Try to load logo
        try {

// Inside try block
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Hospital/icons/hospital_logo.png"));
            Image image = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            BufferedImage circleBuffer = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new Ellipse2D.Float(0, 0, 50, 50));
            g2.drawImage(image, 0, 0, 50, 50, null);
            g2.dispose();
            JLabel logo = new JLabel(new ImageIcon(circleBuffer));
            headerPanel.add(logo, BorderLayout.WEST);


        } catch (Exception e) {
            System.out.println("Hospital logo not found, continuing without it.");
        }

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(homeButton, BorderLayout.EAST);
    }

    /**
     * Create the main content panel with welcome message and hero image
     */
    private void createMainContentPanel() {
        mainContentPanel = new JPanel(new BorderLayout(20, 0));
        mainContentPanel.setBackground(WHITE);
        mainContentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Welcome message panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome to Modern Pet Care");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextPane infoText = new JTextPane();
        infoText.setContentType("text/html");
        infoText.setText("<html><body style='font-family:Segoe UI; font-size:14pt; color:#34495e'>" +
                "Your pet deserves the best care possible. At VetConnect Hospital, we combine " +
                "cutting-edge veterinary medicine with compassionate care to keep your furry, " +
                "feathered, or scaled family members happy and healthy.<br><br>" +
                "Return to the welcome screen to log in or register as a user for full access to our services." +
                "</body></html>");
        infoText.setEditable(false);
        infoText.setOpaque(false);
        infoText.setBorder(null);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        welcomePanel.add(infoText);

        // Actions panel with modern buttons
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton loginButton = createStyledButton("Return to Login", PRIMARY_COLOR, WHITE);

        loginButton.addActionListener(e -> {
            frame.dispose();
            new LoginPage();
        });

        actionsPanel.add(loginButton);
        actionsPanel.add(Box.createHorizontalGlue());
//
        welcomePanel.add(Box.createVerticalGlue());
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(actionsPanel);


        // Try to add hero image
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Hospital/icons/guestpage.jpg"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            JLabel heroImage = new JLabel(new ImageIcon(scaledImage));
            heroImage.setBorder(BorderFactory.createLineBorder(LIGHT_BG, 3));
            mainContentPanel.add(heroImage, BorderLayout.EAST);
        } catch (Exception e) {
            System.out.println("Hero image not found, adjusting layout.");
        }

        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);
        createFeaturesPanel();
        mainContentPanel.add(featuresPanel, BorderLayout.SOUTH);
    }

    /**
     * Create feature cards showcasing hospital services
     */
    private void createFeaturesPanel() {
        featuresPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        featuresPanel.setOpaque(false);
        featuresPanel.setBorder(new EmptyBorder(40, 0, 0, 0));

        featuresPanel.add(createFeatureCard(
                "Medical Services",
                "Comprehensive healthcare including check-ups, vaccinations, surgery, and emergency care.",
                "/Hospital/icons/medical.png"));

        featuresPanel.add(createFeatureCard(
                "Pet Pharmacy",
                "Order and refill prescriptions online with our fully-stocked pharmacy.",
                "/Hospital/icons/pharmacy.png"));

        featuresPanel.add(createFeatureCard(
                "Grooming & Boarding",
                "Professional grooming services and comfortable accommodations for your pets.",
                "/Hospital/icons/grooming.png"));
    }

    /**
     * Create a styled feature card for services
     */
    private JPanel createFeatureCard(String title, String description, String iconPath) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(LIGHT_BG);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2d.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);

        // Try to load icon
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(iconLabel);
            card.add(Box.createRigidArea(new Dimension(0, 10)));
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea descLabel = new JTextArea(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setLineWrap(true);
        descLabel.setWrapStyleWord(true);
        descLabel.setEditable(false);
        descLabel.setOpaque(false);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(descLabel);

        // Add click effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showServiceDetails(title);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        return card;
    }

    /**
     * Create footer panel with contact and additional links
     */
    private void createFooterPanel() {
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(LIGHT_BG);
        footerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        // Contact information
        JLabel contactLabel = new JLabel("Contact Us: (+880) 1911909164 | info@vetconnecthospital.com");
        contactLabel.setForeground(TEXT_COLOR);
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Footer links
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        linksPanel.setOpaque(false);

        JButton aboutButton = createTextLinkButton("About Us");
        JButton servicesButton = createTextLinkButton("Services");
        JButton contactButton = createTextLinkButton("Contact");

        aboutButton.addActionListener(e -> showAboutInfo());
        servicesButton.addActionListener(e -> showServicesInfo());
        contactButton.addActionListener(e -> showContactInfo());

        linksPanel.add(aboutButton);
        linksPanel.add(servicesButton);
        linksPanel.add(contactButton);

        footerPanel.add(contactLabel, BorderLayout.WEST);
        footerPanel.add(linksPanel, BorderLayout.EAST);
    }

    /**
     * Create a modern styled button
     */
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isPressed() ? bgColor.darker() : bgColor);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(fgColor);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));

        return button;
    }

    /**
     * Create a text link button for footer
     */
    private JButton createTextLinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    /**
     * Show detailed info about a specific service
     */
    private void showServiceDetails(String serviceType) {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(serviceType);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea detailText = new JTextArea();
        detailText.setEditable(false);
        detailText.setLineWrap(true);
        detailText.setWrapStyleWord(true);
        detailText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailText.setBackground(null);

        switch (serviceType) {
            case "Medical Services":
                detailText.setText(
                        "• Complete wellness examinations\n" +
                                "• Vaccinations and preventative care\n" +
                                "• Diagnostics including X-ray and ultrasound\n" +
                                "• Dental care and cleaning\n" +
                                "• Surgical procedures\n" +
                                "• Emergency and urgent care\n" +
                                "• Internal medicine\n" +
                                "• Geriatric pet care\n\n" +
                                "Our state-of-the-art facility is equipped with advanced technology " +
                                "to provide the highest standard of care for your pets."
                );

                try {
                    ImageIcon medicalIcon = new ImageIcon(getClass().getResource("/Hospital/icons/medical_service.png"));
                    Image medicalImage = medicalIcon.getImage().getScaledInstance(350, 200, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(medicalImage));
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    detailPanel.add(imageLabel);
                } catch (Exception ex) {
                    System.out.println("medical_service.png not found.");
                }
                break;

            case "Pet Pharmacy":
                detailText.setText(
                        "• Prescription medications\n" +
                                "• Flea, tick, and heartworm prevention\n" +
                                "• Nutritional supplements\n" +
                                "• Special diet foods\n" +
                                "• Online prescription refills\n" +
                                "• Medication consultations\n\n" +
                                "Our in-house pharmacy ensures that your pet gets the right medication " +
                                "at the right time. We offer competitive pricing and convenient home delivery options."
                );

                try {
                    ImageIcon pharmacyIcon = new ImageIcon(getClass().getResource("/Hospital/icons/pharmacy_service.jpg"));
                    Image pharmacyImage = pharmacyIcon.getImage().getScaledInstance(350, 200, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(pharmacyImage));
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    detailPanel.add(imageLabel);
                } catch (Exception ex) {
                    System.out.println("pharmacy_service.jpg not found.");
                }
                break;
            case "Grooming & Boarding":
                detailText.setText(
                        "• Professional bathing and grooming\n" +
                                "• Nail trimming and ear cleaning\n" +
                                "• Breed-specific styling\n" +
                                "• Comfortable, climate-controlled boarding kennels\n" +
                                "• Individual attention and exercise\n" +
                                "• Special accommodations for senior pets or those with medical needs\n\n" +
                                "Whether your pet needs a spa day or a comfortable place to stay while you're away, " +
                                "our professional staff will ensure they receive loving care and attention."
                );

                try {
                    ImageIcon groomingIcon = new ImageIcon(getClass().getResource("/Hospital/icons/grooming.jpg"));
                    Image groomingImage = groomingIcon.getImage().getScaledInstance(350, 200, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(groomingImage));
                    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    detailPanel.add(imageLabel);
                } catch (Exception ex) {
                    System.out.println("grooming.jpg not found.");
                }
                break;

        }

        JScrollPane scrollPane = new JScrollPane(detailText);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        detailPanel.add(titleLabel);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        detailPanel.add(scrollPane);

        JOptionPane.showMessageDialog(frame, detailPanel,
                serviceType + " - VetConnect Hospital", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Show about information dialog
     */
    private void showAboutInfo() {
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("About VetConnect Hospital");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea aboutText = new JTextArea(
                "VetConnect Hospital was founded in 2010 with a mission to provide exceptional " +
                        "veterinary care with compassion and advanced medical technology.\n\n" +
                        "Our team of experienced veterinarians and staff are dedicated to the health and " +
                        "wellbeing of your pets. We believe in building lasting relationships with our " +
                        "clients and their pets, providing personalized care for every animal.\n\n" +
                        "Our state-of-the-art facility is equipped with the latest diagnostic and treatment " +
                        "technologies to ensure your pet receives the best possible care. From routine " +
                        "check-ups to complex surgical procedures, we are here for you and your pet " +
                        "every step of the way."
        );
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aboutText.setBackground(null);

        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        aboutPanel.add(titleLabel);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        aboutPanel.add(scrollPane);

        JOptionPane.showMessageDialog(frame, aboutPanel,
                "About Us - VetConnect Hospital", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Show services information dialog
     */
    private void showServicesInfo() {
        JPanel servicesPanel = new JPanel();
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        servicesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Our Services");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea servicesText = new JTextArea(
                "• Wellness & Preventive Care: Regular examinations, vaccinations, parasite prevention\n\n" +
                        "• Diagnostics: Laboratory testing, digital X-rays, ultrasound imaging\n\n" +
                        "• Surgical Services: Soft tissue surgery, orthopedic procedures, spay/neuter\n\n" +
                        "• Dental Care: Cleaning, extractions, oral health assessments\n\n" +
                        "• Emergency Care: Treatment for urgent medical conditions and injuries\n\n" +
                        "• Pharmacy: Prescription medications, supplements, special diets\n\n" +
                        "• Grooming: Professional bathing, styling, nail trimming\n\n" +
                        "• Boarding: Comfortable accommodations, daily exercise, medication administration\n\n" +
                        "• Behavior Counseling: Managing behavioral issues and training recommendations\n\n" +
                        "• Nutritional Counseling: Customized diet plans for all life stages"
        );
        servicesText.setEditable(false);
        servicesText.setLineWrap(true);
        servicesText.setWrapStyleWord(true);
        servicesText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        servicesText.setBackground(null);

        JScrollPane scrollPane = new JScrollPane(servicesText);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(500, 350));

        servicesPanel.add(titleLabel);
        servicesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        servicesPanel.add(scrollPane);

        JOptionPane.showMessageDialog(frame, servicesPanel,
                "Services - VetConnect Hospital", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Show contact information dialog
     */
    private void showContactInfo() {
        JPanel contactContent = new JPanel();
        contactContent.setLayout(new BoxLayout(contactContent, BoxLayout.Y_AXIS));
        contactContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Contact Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea contactText = new JTextArea(
                "VetConnect Hospital\n\n" +
                        "Address: 123 Pet Care Road, Narayanganj, Bangladesh\n\n" +
                        "Phone: (+880) 1911-909164\n" +
                        "Emergency: (+880) 1700-000000\n\n" +
                        "Email: info@vetconnecthospital.com\n" +
                        "Website: www.vetconnecthospital.com\n\n" +
                        "Hours:\n" +
                        "Monday - Friday: 8:00 AM - 7:00 PM\n" +
                        "Saturday: 9:00 AM - 5:00 PM\n" +
                        "Sunday: 10:00 AM - 3:00 PM (Emergency Only)"
        );
        contactText.setEditable(false);
        contactText.setLineWrap(true);
        contactText.setWrapStyleWord(true);
        contactText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contactText.setBackground(null);
        contactText.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add image if available
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Hospital/icons/guestpage2.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            JLabel mapImage = new JLabel(new ImageIcon(scaledImage));
            mapImage.setAlignmentX(Component.CENTER_ALIGNMENT);
            mapImage.setBorder(BorderFactory.createLineBorder(LIGHT_BG, 3));
            contactContent.add(titleLabel);
            contactContent.add(Box.createRigidArea(new Dimension(0, 15)));
            contactContent.add(contactText);
            contactContent.add(Box.createRigidArea(new Dimension(0, 15)));
            contactContent.add(mapImage);
        } catch (Exception e) {
            contactContent.add(titleLabel);
            contactContent.add(Box.createRigidArea(new Dimension(0, 15)));
            contactContent.add(contactText);
        }

        // Simulated "Call Us" button
        JButton callButton = new JButton("Call Us: (+880) 1911-909164");
        callButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        callButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        callButton.setFocusPainted(false);
        callButton.setBackground(new Color(39, 174, 96)); // Use green for action
        callButton.setForeground(Color.black);
        callButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        callButton.setPreferredSize(new Dimension(260, 40));
        callButton.setMaximumSize(new Dimension(260, 40));
        callButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        callButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Dialing (+880) 1911-909164...\n(This simulates a call action)",
                    "Call VetConnect", JOptionPane.INFORMATION_MESSAGE);
        });

        contactContent.add(Box.createRigidArea(new Dimension(0, 20)));
        contactContent.add(callButton);

        JScrollPane scrollPane = new JScrollPane(contactContent);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        scrollPane.setBorder(null);

        JOptionPane.showMessageDialog(frame, scrollPane,
                "Contact Us - VetConnect Hospital", JOptionPane.PLAIN_MESSAGE);
    }


    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        // Set the look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show the guest mode screen
        SwingUtilities.invokeLater(() -> {
            new GuestPage();
        });
    }
}