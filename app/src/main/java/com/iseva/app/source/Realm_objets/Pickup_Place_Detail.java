package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 1/30/2017.
 */

public class Pickup_Place_Detail extends RealmObject {

   private int ProviderId;
   private int PickupId;
   private String PickupName;
   private String PkpTime;
   private String Address;
   private String Landmark;
   private String ProviderPickupId;
   private String Phone;


    public int getProviderId()
    {
        return this.ProviderId;
    }
    public int getPickupId()
    {
        return this.PickupId;
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
    public String getProviderPickupId()
    {
        return this.ProviderPickupId;
    }
    public String getPhone()
    {
        return this.Phone;
    }






    public void setProviderId(int providerId)
    {
        this.ProviderId = providerId;
    }
    public void setPickupId(int pickupId)
    {
        this.PickupId = pickupId;
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
    public void setProviderPickupId(String providerPickupId)
    {
        this.ProviderPickupId = providerPickupId;
    }
    public void setPhone(String phone)
    {
        this.Phone = phone;
    }
}
