Feature: Testing login REST API
  Users should be able to submit POST and PUT requests to the Login web service endpoints

  @Endpoint-login
  Scenario: Logging in with valid credentials (positive)
    When user 'Evelyn.Clark' with password 'hDs5NQWjTe' submits credentials
    Then the app should handle request and return status code of 200
    And user receives access token

  @Endpoint-login
  Scenario: Logging in with invalid password (negative)
    When user 'Sophia.Walker' with password 'InvalidPassword' submits credentials
    Then the app should handle request and return status code of 401

  @Endpoint-login
  Scenario: Logging in with invalid userName (negative)
    When user 'Sophia.Walkerr' with password 'bSVA5mGOtI' submits credentials
    Then the app should handle request and return status code of 401

  @Endpoint-login
  Scenario: Logging in with both invalid credentials (negative)
    When user 'Invalid.UserName' with password 'InvalidPassword' submits credentials
    Then the app should handle request and return status code of 401

  @Endpoint-change-password
  Scenario: Password change (positive)
    When user 'Sophia.Walker' with password 'bSVA5mGOtI' submits valid password change request
    Then the app should handle request and return status code of 200
    And user receives a message 'Password changed successfully for user=Sophia.Walker'

  @Endpoint-change-password
  Scenario: Password change with invalid new password (negative)
    When user 'Steven.Wright' with password 'fO8sBTqIo0' submits change request with invalid password
    Then the app should handle request and return status code of 422

  @Endpoint-change-password
  Scenario: Password change with invalid old password (negative)
    When user 'Michelle.Adams' with password 'WrongPassword' submits change request with invalid password
    Then the app should handle request and return status code of 401
