import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;
/*
 * Created by JFormDesigner on Sat Apr 30 15:33:14 ICT 2022
 */

public class MainFrame extends JFrame {


    ArrayList<Student> studentArrayList = new ArrayList<>();
    static String dbFile = "Student.txt";

    public static void main(String[] args) throws IOException {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public MainFrame() throws IOException {
        initComponents();
        returnStudentsToTable(readFile());
        List<String> list = Files.readAllLines(new File(dbFile).toPath(), Charset.defaultCharset());
        for (String line : list) {
            String[] res = line.split(",");
            Student user = new Student(res[0], res[1], res[2], res[3]);
            studentArrayList.add(user);
        }
    }

    // Clear text inside the text field when implement function
    private void Clear() {
        idTextField.setText("");
        nameTextField.setText("");
        gradeTextField.setText("");
    }


    // add function
    private void addBtn(ActionEvent e) {
        // TODO add your code here
        String name = nameTextField.getText().trim();
        String id = idTextField.getText().trim();
        String gender;
        if (maleRadioButton.isSelected()){
            gender = maleRadioButton.getText();
        }else if (femaleRadioButton.isSelected()){
            gender = femaleRadioButton.getText();
        }else if (otherRadioButton.isSelected()){
            gender = otherRadioButton.getText();
        }else {
            gender = "";
        }
        String grade = gradeTextField.getText().toUpperCase();

        if (!id.isEmpty() && !name.isEmpty()) {
            if (CheckId(id)) {
                JOptionPane.showMessageDialog(null, "ID is duplicated", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!CheckIdValid(id)) {
                JOptionPane.showMessageDialog(null, "Invalid ID! Try Again", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            } else if (!CheckNameValid(name)) {
                JOptionPane.showMessageDialog(null, "Name is invalid", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!CheckGradeValid(grade)){
                JOptionPane.showMessageDialog(null, "Grade must be P, M or D", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student student = new Student(id, name, gender, grade);
            studentArrayList.add(student);
            writeToFile(studentArrayList);
            Clear();
            clearTableContents();
            returnStudentsToTable(readFile());
        }
        else {
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill ID Required", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill Name Required", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    // delete function
    private void deleteBtn(ActionEvent event) {
        // TODO add your code here
        String id = idTextField.getText().trim();
        for (int i = 0; i < studentArrayList.size(); i++) {
            if (studentArrayList.get(i).getId().equals(id)) {
                studentArrayList.remove(i);
            }
        }
        writeToFile(studentArrayList);
        Clear();
        clearTableContents();
        returnStudentsToTable(readFile());
    }

    private void updateBtn(ActionEvent e) {
        // TODO add your code here
        String id = idTextField.getText().trim();
        String name = nameTextField.getText().trim();
        String gender;
        if (maleRadioButton.isSelected()){
            gender = maleRadioButton.getText();
        }else if (femaleRadioButton.isSelected()){
            gender = femaleRadioButton.getText();
        }else if (otherRadioButton.isSelected()){
            gender = otherRadioButton.getText();
        }else {
            gender = "";
        }
        String grade = gradeTextField.getText().toUpperCase();
        if (!id.isEmpty() && !name.isEmpty()) {
            if (!CheckNameValid(name)) {
                JOptionPane.showMessageDialog(null, "Name is invalid", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Student student : studentArrayList) {
                if (student.getId().equals(id)) {
                    student.setName(name);
                    student.setGender(gender);
                    student.setGrade(grade);
                }
            }
            writeToFile(studentArrayList);
            Clear();
            clearTableContents();
            returnStudentsToTable(readFile());
        } else {
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill ID Required", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill Name Required", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    // search Button
    private void searchBtn(ActionEvent e) {
        String id = idTextField.getText().trim();
        clearTableContents();
        if (!id.isEmpty()){
            for (Student student : studentArrayList) {
                if (student.getId().equals(id)) {
                    Student findedStudent = new Student(student.getId(), student.getName(), student.getGender(), student.getGrade());
                    clearTableContents();
                    returnFindedStudentsToTable(findedStudent);
                }
            }
        } else{
            returnStudentsToTable(readFile());
            JOptionPane.showMessageDialog(null, "Filling Search Field Required", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    // return user by Object
    public void returnFindedStudentsToTable(Student student){
        DefaultTableModel defaultTableModel = (DefaultTableModel) tableStudent.getModel();
        String[] findedUser = student.toString().split(",");
        defaultTableModel.addRow(findedUser);
    }

    // clear all the contents of table
    public void clearTableContents(){
        DefaultTableModel defaultTableModel = (DefaultTableModel) tableStudent.getModel();
        defaultTableModel.setRowCount(0);
    }

    // read file
    public Object[] readFile(){
        Object[] objects;
        try {
            FileReader fr = new FileReader(dbFile);
            BufferedReader bufferedReader = new BufferedReader(fr);
            // each lines to array
            objects = bufferedReader.lines().toArray();
            bufferedReader.close();
            return objects;
        } catch (IOException e) {

        }
        return null;
    }

    // write from list to file
    public static void writeToFile(ArrayList<Student> _users){
        try{
            FileWriter fw = new FileWriter(dbFile);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Student user: _users
            ) {
                bw.write(user.toString());
                bw.newLine();
            }
            bw.close();
            fw.close();

        }catch (Exception exception){

        }
    }

    // return user by Object array
    public void returnStudentsToTable(Object[] objects){
        DefaultTableModel defaultTableModel = (DefaultTableModel) tableStudent.getModel();
        int i = 0;
        while(i < objects.length) {
            String row = objects[i].toString().trim();
            String[] rows = row.split(",");
            defaultTableModel.addRow(rows);
            i++;
        }
    }

    // Check Information of Student
    // Check ID
    public boolean CheckId(String id){
        for (Student user : studentArrayList) {
            if (user.getId().equals(id)) {
                return true;
            }
        }return false;
    }

    // Check name valid
    public boolean CheckNameValid(String name){
        String regexPattern = "[^0-9]+";
        boolean validName = Pattern.compile(regexPattern)
                .matcher(name)
                .matches();
        if(validName){
            return true;
        }
        return false;
    }

    // Check ID valid
    public boolean CheckIdValid(String id){
        try {
            Integer.parseInt(id);
            return true;
        }catch (NumberFormatException e){

        }
        return false;
    }

    // Check grade valid
    public boolean CheckGradeValid(String grade){
        if (grade.equalsIgnoreCase("P") || grade.equalsIgnoreCase("M") || grade.equalsIgnoreCase("D")){
            return true;
        }
        return false;
    }

    private void otherRadioButton(ActionEvent e) {
        // TODO add your code here
    }

    private void femaleRadioButton(ActionEvent e) {
        // TODO add your code here
    }

    private void maleRadioButton(ActionEvent e) {
        // TODO add your code here
    }

    private void male(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel1 = new JPanel();
        nameLabel = new JLabel();
        genderLabel = new JLabel();
        gradeLabel = new JLabel();
        idLabel = new JLabel();
        nameTextField = new JTextField();
        idTextField = new JTextField();
        otherRadioButton = new JRadioButton();
        femaleRadioButton = new JRadioButton();
        maleRadioButton = new JRadioButton();
        gradeTextField = new JTextField();
        addButton = new JButton();
        searchButton = new JButton();
        updateButton = new JButton();
        deleteButton = new JButton();
        wrongInput = new JLabel();
        errorInput = new JLabel();
        scrollPane1 = new JScrollPane();
        tableStudent = new JTable();

        //======== this ========
        setTitle("Student Management");
        var contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder
            ( 0, 0 ,0 , 0) ,  "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border
            .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069al\u006fg", java .awt . Font. BOLD ,12 ) ,java . awt
            . Color .red ) ,panel1. getBorder () ) ); panel1. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void
            propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062or\u0064er" .equals ( e. getPropertyName () ) )throw new RuntimeException( )
            ;} } );

            //---- nameLabel ----
            nameLabel.setText("Full Name");

            //---- genderLabel ----
            genderLabel.setText("Gender");

            //---- gradeLabel ----
            gradeLabel.setText("Grade");

            //---- idLabel ----
            idLabel.setText("ID");

            //---- otherRadioButton ----
            otherRadioButton.setText("Other");
            otherRadioButton.addActionListener(e -> otherRadioButton(e));

            //---- femaleRadioButton ----
            femaleRadioButton.setText("Female");
            femaleRadioButton.addActionListener(e -> femaleRadioButton(e));

            //---- maleRadioButton ----
            maleRadioButton.setText("Male");
            maleRadioButton.addActionListener(e -> {
			maleRadioButton(e);
			male(e);
		});

            //---- addButton ----
            addButton.setText("Add");
            addButton.addActionListener(e -> {
			addBtn(e);
		});

            //---- searchButton ----
            searchButton.setText("Search");
            searchButton.addActionListener(e -> {
			searchBtn(e);
		});

            //---- updateButton ----
            updateButton.setText("Update");
            updateButton.addActionListener(e -> {
			updateBtn(e);
		});

            //---- deleteButton ----
            deleteButton.setText("Delete");
            deleteButton.addActionListener(e -> {
			deleteBtn(e);

		});

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(idLabel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(idTextField, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(searchButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(genderLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(maleRadioButton, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(femaleRadioButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(otherRadioButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(gradeLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(gradeTextField, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panel1Layout.createParallelGroup()
                                            .addComponent(wrongInput, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(errorInput, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(70, 70, 70)))
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(deleteButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(updateButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(idLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(idTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(addButton))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchButton))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(genderLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addComponent(maleRadioButton)
                                .addComponent(femaleRadioButton)
                                .addComponent(otherRadioButton))
                            .addComponent(updateButton))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(gradeLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                            .addComponent(gradeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteButton)
                            .addComponent(wrongInput)
                            .addComponent(errorInput, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(20, Short.MAX_VALUE))
            );
        }

        //======== scrollPane1 ========
        {

            //---- tableStudent ----
            tableStudent.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                    "ID", "Full Name", "Gender", "Grade"
                }
            ) {
                boolean[] columnEditable = new boolean[] {
                    false, false, false, false
                };
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
            {
                TableColumnModel cm = tableStudent.getColumnModel();
                cm.getColumn(0).setPreferredWidth(50);
                cm.getColumn(1).setPreferredWidth(150);
                cm.getColumn(2).setPreferredWidth(50);
                cm.getColumn(3).setPreferredWidth(50);
            }
            scrollPane1.setViewportView(tableStudent);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(panel1, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        var buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(otherRadioButton);
        buttonGroup1.add(femaleRadioButton);
        buttonGroup1.add(maleRadioButton);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }



    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel1;
    private JLabel nameLabel;
    private JLabel genderLabel;
    private JLabel gradeLabel;
    private JLabel idLabel;
    private JTextField nameTextField;
    private JTextField idTextField;
    private JRadioButton otherRadioButton;
    private JRadioButton femaleRadioButton;
    private JRadioButton maleRadioButton;
    private JTextField gradeTextField;
    private JButton addButton;
    private JButton searchButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JLabel wrongInput;
    private JLabel errorInput;
    private JScrollPane scrollPane1;
    private JTable tableStudent;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
