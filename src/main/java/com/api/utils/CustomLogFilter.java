package com.api.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.Optional;

public class CustomLogFilter implements Filter {
    private StringBuilder requestBuilderLogs;
    private StringBuilder responseBuilderLogs;

    private StringBuilder curlBuilderLogs;


    @Override
    public Response filter(final FilterableRequestSpecification filterableRequestSpecification, final FilterableResponseSpecification filterableResponseSpecification, final FilterContext filterContext) {
        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
        requestBuilderLogs = new StringBuilder();
        responseBuilderLogs = new StringBuilder();
        curlBuilderLogs = new StringBuilder();
        try {
            requestBuilderLogs = new StringBuilder();
            requestBuilderLogs.append("\n");
            requestBuilderLogs.append(
                    "Request method: " + Optional.ofNullable(objectValidation(filterableRequestSpecification.getMethod())).orElse("Null"));
            requestBuilderLogs.append("\n");
            requestBuilderLogs.append(
                    "Request URI: " + Optional.ofNullable(objectValidation(filterableRequestSpecification.getURI())).orElse("Null"));
            requestBuilderLogs.append("\n");
            requestBuilderLogs.append(
                    "Form Params: " + Optional.ofNullable(objectValidation(filterableRequestSpecification.getFormParams())).orElse("Null"));
            requestBuilderLogs.append("\n");
            requestBuilderLogs.append("Request Param: " + Optional.ofNullable(objectValidation(
                    filterableRequestSpecification.getRequestParams())).orElse("Null"));
            requestBuilderLogs.append("\n");

            requestBuilderLogs.append("Request Query Param: " + Optional.ofNullable(objectValidation(
                    filterableRequestSpecification.getQueryParams())).orElse("Null"));
            requestBuilderLogs.append("\n");

            requestBuilderLogs.append(
                    "Headers: " + Optional.ofNullable(objectValidation(filterableRequestSpecification.getHeaders())).orElse("Null"));
            requestBuilderLogs.append("\n");
            requestBuilderLogs.append(
                    "Cookies: " + Optional.ofNullable(objectValidation(filterableRequestSpecification.getCookies())).orElse("Null"));
            requestBuilderLogs.append("\n");
            requestBuilderLogs.append("Proxy: " + Optional.ofNullable(objectValidation(
                    filterableRequestSpecification.getProxySpecification())).orElse("Null"));
            requestBuilderLogs.append("\n");
            requestBuilderLogs.append(
                    "Body: " + Optional.ofNullable(objectValidation(filterableRequestSpecification.getBody())).orElse("Null"));
            requestBuilderLogs.append("\n" + "\n");
            requestBuilderLogs.append(
                    "*****************************************************************");
            requestBuilderLogs.append("\n");
            curlBuilderLogs.append("\n");
            curlBuilderLogs.append(
                    "curl --location --request " + Optional.ofNullable(objectValidation(filterableRequestSpecification.getMethod())).orElse("Null"));
            curlBuilderLogs.append(
                    " '" + Optional.ofNullable(objectValidation(filterableRequestSpecification.getURI())).orElse("Null") + "' ");
            for (Header eachHeader : filterableRequestSpecification.getHeaders()) {
                curlBuilderLogs.append("\n --header '" + eachHeader.toString().replace("=", ":") + "'");
            }
            curlBuilderLogs.append("\n --data-raw '" + Optional.ofNullable(objectValidation(filterableRequestSpecification.getBody())).orElse("Null") + "'\n");
            curlBuilderLogs.append("\n");
            curlBuilderLogs.append(
                    "*****************************************************************");
            curlBuilderLogs.append("\n");
//            log.info(curlBuilderLogs);

            responseBuilderLogs.append("\n");
            responseBuilderLogs.append("Status Code: " + Optional.ofNullable(response.getStatusCode()).orElse(000));
            responseBuilderLogs.append("\n");
            responseBuilderLogs.append("Status Line: " + Optional.ofNullable(response.getStatusLine()).orElse("Null"));
            responseBuilderLogs.append("\n");
            responseBuilderLogs.append("Response Cookies: " + Optional.ofNullable(response.getDetailedCookies()).orElse(new Cookies()));
            responseBuilderLogs.append("\n");
            responseBuilderLogs.append("Response Content Type: " + Optional.ofNullable(response.getContentType()).orElse("Null"));
            responseBuilderLogs.append("\n");
            responseBuilderLogs.append("Response Headers: " + Optional.ofNullable(response.getHeaders()).orElse(new Headers()));
            responseBuilderLogs.append("\n");
            responseBuilderLogs.append("Response Body: " + "\n" + Optional.ofNullable(response.getBody().prettyPrint()).orElse("Null"));
        } catch (Exception e) {
            e.printStackTrace();
            requestBuilderLogs.append("\n");
            requestBuilderLogs.append("Unable to get Request Details");
            responseBuilderLogs.append("\n");
            responseBuilderLogs.append("Unable to get Response Details");
        }

        return response;
    }

    public String getRequestBuilder() {
        try {
            return requestBuilderLogs.toString();
        } catch (Exception e) {
            return "Unable to get request to attach";
        }
    }

    public String getResponseBuilder() {
        try {
            return responseBuilderLogs.toString();
        } catch (Exception e) {
            return "Unable to get response to attach";
        }
    }

    public String objectValidation(final Object o) {
        if (o == null)
            return null;
        else
            return o.toString();
    }

    public String getCurlBuilder() {
        try {
            return curlBuilderLogs.toString();
        } catch (Exception e) {
            return "Unable to get response to attach";
        }
    }

}
