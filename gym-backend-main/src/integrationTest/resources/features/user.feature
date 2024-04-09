Feature: Testing user REST API
  Users should be able to submit POST and PATCH requests to the User web service endpoint

  @Endpoint-users-profile
  Scenario: Authenticated user changes own activity status (positive)
    When user 'Kevin.Baker' with password 'evI9iEgmPM' changes status of 'Kevin.Baker'
    Then app should handle request and return status code of 200

  @Endpoint-users-profile
  Scenario: Authenticated user changes not existing user activity status (negative)
    When user 'Deborah.Mitchell' with password 'hlcFAIXBAH' changes status of 'Not.Existing'
    Then app should handle request and return status code of 404

  @Endpoint-users-profile
  Scenario: Unauthenticated user changes activity status of any user (negative)
    When unauthenticated user changes status of 'Kevin.Baker'
    Then app should handle request and return status code of 401
