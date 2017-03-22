package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 2/1/2017.
 */

public class Selected_Seats extends RealmObject {

    private   String SeatNo;
    private  String Gender;
    private    int Deck;
    private   Boolean IsAc;
    private    Boolean IsSleeper;
    private    float Fare;

    public String getSeatNo()
    {
        return this.SeatNo;
    }

    public String getGender()
    {
        return this.Gender;
    }
    public int getDeck()
    {
       return this.Deck;
    }

    public Boolean getIsAc()
    {
        return this.IsAc;
    }
    public Boolean getIsSleeper()
    {
        return this.IsSleeper;
    }
    public float getFare()
    {
        return this.Fare;
    }


    public void setSeatNo(String seatNo)
    {
        this.SeatNo = seatNo;
    }

    public void setGender(String gender)
    {
        this.Gender = gender;
    }
    public void setDeck(int deck)
    {
        this.Deck = deck;
    }

    public void setIsAc(Boolean isAc)
    {
        this.IsAc = isAc;
    }
    public void setIsSleeper(Boolean isSleeper)
    {
        this.IsSleeper = isSleeper;
    }
    public void setFare(float fare)
    {
        this.Fare = fare;
    }

}
