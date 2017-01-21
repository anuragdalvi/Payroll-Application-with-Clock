/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payrollapplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Anurag
 */
public class MainJFrame extends javax.swing.JFrame {
    
    EmployeeList employeeList;
    Date holidayDate;
     int time = 0;
    int run = 0;

    /**
     * Creates new form MainJFrame
     */
    private Date holiday = new Date(2015, Calendar.NOVEMBER, 26);

    
    public MainJFrame() {
        initComponents();
        employeeList = new EmployeeList();
        loadCSVFile();
        this.employeeList = employeeList;
        this.holidayDate = holidayDate;
        populateCombo();
              Integer [] date1 = new Integer [12];
            for(int i = 0; i < 12;i++) {
            date1[i] = i + 1;
    }
             Integer [] date2 = new Integer [60];
            for(int i = 0; i < 60 ; i++) {
            date2[i] = i + 1;
    }
        
            
    clkcom1.setModel(new javax.swing.DefaultComboBoxModel(date1));
    clkcom2.setModel(new javax.swing.DefaultComboBoxModel(date2));
    clkcom3.setModel(new javax.swing.DefaultComboBoxModel(date2));
    lblcomzone.setModel(new javax.swing.DefaultComboBoxModel(date1));
    lblcomzone2.setModel(new javax.swing.DefaultComboBoxModel(date2));
        
        new Thread(){
            public void run(){
                while(time == 0){
                    Integer com1 = (Integer)lblcomzone.getSelectedItem();
                    Integer com2 = (Integer)lblcomzone2.getSelectedItem();
                    Calendar  c  = new GregorianCalendar();
                    int hour = c.get(Calendar.HOUR) + com1 - 1;
                    int min = c.get(Calendar.MINUTE) + com2 - 1;
                    int sec = c.get(Calendar.SECOND);
                    int ampm = c.get(Calendar.AM_PM);
                    
                     
                    
                    String daynight = "";
                    if(ampm == 1){
                        daynight = "PM";
                    }
                    else{
                        daynight = "AM";
                    }
                    String time1 = hour + ":" + min + ":" + sec + daynight;
                    lblclock.setText(time1);
                    
          
                }
            }
        }.start();
        new Thread(){
             public void run(){
                 while( run == 0){
             
             Calendar  c  = new GregorianCalendar();
             Integer com1 = (Integer)clkcom1.getSelectedItem();
             Integer com2 = (Integer)clkcom2.getSelectedItem();
             Integer com3 = (Integer)clkcom3.getSelectedItem();
             
                    int hour = c.get(Calendar.HOUR);
                    int min = c.get(Calendar.MINUTE);
                    int sec = c.get(Calendar.SECOND);
                    int ampm = c.get(Calendar.AM_PM);
                    
                   
                    
                    if(hour == com1 && min == com2)
                    {
                    txtClock2.setText("Wake UP");
                    }
                    else{
                       txtClock2.setText("SLEEP"); 
                    }
        }
             }
            
      
                
                    
        }.start();
    }

     private void loadCSVFile() {

        try {
            Scanner file = new Scanner(new BufferedReader(new FileReader("Payroll Application.csv")));

            file.nextLine(); //skip the first line
            while (file.hasNextLine()) {
                // file.nextLine();

                String inputLine = file.nextLine();

                Scanner in = new Scanner(inputLine);
                in.useDelimiter("\"");

                String firstPart = in.next();
                String[] parts = firstPart.split(",");
                int empId = Integer.parseInt(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                String address = in.next();
                String lastPart = in.next();
                String[] part3 = lastPart.split(",");
                String phoneNum = part3[1];
                double salary = Double.parseDouble(part3[2]);
                double bonus = 0;
                if(!part3[3].isEmpty()){
                    bonus = Double.parseDouble(part3[3]);
                }
                String payCategory = part3[4];
                String title = part3[5];
                Date date=null;
                if (in.hasNext()) {
                    String lastPart1 = in.next();
                    String dateEx = lastPart1.replaceAll("th|rd|st|nd","");
                    DateFormat format = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);
                    try {
                        date = format.parse(dateEx);
                    } catch (ParseException ex) {
                        Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    date = null;
                }
                ///////////////////Factory Design Patern////////////////
                Employee employee = EmployeeFactory.CreateEmployee(empId, firstName, lastName, address, phoneNum,salary, bonus, payCategory, title, date);
                employeeList.getEmployeelist().add(employee);
              
                in.close();
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

    }
     
         private Boolean checkHolidayDate(Date startDate, Date endDate, Date holidayDate) { 
        if (holidayDate != null && startDate != null && endDate != null) {
            if (holidayDate.after(startDate) && holidayDate.before(endDate)) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
     
       private void populateCombo() {
        comboxpay1.removeAllItems();
        for (Employee emp : employeeList.getEmployeelist()) {
            comboxpay1.addItem(emp);
        }
    }
     
      private void populatePayroll() {
        DefaultTableModel employeesTable = (DefaultTableModel) tblpay1.getModel();
        employeesTable.setRowCount(0);
        double payroll = 0;
        int employessCount = 0;
        for (Employee emp : employeeList.getEmployeelist()) {
            employessCount++;
            for(TimeSheet ts : emp.getTimesheet()){
            Object[] obj = new Object[10];
            obj[0] = ts.getEnddate();
            obj[1] = emp.getEmpid();
            obj[2] = emp.getTitle();
            obj[3] = emp.getFname();
            obj[4] = emp.getLname();
            obj[5] = emp.getPayCategory();
            obj[6] = ts.getHours();
            obj[7] = emp.getSalary();
            obj[8] = emp.getBonus();
            double temp = calculateGrossPay(emp,ts.getStartdate(), ts.getEnddate(), emp.getSalary(), emp.getTitle(), ts.getHours());
            obj[9] = temp;
            payroll = payroll+temp;
                    
            
            employeesTable.addRow(obj);
            }
        }
        txtpay7.setText(String.valueOf(employessCount));
        txtpay9.setText(String.valueOf(payroll));
    }
    
     private double calculateGrossPay(Employee emp,Date startDate, Date endDate, double salary, String title, int hours) {
         if(title.equalsIgnoreCase("Student")){
             double tempSal = salary*hours;
             if(checkHolidayDate(startDate, endDate, holidayDate)){
                    tempSal = tempSal - (salary*8);
                }
             return tempSal;
         }
        else if (title.equalsIgnoreCase("Staff")) {
            return ((salary / 365) * (endDate.getDate() - startDate.getDate()));
        }
        else if (title.equalsIgnoreCase("Executive")){
                double tempSal = ((salary / 365) * (endDate.getDate() - startDate.getDate()));
                if(checkHolidayDate(startDate, endDate, emp.getDate())){
                    tempSal = tempSal + emp.getBonus();
                }
            return tempSal;
        }
        else if (title.equalsIgnoreCase("Volunteer")){
            return 0;
        }
        else if(title.equalsIgnoreCase("Teacher")) {
            double tempSal = (salary*hours);
            return  tempSal;
        }
        return 0;
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblpay1 = new javax.swing.JLabel();
        lblpay2 = new javax.swing.JLabel();
        txtpay1 = new javax.swing.JTextField();
        lblpay3 = new javax.swing.JLabel();
        txtpay2 = new javax.swing.JTextField();
        lblpay4 = new javax.swing.JLabel();
        txtpay3 = new javax.swing.JTextField();
        lblpay5 = new javax.swing.JLabel();
        comboxpay1 = new javax.swing.JComboBox();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        lblpay6 = new javax.swing.JLabel();
        btnpay1 = new javax.swing.JButton();
        butpay2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblpay1 = new javax.swing.JTable();
        lblpay7 = new javax.swing.JLabel();
        lblpay8 = new javax.swing.JLabel();
        txtpay7 = new javax.swing.JTextField();
        txtpay9 = new javax.swing.JTextField();
        lblhour1 = new javax.swing.JLabel();
        lblmin2 = new javax.swing.JLabel();
        lblclock = new javax.swing.JLabel();
        clkcom1 = new javax.swing.JComboBox();
        clkcom2 = new javax.swing.JComboBox();
        clkcom3 = new javax.swing.JComboBox();
        txtClock2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblcomzone = new javax.swing.JComboBox();
        lblzone1 = new javax.swing.JLabel();
        lblcomzone2 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblpay1.setText("PAYROLL APPLICATION");

        lblpay2.setText("Employee ID:");

        lblpay3.setText("Employee Name:");

        lblpay4.setText("Hours:");

        lblpay5.setText("Find Employee:");

        comboxpay1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblpay6.setText("Date:");

        btnpay1.setText("OK");
        btnpay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpay1ActionPerformed(evt);
            }
        });

        butpay2.setText("PROCESS");
        butpay2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butpay2ActionPerformed(evt);
            }
        });

        tblpay1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Employee ID", "Title", "First Name", "Last Name", "Pay Category", "Hours", "Salary", "Bonus", "Gross Pay"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblpay1);

        lblpay7.setText("Employee Total:");

        lblpay8.setText("Payroll Total:");

        lblhour1.setText("set hour");

        lblmin2.setText("set min.");

        lblclock.setFont(new java.awt.Font("Lucida Sans", 1, 24)); // NOI18N

        jLabel1.setText("Alarm:");

        lblcomzone.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        lblzone1.setText("ZONE GMT:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblpay6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(butpay2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(btnpay1)))
                        .addGap(305, 305, 305))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(lblcomzone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(20, 20, 20))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(lblzone1)
                                            .addGap(18, 18, 18)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblhour1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addComponent(lblclock, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblcomzone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblmin2))
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(clkcom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtClock2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(clkcom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                                        .addComponent(clkcom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(365, 365, 365)
                        .addComponent(lblpay1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(lblpay2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtpay1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(lblpay3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtpay2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(lblpay4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtpay3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(lblpay5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboxpay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(287, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(407, 407, 407)
                .addComponent(lblpay7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtpay7, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblpay8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtpay9, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblpay1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblpay2)
                    .addComponent(txtpay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblpay3)
                    .addComponent(txtpay2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblpay4)
                    .addComponent(txtpay3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblpay5)
                    .addComponent(comboxpay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblpay6))
                    .addComponent(btnpay1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(butpay2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblclock, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(clkcom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(clkcom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(clkcom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblzone1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblhour1)
                                .addGap(5, 5, 5)
                                .addComponent(lblcomzone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(lblmin2)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtClock2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblcomzone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)))
                        .addGap(30, 30, 30)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblpay7)
                    .addComponent(lblpay8)
                    .addComponent(txtpay7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtpay9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void butpay2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butpay2ActionPerformed
        // TODO add your handling code here:
        populatePayroll();
    }//GEN-LAST:event_butpay2ActionPerformed

    private void btnpay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpay1ActionPerformed
        // TODO add your handling code here:
        boolean empFlag=false;
        if (!txtpay1.getText().isEmpty()) {
            for (Employee emp : employeeList.getEmployeelist()) {
                if (Integer.parseInt(txtpay1.getText()) == emp.getEmpid()) {
                    txtpay1.setText(emp.getFname() + "," + emp.getLname());
                    empFlag = true;
                }

            }
            if (!empFlag) {
                JOptionPane.showMessageDialog(null, "Employee ID does not match");
                return;

            }
        }
        String hours = txtpay3.getText();
        if (hours.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Enter the number of hours");
            return;
        } else if (Integer.parseInt(txtpay3.getText()) > 40) {
            JOptionPane.showMessageDialog(null, "Please check the number of hours(Max hours per week is 40)");
            return;
        }
        Date selectedEndDate = jDateChooser1.getDate();
        if (selectedEndDate.getDay() != 5) {
            JOptionPane.showMessageDialog(null, "Please select a Friday");
            return;
        }

        TimeSheet paySheet = new TimeSheet(selectedEndDate, Integer.parseInt(hours));
        if (txtpay1.getText().isEmpty()) {
            Employee emp = (Employee) comboxpay1.getSelectedItem();
            emp.getTimesheet().add(paySheet);
        } else {
            for (Employee emp : employeeList.getEmployeelist()) {
                if (emp.getEmpid() == Integer.parseInt(txtpay1.getText())) {
                    emp.getTimesheet().add(paySheet);
                }
            }
        }
    }//GEN-LAST:event_btnpay1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnpay1;
    private javax.swing.JButton butpay2;
    private javax.swing.JComboBox clkcom1;
    private javax.swing.JComboBox clkcom2;
    private javax.swing.JComboBox clkcom3;
    private javax.swing.JComboBox comboxpay1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblclock;
    private javax.swing.JComboBox lblcomzone;
    private javax.swing.JComboBox lblcomzone2;
    private javax.swing.JLabel lblhour1;
    private javax.swing.JLabel lblmin2;
    private javax.swing.JLabel lblpay1;
    private javax.swing.JLabel lblpay2;
    private javax.swing.JLabel lblpay3;
    private javax.swing.JLabel lblpay4;
    private javax.swing.JLabel lblpay5;
    private javax.swing.JLabel lblpay6;
    private javax.swing.JLabel lblpay7;
    private javax.swing.JLabel lblpay8;
    private javax.swing.JLabel lblzone1;
    private javax.swing.JTable tblpay1;
    private javax.swing.JTextField txtClock2;
    private javax.swing.JTextField txtpay1;
    private javax.swing.JTextField txtpay2;
    private javax.swing.JTextField txtpay3;
    private javax.swing.JTextField txtpay7;
    private javax.swing.JTextField txtpay9;
    // End of variables declaration//GEN-END:variables
}
