import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

/* 
Author: Kristine Bermudo 23-1260-868
*/

public class StudentTableApp extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField idField, firstNameField, lastNameField, gradeField;
    private JButton addButton, deleteButton;

    private final String DEFAULT_CSV = "/MOCK_DATA.csv";  // bundled in resources
    private final String DATA_CSV = "student_data.csv";   // writable CSV

    public StudentTableApp() {
        this.setTitle("Records - Kristine Bermudo 23-1260-868");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ----- Table Model -----
        model = new DefaultTableModel();
        // Add a hidden `Grades` column to store the raw comma-separated grades
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Average Grade", "Grades"});

        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Hide the raw Grades column from the table view but keep it in the model
        // (remove from view preserves data in the TableModel)
        if (table.getColumnModel().getColumnCount() > 3) {
            table.removeColumn(table.getColumnModel().getColumn(3));
        }

        // ----- Input Panel (bottom) -----
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // ----- Input Fields -----
        idField = new JTextField();
        idField.setFont(fieldFont);
        idField.setPreferredSize(new Dimension(100, 30));

        firstNameField = new JTextField();
        firstNameField.setFont(fieldFont);
        firstNameField.setPreferredSize(new Dimension(180, 30));

        lastNameField = new JTextField();
        lastNameField.setFont(fieldFont);
        lastNameField.setPreferredSize(new Dimension(180, 30));

        gradeField = new JTextField();
        gradeField.setFont(fieldFont);
        gradeField.setPreferredSize(new Dimension(250, 30)); 

        addButton = new JButton("Add");
        addButton.setFont(buttonFont);
        addButton.setBackground(new Color(59, 89, 182));
        addButton.setForeground(Color.WHITE);
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);

        deleteButton = new JButton("Delete");
        deleteButton.setFont(buttonFont);
        deleteButton.setBackground(new Color(220, 50, 50));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);

        // ----- Layout Input Fields and Buttons -----
        gbc.weightx = 0.0;
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; inputPanel.add(idField, gbc);

        gbc.gridx = 2; gbc.weightx = 0.0; inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; inputPanel.add(firstNameField, gbc);

        gbc.gridx = 4; gbc.weightx = 0.0; inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 5; gbc.weightx = 1.0; inputPanel.add(lastNameField, gbc);

        gbc.gridx = 6; gbc.weightx = 0.0; inputPanel.add(new JLabel("Grades (comma-separated):"), gbc);
        gbc.gridx = 7; gbc.weightx = 1.0; inputPanel.add(gradeField, gbc);

        gbc.gridx = 8; gbc.weightx = 0.0; inputPanel.add(addButton, gbc);
        gbc.gridx = 9; gbc.weightx = 0.0; inputPanel.add(deleteButton, gbc);

        add(inputPanel, BorderLayout.SOUTH);

        // ----- Button Actions -----
        addButton.addActionListener(e -> { addRow(); saveCSV(); });
        deleteButton.addActionListener(e -> { deleteRow(); saveCSV(); });

        // ----- Load CSV -----
        loadCSV();
    }

    // ----- Load CSV, skip first line automatically -----
    private void loadCSV() {
        File file = new File(DATA_CSV);
        if (!file.exists()) {
            try (InputStream is = getClass().getResourceAsStream(DEFAULT_CSV)) {
                if (is == null) return;
                try (OutputStream os = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        os.write(buffer, 0, len);
                    }
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip first line (header)
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    String fullName = parts[1] + " " + parts[2];
                    double avg = computeAverage(parts[3]);
                    // Store: ID, FullName, Average (formatted), Raw Grades
                    model.addRow(new String[]{parts[0], fullName, String.format("%.2f", avg), parts[3]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading CSV: " + e.getMessage());
        }
    }

    // ----- Save table to CSV -----
    private void saveCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_CSV))) {
            pw.println("ID,First Name,Last Name,Grades");
            for (int i = 0; i < model.getRowCount(); i++) {
                String[] nameParts = model.getValueAt(i, 1).toString().split(" ", 2);
                String firstName = nameParts[0];
                String lastName = nameParts.length > 1 ? nameParts[1] : "";
                // Save the raw grades (column index 3) so next load can recompute averages
                String rawGrades = model.getValueAt(i, 3) != null ? model.getValueAt(i, 3).toString() : "";
                pw.println(model.getValueAt(i, 0) + "," + firstName + "," + lastName + "," + rawGrades);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving CSV: " + e.getMessage());
        }
    }

    // ----- Add Row -----
    private void addRow() {
        String id = idField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String gradesText = gradeField.getText().trim();

        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gradesText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        double average = computeAverage(gradesText);
        if (average < 0) return;

        String fullName = firstName + " " + lastName;
        // Store the raw grades in the hidden 4th column so we can persist them
        model.addRow(new String[]{id, fullName, String.format("%.2f", average), gradesText});

        idField.setText(""); firstNameField.setText(""); lastNameField.setText(""); gradeField.setText("");
    }

    // ----- Compute average from comma-separated grades -----
    private double computeAverage(String gradesText) {
        String[] parts = gradesText.split(",");
        double sum = 0;
        for (String p : parts) {
            try { sum += Double.parseDouble(p.trim()); }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Grades must be numbers, separated by commas.");
                return -1;
            }
        }
        return sum / parts.length;
    }

    // ----- Delete Row -----
    private void deleteRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) model.removeRow(selectedRow);
        else JOptionPane.showMessageDialog(this, "Please select a row to delete.");
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch(Exception e){ e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> new StudentTableApp().setVisible(true));
    }
}
