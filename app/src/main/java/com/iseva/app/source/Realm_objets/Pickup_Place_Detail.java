package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 1/30/2017.
 */

public class Pickup_Place_Detail extends RealmObject {

   private String PickupCode;
   private String PickupName;
   private String PkpTime;
    private String PickupArea;
   private String Address;
   private String Landmark;
   private String Contact;

    public String getPickupCode() {
        return PickupCode;
    }

    public void setPickupCode(String pickupCode) {
        PickupCode = pickupCode;
    }

    public String getPickupArea() {
        return PickupArea;
    }

    public void setPickupArea(String pickupArea) {
        PickupArea = pickupArea;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getPickupName()
    {
        return this.PickupName;
    }
    public String getPkpTime()
    {
        return this.PkpTime;

    }
    public String getAddress()
    {
        return this.Address;
    }
    public String getLandmark()
    {
        return this.Landmark;
    }







    public void setPickupName(String pickupName)
    {
        this.PickupName = pickupName;
    }
    public void setPkpTime(String pkpTime)
    {
        this.PkpTime = pkpTime;

    }
    public void setAddress(String address)
    {
        this.Address = address;
    }
    public void setLandmark(String landmark)
    {
        this.Landmark = landmark;
    }


}
