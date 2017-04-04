package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 1/26/2017.
 */

public class Bus_routes_detail extends RealmObject{

    private int RouteScheduleId;
    private  int CompanyId ;
    private String CompanyName ;

    private String Duration;
    private String arrivaltime;
    private String departuretime;
   /* private Time ArriveTime;
    private Time departureTime;*/

    private String DepTime ;

    private String ArrTime ;


    private float Fare ;
    private float Fare_after_offer;

    private float SeaterFareNAC ;
    private float SeaterFareAC ;
    private float SleeperFareNAC ;
    private float SleeperFareAC ;
    private Boolean HasAC ;
    private Boolean HasNAC ;
    private Boolean HasSeater ;
    private Boolean HasSleeper ;
    private Boolean IsVolvo ;
    private String BusLabel ;

    private float CommPCT ;
    private float CommAmount ;
    private int AvailableSeats ;
    private String BusTypeName;
    private String BusNumber ;

    public String getDuration()
    {
        return Duration;
    }

    public String getArrivaltime()
    {
        return arrivaltime;
    }

    public String getDeparturetime()
    {
        return departuretime;
    }

    public int getRouteScheduleId()
    {
        return RouteScheduleId;
    }

    public int getCompanyId()
    {
        return CompanyId;
    }
    public String  getCompanyName()
    {
        return CompanyName;
    }

    public String getDepTime()
    {
        return DepTime;
    }

    public String getArrTime()
    {
        return ArrTime;
    }
    public float getFare()
    {
        return Fare;
    }
    public float getFare_after_offer()
    {
        return Fare_after_offer;
    }
    public float getSeaterFareNAC()
    {
        return SeaterFareNAC;
    }
    public float getSeaterFareAC()
    {
        return SeaterFareAC;
    }
    public float getSleeperFareNAC()
    {
        return SleeperFareNAC;
    }
    public float getSleeperFareAC()
    {
        return SleeperFareAC;
    }
    public Boolean getHasAC()
    {
        return HasAC;
    }
    public Boolean getHasNAC()
    {
        return HasNAC;
    }
    public Boolean getHasSeater()
    {
        return HasSeater;
    }
    public Boolean getHasSleeper()
    {
        return HasSleeper;
    }
    public Boolean getIsVolvo()
    {
        return IsVolvo;
    }
    public String getBusLabel()
    {
        return BusLabel;
    }
    public float getCommPCT()
    {
        return CommPCT;
    }
    public float getCommAmount()
    {
        return CommAmount;
    }
    public int getAvailableSeats()
    {
        return AvailableSeats;
    }
    public String getBusTypeName()
    {
        return BusTypeName;
    }
    public String getBusNumber()
    {
        return BusNumber;
    }





    public void setRouteScheduleId(int scheduleId)
    {
        this.RouteScheduleId = scheduleId;
    }

    public void setCompanyId(int companyId)
    {
       this.CompanyId = companyId;
    }
    public void  setCompanyName(String companyName)
    {
        this.CompanyName = companyName;
    }

    public void setDepTime(String depTime)
    {
        this.DepTime = depTime;
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
    public void setIsVolvo(Boolean isVolvo)
    {
        this.IsVolvo = isVolvo;
    }
    public void setBusLabel(String busLabel)
    {
        this.BusLabel = busLabel;
    }
    public void setCommPCT(float commPCT)
    {
        this.CommPCT = commPCT;
    }
    public void setCommAmount(float commAmount)
    {
        this.CommAmount = commAmount;
    }
    public void setAvailableSeats(int availableSeats)
    {
        this.AvailableSeats = availableSeats;
    }
    public void setBusTypeName(String busTypeName)
    {
        this.BusTypeName = busTypeName;
    }
    public void setBusNumber(String busNumber)
    {
        this.BusNumber = busNumber;
    }

    public void setDuration(String duration)
    {
        this.Duration = duration;
    }

    public void setArrivaltime(String arrivalTime)
    {
        this.arrivaltime = arrivalTime;
    }

    public void setDeparturetime(String departureTime)
    {
        this.departuretime = departureTime;
    }

}
