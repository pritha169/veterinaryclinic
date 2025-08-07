package Hospital;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineCorner extends JFrame implements ActionListener {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAddToCart, btnViewCart, btnBack;
    private JLabel totalCostLabel;
    private List<CartItem> cart = new ArrayList<>();
    private String patientId;
    private ImageIcon backgroundIcon = new ImageIcon(ClassLoader.getSystemResource("Hospital/icons/store.jpg"));

    public MedicineCorner(String patientId) {
        this.patientId = patientId;

        setTitle("Medicine Corner for Patient ID: " + patientId);
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        String[] columns = {"Medicine Name", "Type", "Price", "Description", "Available", "Quantity"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 5;
            }

            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 2) return Double.class;
                if (col == 4 || col == 5) return Integer.class;
                return String.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);

        // Apply center alignment to all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(5).setCellRenderer(new QuantityRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new QuantityEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        backgroundPanel.add(scroll, BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        btnAddToCart = new JButton("Add to Cart");
        btnViewCart = new JButton("Checkout");
        btnBack = new JButton("Back");

        btnAddToCart.addActionListener(this);
        btnViewCart.addActionListener(this);
        btnBack.addActionListener(this);

        buttonPanel.add(btnAddToCart);
        buttonPanel.add(btnViewCart);
        buttonPanel.add(btnBack);
        panel.add(buttonPanel, BorderLayout.CENTER);

        totalCostLabel = new JLabel("Total Cost: $0.00");
        totalCostLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalCostLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        totalCostLabel.setForeground(Color.WHITE);
        panel.add(totalCostLabel, BorderLayout.SOUTH);

        backgroundPanel.add(panel, BorderLayout.SOUTH);

        loadMedicines();
        setVisible(true);
    }

    private void loadMedicines() {
        try {
            model.setRowCount(0); // Clear previous rows

            ConnectionClass cc = new ConnectionClass();
            Statement st = cc.con.createStatement();
            ResultSet rs = st.executeQuery("SELECT name, type, price, description, quantity FROM medicine");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        0
                });
            }

            cc.con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTotalCost() {
        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            int qty = (int) model.getValueAt(i, 5);
            if (qty > 0) {
                double price = (double) model.getValueAt(i, 2);
                total += price * qty;
            }
        }
        totalCostLabel.setText(String.format("Total Cost: $%.2f", total));
    }

    private void addToCart() {
        cart.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            int selectedQty = (int) model.getValueAt(i, 5);
            int available = (int) model.getValueAt(i, 4);

            if (selectedQty > 0) {
                if (selectedQty > available) {
                    JOptionPane.showMessageDialog(this, "Selected quantity exceeds stock for " + model.getValueAt(i, 0));
                    return;
                }

                cart.add(new CartItem(
                        (String) model.getValueAt(i, 0),
                        (String) model.getValueAt(i, 1),
                        (double) model.getValueAt(i, 2),
                        selectedQty
                ));
            }
        }

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No medicines selected!");
        } else {
            JOptionPane.showMessageDialog(this, "Items added to cart!");
        }
    }

    private void checkoutCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty. Please add items before checkout.");
            return;
        }

        try {
            ConnectionClass cc = new ConnectionClass();
            Connection con = cc.con;
            con.setAutoCommit(false); // transactional

            String insertOrder = "INSERT INTO medicine_orders(patient_id, medicine_name, type, quantity, price_per_unit, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
            String updateStock = "UPDATE medicine SET quantity = quantity - ? WHERE name = ?";

            PreparedStatement psInsert = con.prepareStatement(insertOrder);
            PreparedStatement psUpdate = con.prepareStatement(updateStock);

            double totalBill = 0;

            for (CartItem item : cart) {
                double subtotal = item.price * item.quantity;
                totalBill += subtotal;

                psInsert.setString(1, patientId);
                psInsert.setString(2, item.name);
                psInsert.setString(3, item.type);
                psInsert.setInt(4, item.quantity);
                psInsert.setDouble(5, item.price);
                psInsert.setDouble(6, subtotal);
                psInsert.addBatch();

                psUpdate.setInt(1, item.quantity);
                psUpdate.setString(2, item.name);
                psUpdate.addBatch();
            }

            psInsert.executeBatch();
            psUpdate.executeBatch();
            con.commit();
            con.close();

            JOptionPane.showMessageDialog(this, "Order placed successfully!\nTotal Bill: $" + String.format("%.2f", totalBill));
            clearCartAndReset();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred during checkout!");
        }
    }

    private void clearCartAndReset() {
        cart.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(0, i, 5); // Reset quantity column
        }
        loadMedicines(); // reload available stock
        updateTotalCost();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddToCart) {
            addToCart();
        } else if (e.getSource() == btnViewCart) {
            checkoutCart();
        } else if (e.getSource() == btnBack) {
            dispose();
        }
    }

    static class QuantityRenderer extends DefaultTableCellRenderer {
        public QuantityRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    class QuantityEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel;
        JButton btnMinus, btnPlus;
        JLabel lblQuantity;
        int currentQty = 0;

        public QuantityEditor() {
            panel = new JPanel(new BorderLayout());
            btnMinus = new JButton("-");
            btnPlus = new JButton("+");
            lblQuantity = new JLabel("0", SwingConstants.CENTER);

            panel.add(btnMinus, BorderLayout.WEST);
            panel.add(lblQuantity, BorderLayout.CENTER);
            panel.add(btnPlus, BorderLayout.EAST);

            btnMinus.addActionListener(e -> {
                if (currentQty > 0) {
                    currentQty--;
                    lblQuantity.setText(String.valueOf(currentQty));
                    fireEditingStopped();
                    updateTotalCost();
                }
            });

            btnPlus.addActionListener(e -> {
                int row = table.getEditingRow();
                int available = (int) model.getValueAt(row, 4);
                if (currentQty < available) {
                    currentQty++;
                    lblQuantity.setText(String.valueOf(currentQty));
                    fireEditingStopped();
                    updateTotalCost();
                } else {
                    JOptionPane.showMessageDialog(MedicineCorner.this, "Reached max available stock!");
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return currentQty;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentQty = (value instanceof Integer) ? (Integer) value : 0;
            lblQuantity.setText(String.valueOf(currentQty));
            return panel;
        }
    }

    static class CartItem {
        String name, type;
        double price;
        int quantity;

        CartItem(String name, String type, double price, int quantity) {
            this.name = name;
            this.type = type;
            this.price = price;
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        new MedicineCorner("P001");
    }
}
