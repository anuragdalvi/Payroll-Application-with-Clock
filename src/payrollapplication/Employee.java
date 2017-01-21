/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payrollapplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Anurag
 */
public class Employee {
    private int empid;
    private String fname;
    private String lname;
    private String address;
    private String phone;
    private double salary;
    private double bonus;
    private String payCategory;
    private String title;
    private Date date;
    private List<TimeSheet> timesheet;
    
        public Employee(int empid, String fname, String lname, String address, String phone, double salary, double bonus, String payCategory, String title, Date date){
        this.empid = empid;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phone = phone;
        this.salary = salary;
        this.bonus = bonus;
        this.payCategory = payCategory;
        this.title = title;
        this.date = date;
        this.timesheet = new ArrayList();
}
           private double grosspay = 0;

    public int getEmpid() {
        return empid;
    }

    public void setEmpid(int empid) {
        this.empid = empid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public String getPayCategory() {
        return payCategory;
    }

    public void setPayCategory(String payCategory) {
        this.payCategory = payCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TimeSheet> getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(List<TimeSheet> timesheet) {
        this.timesheet = timesheet;
    }

    public double getGrosspay() {
        return grosspay;
    }

    public void setGrosspay(double grosspay) {
        this.grosspay = grosspay;
    }
           
    public String toString(){
        return fname + "" + lname;
    }


}
