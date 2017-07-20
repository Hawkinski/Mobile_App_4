package com.example.sachin.fms_client_v1;

/**
 * Created by sachin on 4/6/2016.
 */
public class StatusData {

    String title,date,location,complaint,priority,status,unit;

    public StatusData(String title, String date,String location,String complaint, String priority,String status,String unit){

        this.title=title;
        this.date= date;
        this.location=location;
        this.complaint=complaint;
        this.priority=priority;
        this.status=status;
        this.unit=unit;
    }
}
