Feature: Testing training type REST API
  Users should be able to submit GET requests to the Training type web service endpoint

  @Endpoint-training-types
  Scenario: Getting all training types (positive)
    When user wants to get all training types
    Then application should handle request and return status code of 200
    And user receives all training types

  @Endpoint-training-types-id
  Scenario: Getting training type by id (positive)
    When user wants to get training type by id 1
    Then application should handle request and return status code of 200
    And user receives training type with id 1

  @Endpoint-training-types-id
  Scenario: Getting training type by id (negative)
    When user wants to get not existing training type by id 99
    Then application should handle request and return status code of 404
