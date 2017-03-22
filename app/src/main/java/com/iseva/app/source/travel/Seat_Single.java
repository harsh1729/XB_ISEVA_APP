package com.iseva.app.source.travel;

/**
 * Created by xb_sushil on 3/18/2017.
 */

public class Seat_Single {

    private int Row;
    private int Col;
    private   int Height;
    private int Width;
    private   String SeatNo;
    private  String Gender;
    private    Boolean IsAisle;
    private    int Deck;
    private   Boolean IsAc;
    private    Boolean IsSleeper;
    private   Boolean IsAvailable;
    private    float Fare;
    private    float ChildFare;
    private  float InfantFare;





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
    }public String getGender()
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
    public Boolean getIsAisle()
    {
        return this.IsAisle;
    }
    public Boolean getIsSleeper()
    {
        return this.IsSleeper;
    }
    public Boolean getIsAvailable()
    {
        return this.IsAvailable;
    }

    public float getFare()
    {
        return this.Fare;
    }
    public float getChildFare()
    {
        return this.ChildFare;
    }
    public float getInfantFare()
    {
        return this.InfantFare;
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
    public void setGender(String  gender)
    {
        this.Gender = gender;
    }
    /*public void setIsAisle(Boolean isAisle)
    {
        this.IsAisle = isAisle;

    }*/
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
    public void setIsAvailable(Boolean isAvailable)
    {
        this.IsAvailable = isAvailable;
    }
    public void setFare(float fare)
    {
        this.Fare = fare;
    }
    public void setChildFare(float childFare)
    {
        this.ChildFare = childFare;
    }
    public void setInfantFare(float infantFare)
    {
        this.InfantFare = infantFare;
    }
    public void setIsAisle(Boolean isAisle)
    {
        this.IsAisle = isAisle;
    }

}
