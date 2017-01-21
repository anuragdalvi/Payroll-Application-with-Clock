/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payrollapplication;

import java.util.Date;

/**
 *
 * @author Anurag
 */
public class EmployeeFactory {
    
      public static Employee CreateEmployee (int empid, String fname, String lname, String address, String phone, double salary, double bonus, String payCategory, String title, Date date){
        if(title.equalsIgnoreCase("Student"))
            return new Student(empid, fname, lname,  address,  phone,  salary,  bonus,  payCategory,  title,  date);
          else if(title.equalsIgnoreCase("Teacher"))
              return new Teacher(empid, fname, lname,  address,  phone,  salary,  bonus,  payCategory,  title,  date);
          else if(title.equalsIgnoreCase("Executive"))
              return new Executive (empid, fname, lname,  address,  phone,  salary,  bonus,  payCategory,  title,  date);
          else if(title.equalsIgnoreCase("Staff"))
              return new Staff(empid, fname, lname,  address,  phone,  salary,  bonus,  payCategory,  title,  date);
          else 
              return new Volunteer(empid, fname, lname,  address,  phone,  salary,  bonus,  payCategory,  title,  date);
        
    }
    
}
