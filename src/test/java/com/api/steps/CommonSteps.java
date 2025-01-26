package com.api.steps;

import com.api.dto.Matcher;
import com.api.dto.MethodType;
import com.api.dto.ReqRes;
import com.api.urls.BaseUrl;
import com.api.urls.EndPoint;
import com.api.utils.CommonUtils;
import com.api.utils.RestClient;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.CoreMatchers.equalTo;


@Log4j2
public class CommonSteps {


    ReqRes reqRes = new ReqRes();
    RestClient restClient;


    @Given("User have query params as below")
    public void userHaveQueryParamsAsBelow(final DataTable dataTable) {
        reqRes.setQueryParams(CommonUtils.getModifiableDataList(dataTable).get(0));
    }

    @Given("User have headers as below")
    public void userHaveHeadersAsBelow(final DataTable dataTable) {
        reqRes.setHeaders(CommonUtils.getModifiableDataList(dataTable).get(0));
    }

    @Given("User have path params as below")
    public void userHavePathParamsAsBelow(final DataTable dataTable) {
        reqRes.setPathParams(CommonUtils.getModifiableDataList(dataTable).get(0));
    }

    @Given("User have body as below with request file name as {}")
    public void userHaveBodyAsBelow(final String requestFileName, final DataTable dataTable) {
        reqRes.setBody(CommonUtils.buildRequestBody(requestFileName, CommonUtils.getModifiableDataList(dataTable).get(0)));
    }


    @Given("User performed request type: {}, for base url: {}, endpoint: {}, Expected status code: {int}")
    public void userPerformRequestForAPI(final MethodType call, final BaseUrl baseUrl, final EndPoint endPoint, final int expectedStatusCode) {
        String finalUrl = baseUrl.getBaseUrl() + endPoint.getEndPoint();
        log.info("PERFORMING REQUEST With  RequestType: " + call.name() + " for APIEndPoint: " + finalUrl);
        restClient = RestClient.getInstance();
        if (reqRes.getQueryParams() != null) {
            restClient.queryParam(reqRes.getQueryParams());
        }
        if (reqRes.getHeaders() != null) {
            restClient.headers(reqRes.getHeaders());
        }

        if (reqRes.getBody() != null) {
            restClient.body(reqRes.getBody());
        }
        Response response = restClient.execute(call, finalUrl);
        reqRes.setResponse(response);
        assertThat("Received Status code :" + response.getStatusCode()
                + "\n while expected status code is :" + expectedStatusCode, response.getStatusCode(), equalTo(expectedStatusCode));

    }

    @Given("User should see response body with below fields and Matcher Type: {}")
    public void validateJsonResponse(final Matcher matcher, final DataTable dataTable) {
        CommonUtils.validateResponsePath(matcher, dataTable, reqRes.getResponse().jsonPath());
        log.info("Response validated for datatable " + dataTable);
    }
}
