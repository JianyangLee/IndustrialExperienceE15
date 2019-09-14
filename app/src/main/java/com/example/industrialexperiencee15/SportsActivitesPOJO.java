package com.example.industrialexperiencee15;

import com.google.android.gms.maps.model.LatLng;

public class SportsActivitesPOJO {

    private LatLng latLangOfFacility;
    private String nameOfFacility;
    private String street;
    private String suburb;
    private Integer postcode;
    private String  description;

    public LatLng getLatLangOfFacility() {
        return latLangOfFacility;
    }

    public void setLatLangOfFacility(LatLng latLangOfFacility) {
        this.latLangOfFacility = latLangOfFacility;
    }

    public String getNameOfFacility() {
        return nameOfFacility;
    }

    public void setNameOfFacility(String nameOfFacility) {
        this.nameOfFacility = nameOfFacility;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
