package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by harsh on 20/08/17.
 */

public class Realm_City extends RealmObject {

    private String cityName;
    private String cityId;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
