/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payrollapplication;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Anurag
 */
public class TimeSheet {
    
    private Date startdate;
    private Date enddate;
    private int hours = 0;
    
    public TimeSheet(Date enddate, int hours){
        Date tempdate = enddate;
        this.enddate = enddate;
        this.hours = hours;
        Calendar c = Calendar.getInstance();
        c.setTime(enddate); //Convert date to calendar
        c.add(Calendar.DATE, -4); //biweekly
        tempdate.setTime(c.getTime().getTime());
        this.startdate = tempdate;
        
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
    
    
    
}
