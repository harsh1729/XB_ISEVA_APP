package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 1/28/2017.
 */

public class Realm_Seat_Details extends RealmObject {

    private int Row;
    private int Col;
    private   int Height;
    private int Width;
    private   String SeatNo;
    private  int Gender;
    private    int Deck;

    public int getSeatType() {
        return SeatType;
    }

    public void setSeatType(int seatType) {
        SeatType = seatType;
    }

    private    Boolean IsSleeper;
    private   Boolean IsAvailable;
    private    float Fare;
    private    double Fare_after_offer;

    private int SeatType;





    public int getRow()
    {
        return this.Row;
    }
    public int getCol()
    {
        return this.Col;
    }
    public int getHeight()
    {
        return this.Height;
    }
    public int getWidth()
    {
        return this.Width;
    }
    public String getSeatNo()
    {
        return this.SeatNo;
    }
    public int getDeck()
    {
        return this.Deck;
    }


    public Boolean getIsSleeper()
    {
        return this.IsSleeper;
    }
    public Boolean getIsAvailable()
    {
        return this.IsAvailable;
    }
    public int getGender()
    {
        return this.Gender;
    }

    public float getFare()
    {
        return this.Fare;
    }
    public double getFare_after_offer()
    {
        return this.Fare_after_offer;
    }


    public void setRow(int row)
    {
        this.Row = row;
    }
    public void setCol(int col)
    {
        this.Col = col;
    }
    public void setHeight(int height)
    {
        this.Height = height;
    }
    public void setWidth(int width)
    {
        this.Width = width;
    }
    public void setSeatNo(String seatNo)
    {
        this.SeatNo = seatNo;
    }
    public void setGender(int  gender)
    {
        this.Gender = gender;
    }

    public void setDeck(int deck)
    {
        this.Deck = deck;
    }

    public void setIsSleeper(Boolean isSleeper)
    {
        this.IsSleeper = isSleeper;
    }
    public void setIsAvailable(Boolean isAvailable)
    {
        this.IsAvailable = isAvailable;
    }
    public void setFare(float fare)
    {
        this.Fare = fare;
    }
    public void setFare_after_offer(double fare_after_offer)
    {
        this.Fare_after_offer = fare_after_offer;

    }







}
