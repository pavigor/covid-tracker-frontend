package com.epam.devops.stream17.data;

import java.io.Serializable;

// Composite primary key
public class CovidDataId implements Serializable {
    private String date;

    private String countryCode;

    public CovidDataId() {}

    public CovidDataId(String date, String countryCode) {
        this.date = date;
        this.countryCode = countryCode;
    }
}
