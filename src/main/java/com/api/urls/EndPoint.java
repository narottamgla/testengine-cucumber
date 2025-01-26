package com.api.urls;

import lombok.Getter;
import lombok.Setter;

public enum EndPoint {


    AIO_VEHICLES("/api/vehicles/getallmakes");

    @Getter
    @Setter
    private String endPoint;
    EndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

}