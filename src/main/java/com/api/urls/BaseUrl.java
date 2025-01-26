package com.api.urls;
import lombok.Getter;
import lombok.Setter;

public enum BaseUrl {

    AIO("AIO_GOV");


    @Getter
    @Setter
    private String baseUrl;
    BaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
