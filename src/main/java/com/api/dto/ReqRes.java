package com.api.dto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.*;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class ReqRes {

    private MethodType call;

    private String body;

    private Response response;

    private Map<String, String> headers;

    private Map<String, String> queryParams;

    private Map<String, String> pathParams;

    private String baseUri;

    private String url;

    private ContentType contentType;

}
