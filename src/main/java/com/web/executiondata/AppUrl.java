package com.web.executiondata;

import lombok.Getter;
import lombok.Setter;

public enum AppUrl {

    APP_URL("");

    @Getter
    @Setter
    private String url;
    AppUrl(String url){
        this.url=url;
    }
}
