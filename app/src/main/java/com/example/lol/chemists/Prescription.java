package com.example.lol.chemists;

import java.util.Date;

/**
 * Created by lol on 12/02/16.
 */
public class Prescription {

    public String docname;
    public Date date;
    public String id;
    public String mapJson;

    public Prescription(String name, Date date, String id, String mapJson) {
        this.docname = name;
        this.date = date;
        this.id = id;
        this.mapJson = mapJson;
    }

    public Date getDate() {
        return date;
    }

    public int compare(Prescription m1, Prescription m2) {
        //possibly check for nulls to avoid NullPointerException
        return m1.getDate().compareTo(m2.getDate());
    }

}
