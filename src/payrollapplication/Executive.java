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
public class Executive extends Employee{
    
      Executive(int empid, String fname, String lname, String address, String phone, double salary, double bonus, String payCategory, String title, Date date){
        super(empid, fname, lname,  address,  phone,  salary,  bonus,  payCategory,  title,  date);
    }
    
}
