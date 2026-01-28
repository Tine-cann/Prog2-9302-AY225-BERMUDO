import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

// Filter to allow only numbers in text fields
class NumberRangeFilter extends DocumentFilter {
    private final int min, max;
    public NumberRangeFilter(int min, int max) { this.min = min; this.max = max; }

    @Override
    public void insertString(FilterBypass fb,int offset,String string,AttributeSet attr)throws BadLocationException{
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset,string);
        if(isValid(sb.toString())) super.insertString(fb,offset,string,attr);
    }

    @Override
    public void replace(FilterBypass fb,int offset,int length,String text,AttributeSet attr)throws BadLocationException{
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset,offset+length,text);
        if(isValid(sb.toString())) super.replace(fb,offset,length,text,attr);
    }

    private boolean isValid(String text){
        if(text.isEmpty()) return true;
        try { int v = Integer.parseInt(text); return v>=min && v<=max; } 
        catch(Exception e){ return false; }
    }
}

public class PrelimCalculator extends JFrame implements ActionListener {

    private JComboBox<String> enrolleeType, absenceChoice;
    private JTextField startWeekField, excusedField, unexcusedField;
    private JTextField lab1Field, lab2Field, lab3Field;
    private JTextPane resultPane;
    private JButton calculateButton;

    public PrelimCalculator() {
        setTitle("Prelim Grade Calculator");
        setSize(950,900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15,15));
        getContentPane().setBackground(new Color(245,245,245));

        // Fonts
        Font sectionFont = new Font("Segoe UI Emoji", Font.BOLD, 16); // Emoji-capable font
        Font labelFont = new Font("Segoe UI Emoji", Font.PLAIN, 14);

        // --- Main panel ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15,15,15,15));
        mainPanel.setBackground(new Color(245,245,245));

        // --- Enrollment Panel ---
        JPanel enrolPanel = createTitledPanel("📋 ENROLLMENT INFORMATION", sectionFont);
        enrolPanel.setLayout(new GridLayout(2,2,10,10));
        enrolPanel.add(createLabel("Enrollment Type:", labelFont));
        enrolleeType = new JComboBox<>(new String[]{"Regular Enrollee","Late Enrollee"});
        enrolPanel.add(enrolleeType);

        enrolPanel.add(createLabel("If Late, Start Week (1-5):", labelFont));
        startWeekField = new JTextField();
        enrolPanel.add(startWeekField);

        mainPanel.add(enrolPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // --- Absence Panel ---
        JPanel absencePanel = createTitledPanel("📋 ABSENCE INFORMATION", sectionFont);
        absencePanel.setLayout(new GridLayout(3,2,10,10));
        absencePanel.add(createLabel("Did you have absences?", labelFont));
        absenceChoice = new JComboBox<>(new String[]{"No","Yes"});
        absencePanel.add(absenceChoice);

        absencePanel.add(createLabel("Excused Absences:", labelFont));
        excusedField = new JTextField();
        absencePanel.add(excusedField);

        absencePanel.add(createLabel("Unexcused Absences:", labelFont));
        unexcusedField = new JTextField();
        absencePanel.add(unexcusedField);

        mainPanel.add(absencePanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // --- Lab Panel ---
        JPanel labPanel = createTitledPanel("📚 LAB WORK SCORES", sectionFont);
        labPanel.setLayout(new GridLayout(3,2,10,10));
        labPanel.add(createLabel("Lab Work 1 (0-100):", labelFont));
        lab1Field = new JTextField();
        labPanel.add(lab1Field);

        labPanel.add(createLabel("Lab Work 2 (0-100):", labelFont));
        lab2Field = new JTextField();
        labPanel.add(lab2Field);

        labPanel.add(createLabel("Lab Work 3 (0-100):", labelFont));
        lab3Field = new JTextField();
        labPanel.add(lab3Field);

        mainPanel.add(labPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // --- Result Panel ---
        resultPane = new JTextPane();
        resultPane.setEditable(false);
        resultPane.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setPreferredSize(new Dimension(800,350));
        mainPanel.add(scrollPane);

        // --- Calculate button ---
        calculateButton = new JButton("Calculate Prelim Exam Requirements");
        calculateButton.setFont(new Font("Segoe UI",Font.BOLD,16));
        calculateButton.setBackground(new Color(0,123,255));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(new EmptyBorder(10,20,10,20));
        calculateButton.addActionListener(this);
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245,245,245));
        btnPanel.add(calculateButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnPanel);

        add(mainPanel, BorderLayout.CENTER);

        // --- Filters ---
        ((AbstractDocument)startWeekField.getDocument()).setDocumentFilter(new NumberRangeFilter(1,5));
        ((AbstractDocument)excusedField.getDocument()).setDocumentFilter(new NumberRangeFilter(0,5));
        ((AbstractDocument)unexcusedField.getDocument()).setDocumentFilter(new NumberRangeFilter(0,5));
        ((AbstractDocument)lab1Field.getDocument()).setDocumentFilter(new NumberRangeFilter(0,100));
        ((AbstractDocument)lab2Field.getDocument()).setDocumentFilter(new NumberRangeFilter(0,100));
        ((AbstractDocument)lab3Field.getDocument()).setDocumentFilter(new NumberRangeFilter(0,100));

        // --- Initial locking ---
        startWeekField.setEnabled(false); startWeekField.setEditable(false);
        excusedField.setEnabled(false); unexcusedField.setEnabled(false);

        // --- Action listeners for enabling fields ---
        enrolleeType.addActionListener(a->{
            boolean late = enrolleeType.getSelectedIndex()==1;
            startWeekField.setEnabled(late); startWeekField.setEditable(late);
            if(!late) startWeekField.setText("");
        });

        absenceChoice.addActionListener(a->{
            boolean hasAbs = absenceChoice.getSelectedIndex()==1;
            excusedField.setEnabled(hasAbs); unexcusedField.setEnabled(hasAbs);
            if(!hasAbs){ excusedField.setText(""); unexcusedField.setText(""); }
        });

        setVisible(true);
    }

    private JLabel createLabel(String text, Font font){
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        return lbl;
    }

    private JPanel createTitledPanel(String title, Font font){
        JPanel p = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2), title, TitledBorder.LEFT, TitledBorder.TOP, font, new Color(0,102,204));
        p.setBorder(tb);
        p.setBackground(Color.WHITE);
        return p;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        try{
            double lab1 = Double.parseDouble(lab1Field.getText());
            double lab2 = Double.parseDouble(lab2Field.getText());
            double lab3 = Double.parseDouble(lab3Field.getText());

            int excused = excusedField.getText().isEmpty()?0:Integer.parseInt(excusedField.getText());
            int unexcused = unexcusedField.getText().isEmpty()?0:Integer.parseInt(unexcusedField.getText());

            StyledDocument doc = resultPane.getStyledDocument();
            doc.remove(0,doc.getLength());

            // --- Auto fail: unexcused >=4 ---
            if(unexcused>=4){
                add(doc,"===== AUTOMATIC FAILURE =====\n\n",Color.RED,true);
                add(doc,"Reason: You have "+unexcused+" unexcused absences. 4 or more = automatic failure.\n",Color.RED,true);
                return;
            }

            double attendanceGrade = Math.max(0,100-(unexcused*20));
            double labAvg = (lab1+lab2+lab3)/3.0;
            double classStanding = attendanceGrade*0.4 + labAvg*0.6;

            double requiredPassing = (75 - classStanding*0.7)/0.3;
            double requiredExcellent = (100 - classStanding*0.7)/0.3;
            double maxAchievable = classStanding*0.7 + 100*0.3;

            add(doc,"===== 📊 PRELIM GRADE DASHBOARD =====\n\n",Color.BLUE,true);
            add(doc,String.format("Attendance Grade: %.2f\nLab Average: %.2f\nClass Standing: %.2f\n\n",attendanceGrade,labAvg,classStanding),Color.BLACK,false);

            addGradeResult(doc,"PASSING (75)",requiredPassing,maxAchievable);
            addGradeResult(doc,"EXCELLENT (100)",requiredExcellent,maxAchievable);

        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Please fill in all required fields correctly.");
        }
    }

    private void addGradeResult(StyledDocument doc,String label,double required,double max){
        add(doc,"=== "+label+" ===\n",Color.MAGENTA,true);
        if(required>100){
            add(doc,String.format("Max Achievable: %.2f\n",max),Color.BLACK,false);
            add(doc,"Remark: Even with perfect Prelim Exam, achieving "+label+" is impossible.\n",Color.RED,true);
            add(doc,"Do your best to maximize final grade.\n\n",new Color(0,128,0),false);
        }else if(required==100){
            add(doc,"Required Prelim Exam Score: 100\n",Color.BLACK,true);
            add(doc,"Remark: You must score perfectly to reach "+label+".\n",Color.RED,true);
            add(doc,"Intensive preparation and review needed.\n\n",Color.RED,false);
        }else if(required<=0){
            add(doc,"No Prelim Exam score required.\n",new Color(0,128,0),true);
            add(doc,"Remark: "+label+" is already secured.\n\n",new Color(0,128,0),false);
        }else if(required<40){
            add(doc,String.format("Required Prelim Exam: %.2f\n",required),Color.BLACK,true);
            add(doc,"Remark: Very achievable with basic preparation.\n\n",new Color(0,128,0),true);
        }else if(required<70){
            add(doc,String.format("Required Prelim Exam: %.2f\n",required),Color.BLACK,true);
            add(doc,"Remark: Requires moderate study and preparation.\n\n",Color.ORANGE,true);
        }else{
            add(doc,String.format("Required Prelim Exam: %.2f\n",required),Color.BLACK,true);
            add(doc,"Remark: Requires strong study and performance.\n\n",Color.RED,true);
        }
    }

    private void add(StyledDocument doc,String text,Color c,boolean bold){
        SimpleAttributeSet s=new SimpleAttributeSet();
        StyleConstants.setForeground(s,c);
        StyleConstants.setBold(s,bold);
        try{doc.insertString(doc.getLength(),text,s);}catch(Exception ignored){}
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(PrelimCalculator::new);
    }
}

