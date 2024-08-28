package org.acme.matnrtabling.domain;

import java.time.LocalDate;

public class WorkCalendar {
    private LocalDate workDate;
    private boolean workday;    // 是否是工作日
    
    public WorkCalendar(LocalDate workDate, boolean workday) {
        this.workDate = workDate;
        this.workday = workday;
    }

    @Override
    public String toString() {
        return workDate + "(" + workDate.getDayOfWeek() + ")";
    }

    public LocalDate getWorkDate() {
        return workDate;
    }
    public boolean getWorkDay() {
        return workday;
    }
    public String getMonth() {
        return workDate.getMonth().toString();
    }
    public int getMonthValue() {
        return workDate.getMonthValue();
    }
    public String getDayOfWeek() {
        return workDate.getDayOfWeek().toString();
    }
    public int getDayOfMonth() {
        return workDate.getDayOfMonth();
    }
}