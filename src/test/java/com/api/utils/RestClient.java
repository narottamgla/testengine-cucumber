package com.api.utils;

import com.api.dto.MethodType;
import com.api.hooks.Storage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;


@Log4j2
public class RestClient {

    private static RestClient restClient;

    public static RequestSpecBuilder requestSpecBuilder;
    public static RequestSpecification requestSpecification;
    public static ResponseSpecBuilder responseSpecBuilder;
    public static ResponseSpecification responseSpecification;

    private Filter logFilter = new CustomLogFilter();

    public static RestClient getInstance() {
        restClient = new RestClient();
        requestSpecBuilder = new RequestSpecBuilder();
        requestSpecification = requestSpecBuilder.build();
        return restClient;
    }

    public ResponseSpecification getResponseSpecification() {
        responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(200);
        responseSpecBuilder.expectResponseTime(lessThan(3L), TimeUnit.SECONDS);
        responseSpecification = responseSpecBuilder.build();
        return responseSpecification;
    }

    public RestClient queryParam(String param, String value) {
        requestSpecification.queryParam(param, value);
        return this;
    }

    public RestClient contentType(String contentType) {
        requestSpecification.contentType(contentType);
        return this;
    }

    public RestClient body(String map) {
        if(!(map ==null)) {
            requestSpecification.body(map).relaxedHTTPSValidation();
        }
        return this;
    }

    public RestClient queryParam(Map<String, String> queryMap) {
        requestSpecification.queryParams(queryMap);
        return this;
    }

    public RestClient headers(Map<String, String> headers) {
        requestSpecification.headers(headers);
        return this;
    }

    public RestClient header(String header, String value) {
        requestSpecification.headers(header, value);
        return this;
    }


    public RequestSpecification pathParam(String param, String value) {
        return requestSpecification.pathParam(param, value);
    }

    /**
     * Make call to api with Request specification and method type
     *
     * @param type
     * @param endPoint
     * @author narottam.singh
     */

    @SuppressWarnings("deprecation")
    public Response execute(MethodType type, String endPoint) {
        Response response = null;
        switch (type) {
            case GET:
                response = given().spec(requestSpecification).filter(logFilter).get(endPoint);
                break;
            case POST:
                response = given().spec(requestSpecification).filter(logFilter).post(endPoint);
                break;
            case PUT:
                response = given().spec(requestSpecification).filter(logFilter).put(endPoint);
                break;
            case DELETE:
                response = given().spec(requestSpecification).filter(logFilter).delete(endPoint);
                break;
            default:
                log.info("Method Type Not Supported: " + type);
                break;
        }

        if (logFilter instanceof CustomLogFilter) {
            CustomLogFilter customLogFilter = (CustomLogFilter) logFilter;
            Storage.getScenario().attach("\n" + "API Request: " + customLogFilter.getRequestBuilder() + "\nCurl Request: "
                    + customLogFilter.getCurlBuilder() + "API Response: " + customLogFilter.getResponseBuilder(), "text/plain", "API Request/Response");
        }
        return response;
    }

    public void contentType(ContentType type) {
        given().contentType(type);
    }
}
