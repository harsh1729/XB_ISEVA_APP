package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 1/28/2017.
 */

public class Realm_Selected_Bus_Details extends RealmObject {

    private int BusId;
    private String CompanyName;
    private String BusLabel;

    private String DepTime;
    private String ArrTime;
    private String DepDateTime;
    private String ArrDateTime;

    public String getDepDateTime() {
        return DepDateTime;
    }

    public void setDepDateTime(String depDateTime) {
        DepDateTime = depDateTime;
    }

    public String getArrDateTime() {
        return ArrDateTime;
    }

    public void setArrDateTime(String arrDateTime) {
        ArrDateTime = arrDateTime;
    }


    public int getBusId() {
        return BusId;
    }

    public void setBusId(int busId) {
        BusId = busId;
    }




    public String getCompanyName()
    {
        return this.CompanyName;
    }

    public  String getDepTimeOnly()
    {
        return this.DepTime;
    }
    public String getArrTimeOnly()
    {
        return this.ArrTime;
    }


    public  String getBusLabel()
    {
        return this.BusLabel;
    }




    public void setCompanyName(String companyName)
    {
        this.CompanyName = companyName;
    }

    public  void setDepTime(String depTime)
    {
        this.DepTime = depTime;
    }
    public void setArrTime(String arrTime)
    {
        this.ArrTime = arrTime;
    }


    public void setBusLabel(String busLabel)
    {
        this.BusLabel = busLabel;
    }
}
