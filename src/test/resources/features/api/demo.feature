Feature: Validate api/vehicles/getallmakes endpoint

  Scenario: Validate api/vehicles/getallmakes endpoint
    Given User have query params as below
      | format | year | manufacturer |
      | json   | 2015 | audi         |
    Given User have headers as below
      | Content-Type     | Accept           |
      | application/json | application/json |
    Given User performed request type: GET, for base url: AIO, endpoint: AIO_VEHICLES, Expected status code: 200
    Then User should see response body with below fields and Matcher Type: equalTo
      | Count | Message                        |
      | 11073 | Response returned successfully |