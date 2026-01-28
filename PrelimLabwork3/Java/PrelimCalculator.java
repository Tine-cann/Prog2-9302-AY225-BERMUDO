// File: PrelimCalculator.java

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// --- DocumentFilter to restrict JTextField input to numbers 0-100 ---
class NumberRangeFilter extends DocumentFilter {
    private final int min;
    private final int max;

    public NumberRangeFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    // Called when user types new characters
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);
        if (isValid(sb.toString())) super.insertString(fb, offset, string, attr);
    }

    // Called when user replaces text
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text);
        if (isValid(sb.toString())) super.replace(fb, offset, length, text, attrs);
    }

    // Validates numeric input within min and max
    private boolean isValid(String text) {
        if (text.isEmpty()) return true;
        try {
            int value = Integer.parseInt(text);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false; // rejects letters
        }
    }
}

public class PrelimCalculator extends JFrame implements ActionListener {

    // --- GUI Components ---
    private JTextField attendanceField, lab1Field, lab2Field, lab3Field;
    private JTextPane resultPane;
    private JButton calculateButton;

    public PrelimCalculator() {
        setTitle("Prelim Grade Calculator");
        setSize(650, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 245, 245)); // light background

        // --- Input Panel ---
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setLayout(new GridLayout(4, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Create labels and input fields
        inputPanel.add(createLabel("Attendance (0-100):", labelFont));
        attendanceField = createTextField(inputFont);
        inputPanel.add(attendanceField);

        inputPanel.add(createLabel("Lab Work 1 (0-100):", labelFont));
        lab1Field = createTextField(inputFont);
        inputPanel.add(lab1Field);

        inputPanel.add(createLabel("Lab Work 2 (0-100):", labelFont));
        lab2Field = createTextField(inputFont);
        inputPanel.add(lab2Field);

        inputPanel.add(createLabel("Lab Work 3 (0-100):", labelFont));
        lab3Field = createTextField(inputFont);
        inputPanel.add(lab3Field);

        add(inputPanel, BorderLayout.NORTH);

        // --- Restrict inputs to numbers 0-100 ---
        ((AbstractDocument) attendanceField.getDocument()).setDocumentFilter(new NumberRangeFilter(0, 100));
        ((AbstractDocument) lab1Field.getDocument()).setDocumentFilter(new NumberRangeFilter(0, 100));
        ((AbstractDocument) lab2Field.getDocument()).setDocumentFilter(new NumberRangeFilter(0, 100));
        ((AbstractDocument) lab3Field.getDocument()).setDocumentFilter(new NumberRangeFilter(0, 100));

        // --- Result Panel ---
        resultPane = new JTextPane();
        resultPane.setEditable(false);
        resultPane.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));
        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        calculateButton = new JButton("Calculate Prelim Exam Requirements");
        calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        calculateButton.setBackground(new Color(0, 123, 255));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        calculateButton.addActionListener(this);
        buttonPanel.add(calculateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // --- Helper methods to create labels and text fields ---
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        return field;
    }

    // --- Button click handler ---
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if any field is empty
        if(attendanceField.getText().isEmpty() || lab1Field.getText().isEmpty() ||
           lab2Field.getText().isEmpty() || lab3Field.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double attendance = Double.parseDouble(attendanceField.getText());
            double lab1 = Double.parseDouble(lab1Field.getText());
            double lab2 = Double.parseDouble(lab2Field.getText());
            double lab3 = Double.parseDouble(lab3Field.getText());

            // --- Weighted calculations ---
            double labAverage = (lab1 + lab2 + lab3) / 3.0;
            double classStanding = (attendance * 0.4) + (labAverage * 0.6);
            double requiredForPassing = (75 - (classStanding * 0.7)) / 0.3;
            double requiredForExcellent = (100 - (classStanding * 0.7)) / 0.3;
            double maxAchievable = (classStanding * 0.7) + (100 * 0.3);

            StyledDocument doc = resultPane.getStyledDocument();
            try { doc.remove(0, doc.getLength()); } catch (BadLocationException ex) { ex.printStackTrace(); }

            addStyledText(doc, "===== Prelim Grade Dashboard =====\n\n", Color.BLUE, true);
            addStyledText(doc, String.format("Lab Work Average: %.2f\n", labAverage), Color.BLACK, false);
            addStyledText(doc, String.format("Class Standing: %.2f\n\n", classStanding), Color.BLACK, false);

            addGradeResult(doc, "PASSING (75)", requiredForPassing, maxAchievable);
            addGradeResult(doc, "EXCELLENT (100)", requiredForExcellent, maxAchievable);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Display grade results with remarks ---
    private void addGradeResult(StyledDocument doc, String label, double requiredScore, double maxAchievable) {
        addStyledText(doc, "=== " + label + " ===\n", Color.MAGENTA, true);

        if (requiredScore > 100) {
            addStyledText(doc, String.format("Maximum achievable Prelim Grade: %.2f\n", maxAchievable), Color.BLACK, false);
            addStyledText(doc, "Remark: " + label + " is UNATTAINABLE, but the good news is you can already secure a HIGH and PASSING grade. Your maximum achievable grade is " + String.format("%.2f", maxAchievable) + ", which guarantees your academic standing.\n\n", new Color(0, 128, 0), true);
        } else if (requiredScore == 100) {
            addStyledText(doc, String.format("Required Prelim Exam: %.2f\n", requiredScore), Color.BLACK, true);
            addStyledText(doc, "Remark: To achieve " + label + ", you MUST score a perfect 100 on the Prelim Exam. Your current class standing leaves no room for error.\n\n", Color.RED, true);
        } else if (requiredScore <= 0) {
            addStyledText(doc, "No Prelim Exam score needed to reach " + label + ".\n", new Color(0, 128, 0), true);
            addStyledText(doc, "Remark: You have ALREADY SECURED " + label + " based on your class standing alone. Any score on the Prelim Exam will only improve your grade.\n\n", new Color(0, 128, 0), true);
        } else if (requiredScore < 40) {
            addStyledText(doc, String.format("Required Prelim Exam: %.2f\n", requiredScore), Color.BLACK, true);
            addStyledText(doc, "Remark: To achieve " + label + ", you need AT LEAST " + String.format("%.2f", requiredScore) + " on the Prelim Exam. This is VERY ACHIEVABLE with basic preparation.\n\n", new Color(0, 128, 0), true);
        } else if (requiredScore < 70) {
            addStyledText(doc, String.format("Required Prelim Exam: %.2f\n", requiredScore), Color.BLACK, true);
            addStyledText(doc, "Remark: To achieve " + label + ", you need AT LEAST " + String.format("%.2f", requiredScore) + " on the Prelim Exam. This requires MODERATE STUDY and preparation.\n\n", new Color(255, 140, 0), true);
        } else {
            addStyledText(doc, String.format("Required Prelim Exam: %.2f\n", requiredScore), Color.BLACK, true);
            addStyledText(doc, "Remark: To achieve " + label + ", you need AT LEAST " + String.format("%.2f", requiredScore) + " on the Prelim Exam. This requires INTENSIVE STUDY and strong performance.\n\n", Color.RED, true);
        }
    }

    private void addStyledText(StyledDocument doc, String text, Color color, boolean bold) {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, color);
        StyleConstants.setBold(attr, bold);
        try {
            doc.insertString(doc.getLength(), text, attr);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PrelimCalculator::new);
    }
}