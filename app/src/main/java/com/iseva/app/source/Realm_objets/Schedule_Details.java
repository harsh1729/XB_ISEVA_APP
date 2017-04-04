package com.iseva.app.source.Realm_objets;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 1/28/2017.
 */

public class Schedule_Details extends RealmObject {

    private int RouteScheduleId;
    private String JourneyDate;
    private int CompanyId;
    private String CompanyName;
    private int FromCityId;
    private String FromCityName;
    private int ToCityId;
    private String ToCityName;
    private Date DepartureTime;
    private String DepTime;
    private Date ArrivalTime;
    private String ArrTime;
    private String BusTypeName;
    private float Fare;
    private float Fare_after_offer;
    private float SeaterFareNAC;
    private float SeaterFareAC;
    private float SleeperFareNAC;
    private float SleeperFareAC;
    private Boolean HasAC;
    private Boolean HasNAC;
    private Boolean HasSeater;
    private Boolean HasSleeper;
    private String BusLabel;
    private float CommPCT;
    private float CommAmount;


    public int getRouteScheduleId()
    {
        return this.RouteScheduleId;
    }
    public String getJourneyDate()
    {
        return this.JourneyDate;
    }
    public int getCompanyId ()
    {
        return this.CompanyId;
    }
    public String getCompanyName()
    {
        return this.CompanyName;
    }
    public int getFromCityId()
    {
        return this.FromCityId;
    }
    public String getFromCityName()
    {
        return this.FromCityName;
    }
    public int getToCityId()
    {
        return this.ToCityId;
    }
    public String getToCityName()
    {
        return this.ToCityName;
    }

    public Date getDepartureTime()
    {
        return this.DepartureTime;
    }
    public  String getDepTime()
    {
        return this.DepTime;
    }
    public Date getArrivalTime()
    {
        return this.ArrivalTime;
    }
    public String getArrTime()
    {
        return this.ArrTime;
    }
    public float getFare()
    {
        return this.Fare;
    }
    public float getFare_after_offer()
    {
        return this.Fare_after_offer;
    }
    public float getSeaterFareNAC()
    {
        return this.SeaterFareNAC;
    }
    public float getSeaterFareAC()
    {
        return this.SeaterFareAC;
    }
    public float getSleeperFareNAC()
    {
        return this.SleeperFareNAC;
    }
    public float getSleeperFareAC()
    {
        return this.SleeperFareAC;
    }
    public float getCommPCT()
    {
        return this.CommPCT;
    }
    public float getCommAmount()
    {
        return this.CommAmount;
    }

    public Boolean getHasAC()
    {
        return this.HasAC;
    }
    public Boolean getHasNAC()
    {
        return this.HasNAC;
    }
    public Boolean getHasSeater()
    {
        return this.HasSeater;
    }
    public Boolean getHasSleeper()
    {
        return this.HasSleeper;
    }

    public  String getBusLabel()
    {
        return this.BusLabel;
    }

    public String getBusTypeName()
    {
        return this.BusTypeName;
    }




    public void setRouteScheduleId(int routeScheduleId)
    {
        this.RouteScheduleId = routeScheduleId;
    }
    public void setJourneyDate(String journeyDate)
    {
        this.JourneyDate = journeyDate;
    }
    public void setCompanyId (int companyId)
    {
        this.CompanyId = companyId;
    }
    public void setCompanyName(String companyName)
    {
        this.CompanyName = companyName;
    }
    public void setFromCityId(int fromCityId)
    {
         this.FromCityId = fromCityId;
    }
    public void setFromCityName(String fromCityName)
    {
        this.FromCityName = fromCityName;
    }
    public void setToCityId(int toCityId)
    {
        this.ToCityId = toCityId;
    }
    public void setToCityName(String toCityName)
    {
        this.ToCityName = toCityName;
    }

    public void setDepartureTime(Date departureTime)
    {
        this.DepartureTime = departureTime;
    }
    public  void setDepTime(String depTime)
    {
        this.DepTime = depTime;
    }
    public void setArrivalTime(Date arrivalTime)
    {
        this.ArrivalTime = arrivalTime;
    }
    public void setArrTime(String arrTime)
    {
        this.ArrTime = arrTime;
    }
    public void setFare(float fare)
    {
        this.Fare = fare;
    }
    public void setFare_after_offer(float fare_after_offer)
    {
        this.Fare_after_offer = fare_after_offer;
    }
    public void setSeaterFareNAC(float seaterFareNAC)
    {
        this.SeaterFareNAC = seaterFareNAC;
    }
    public void setSeaterFareAC(float seaterFareAC)
    {
        this.SeaterFareAC = seaterFareAC;
    }
    public void setSleeperFareNAC(float sleeperFareNAC)
    {
        this.SleeperFareNAC = sleeperFareNAC;
    }
    public void setSleeperFareAC(float sleeperFareAC)
    {
        this.SleeperFareAC = sleeperFareAC;
    }
    public void setCommPCT(float commPCT)
    {
        this.CommPCT = commPCT;
    }
    public void setCommAmount(float commAmount)
    {
        this.CommAmount = commAmount;
    }

    public void setHasAC(Boolean hasAC)
    {
        this.HasAC = hasAC;
    }
    public void setHasNAC(Boolean hasNAC)
    {
        this.HasNAC = hasNAC;
    }
    public void setHasSeater(Boolean hasSeater)
    {
        this.HasSeater = hasSeater;
    }
    public void setHasSleeper(Boolean hasSleeper)
    {
        this.HasSleeper = hasSleeper;
    }
    public void setBusLabel(String busLabel)
    {
        this.BusLabel = busLabel;
    }
    public void setBusTypeName(String busTypeName)
    {
        this.BusTypeName = busTypeName;
    }
}
