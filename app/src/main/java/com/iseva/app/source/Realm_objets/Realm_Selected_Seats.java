package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 2/1/2017.
 */

public class Realm_Selected_Seats extends RealmObject {

    private   String Name;
    private   int Age;
    private   String SeatNo;


    private  String Gender;
    private    int Deck;
    private    Boolean IsSleeper;



    private    float Fare;

    private  String SelectedGender;

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getSeat_Type() {
        return Seat_Type;
    }

    public void setSeat_Type(int seat_Type) {
        Seat_Type = seat_Type;
    }

    private     float Fare_after_offer;
    private     int Seat_Type;

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

    public Boolean getIsSleeper()
    {
        return this.IsSleeper;
    }
    public float getFare()
    {
        return this.Fare;
    }
    public float getFare_after_offer()
    {
        return this.Fare_after_offer;
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

    public void setIsSleeper(Boolean isSleeper)
    {
        this.IsSleeper = isSleeper;
    }
    public void setFare(float fare)
    {
        this.Fare = fare;
    }
    public void setFare_after_offer(float fare_after_offer)
    {
        this.Fare_after_offer = fare_after_offer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSelectedGender() {
        return SelectedGender;
    }

    public void setSelectedGender(String selectedGender) {
        SelectedGender = selectedGender;
    }

}
