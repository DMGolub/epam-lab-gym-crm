Feature: Testing trainer workload REST API
  Users should be able to submit POST requests to the Trainer workload web service endpoint

  @Endpoint-trainer-workload
  Scenario: Adding new training (positive)
    When service submits valid trainer workload 'ADD' request with duration of 60
    Then application should handle request and return status code of 200
    And database should contain the expected record with duration of 60

  @Endpoint-trainer-workload
  Scenario: Deleting training (positive)
    When service submits valid trainer workload 'DELETE' request with duration of 20
    Then application should handle request and return status code of 200
    And database should contain the expected record with duration of 40

  @Endpoint-trainer-workload
  Scenario: Deleting training (negative)
    When service submits valid trainer workload 'DELETE' request with duration of 200
    Then application should handle request and return status code of 200
    And database should contain the expected record with duration of 40

  @Endpoint-trainer-workload
  Scenario: Wrong request action type (negative)
    When service submits valid trainer workload 'UNKNOWN' request with duration of 30
    Then application should handle request and return status code of 400
    And service gets message 'Invalid action type: UNKNOWN'

  @Endpoint-trainer-workload
  Scenario: Unauthenticated service tries to add new training (negative)
    When unauthenticated service submits valid trainer workload add request
    Then application should handle request and return status code of 401
