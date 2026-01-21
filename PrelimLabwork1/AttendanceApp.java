import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/* 
 * AttendanceApp tracking application.
 * Created using Java Swing for GUI.
 * Automatically records Time In and generates an E-signature.
 * Form is centered and properly spaced using GridBagLayout.
 */

public class AttendanceApp extends JFrame {

    // Text fields for user input and auto-generated data
    private JTextField nameField, courseField, timeInField, signatureField;
    private JButton submitButton;

    public AttendanceApp() {
        // JFrame settings
        setTitle("Attendance Tracking App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window on screen

        // Main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); // spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        int row = 0; // track row index

        // -------------------- Title --------------------
        JLabel titleLabel = new JLabel("Attendance Tracking System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1; // reset for other rows

        // -------------------- Attendance Name --------------------
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        // -------------------- Course / Year --------------------
        JLabel courseLabel = new JLabel("Course / Year:");
        courseLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = ++row;
        panel.add(courseLabel, gbc);

        courseField = new JTextField(20);
        courseField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(courseField, gbc);

        // -------------------- Time In --------------------
        JLabel timeLabel = new JLabel("Time In:");
        timeLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = ++row;
        panel.add(timeLabel, gbc);

        timeInField = new JTextField(20);
        timeInField.setFont(fieldFont);
        timeInField.setEditable(false);
        timeInField.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        panel.add(timeInField, gbc);

        // -------------------- E-Signature --------------------
        JLabel signatureLabel = new JLabel("E-Signature:");
        signatureLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = ++row;
        panel.add(signatureLabel, gbc);

        signatureField = new JTextField(20);
        signatureField.setFont(fieldFont);
        signatureField.setEditable(false);
        signatureField.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        panel.add(signatureField, gbc);

        // -------------------- Submit Button --------------------
        submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setBackground(new Color(33, 150, 243));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setEnabled(false); // disabled until valid input
        submitButton.addActionListener(e -> recordAttendance());

        gbc.gridx = 0;
        gbc.gridy = ++row;
        gbc.gridwidth = 2; // button spans 2 columns
        panel.add(submitButton, gbc);

        // -------------------- Input validation listener --------------------
        nameField.getDocument().addDocumentListener(new InputListener());
        courseField.getDocument().addDocumentListener(new InputListener());
    }

    /* 
     * Listens for input changes to enable/disable the submit button
     */
    private class InputListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) { checkFormValidity(); }
        public void removeUpdate(DocumentEvent e) { checkFormValidity(); }
        public void changedUpdate(DocumentEvent e) { checkFormValidity(); }
    }

    /* 
     * Checks if all required fields are valid and enables/disables button
     */
    private void checkFormValidity() {
        boolean valid = !nameField.getText().trim().isEmpty()
                     && !courseField.getText().trim().isEmpty()
                     && nameField.getText().matches("[a-zA-Z ]+")
                     && courseField.getText().matches("[a-zA-Z ]+[0-9]+");
        submitButton.setEnabled(valid);
    }

    /* 
     * Validates name and course inputs before recording attendance
     */
    private boolean validateInput() {
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        if(name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name and Course/Year must not be empty.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this,
                    "Name must contain letters only.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!course.matches("[a-zA-Z ]+[0-9]+")) {
            JOptionPane.showMessageDialog(this,
                    "Course/Year format is invalid (example: BSIT 1).",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true; // input is valid
    }

    /* 
     * Records attendance data: time in + unique e-signature
     */
    private void recordAttendance() {
        if(!validateInput()) return; // stop if validation fails
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        timeInField.setText(timestamp);
        String signature = nameField.getText() + "-" + UUID.randomUUID().toString().substring(0,6);
        signatureField.setText(signature);

        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String recordLine = String.format("%s | %s | %s | %s", timestamp, name, course, signature);

        String filename = "attendance_records.txt"; // file will be created in the app directory
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(recordLine);
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to write attendance file: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Attendance Recorded Successfully!");
    }

    // Main method to run the application
    public static void main(String[] args) {
        new AttendanceApp().setVisible(true);
    }
}



