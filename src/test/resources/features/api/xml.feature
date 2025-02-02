Feature: Validate api/vehicles/getallmakes endpoint

  Scenario: Validate api/vehicles/getallmakes endpoint

    Given User have body as below with request xml file name as "order"
      | XPath                                  | Attribute      | NewValue |
      | //Order[@AllocationRuleID="USAG_NAPA"] | EnterpriseCode | shruti   |
      | *//PaymentMethod[@PaymentType="COD"]   | MaxChargeLimit | 10       |
