Feature: Testing microservices integration
  Workload database should be updated after successful POST requests to the Training web service endpoint

  @Microservices-integration
  Scenario: Adding new training (positive)
    When trainee 'Liam.Anderson' with password 'GhOYGnECBM' adds new training with 'Koa.King' and duration of 60
    Then application should handle request and return status code of 200
    And workload database should contain a record for 'Koa.King' with total duration of 60
