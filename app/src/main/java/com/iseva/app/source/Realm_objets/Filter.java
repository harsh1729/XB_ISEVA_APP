package com.iseva.app.source.Realm_objets;

import io.realm.RealmObject;

/**
 * Created by xb_sushil on 2/14/2017.
 */

public class Filter extends RealmObject {

    private String Id;
    private String Filter_tag;
    private String Filter_value;
    private String Filter_name;

    public String getId()
    {
        return Id;
    }
    public  String getFilter_tag()
    {
        return Filter_tag;
    }
    public String getFilter_value()
    {
        return Filter_value;
    }
    public String getFilter_name()
    {
        return Filter_name;
    }

    public void setId(String id)
    {
        this.Id = id;
    }
    public void setFilter_tag(String filter_tag)
    {
        this.Filter_tag = filter_tag;
    }
    public void setFilter_value(String filter_value)
    {
        this.Filter_value =filter_value;
    }
    public void setFilter_name(String filter_name)
    {
        this.Filter_name = filter_name;
    }

}
