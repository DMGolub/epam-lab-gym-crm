Feature: Testing training REST API
  Users should be able to submit GET and POST requests to the Training web service endpoint

  @Endpoint-trainings-search-by-trainee
  Scenario: Getting all trainings by trainee (positive)
    When trainee 'Angela.Thompson' with password 'fRySAkoV1H' wants to get all own trainings
    Then the application should handle request and return status code of 200
    And user receives all the required trainee trainings

  @Endpoint-trainings-search-by-trainee
  Scenario: Unauthenticated user wants to get all training by trainee (negative)
    When unauthenticated user wants to get trainings by trainee 'Any.Trainee'
    Then the application should handle request and return status code of 401

  @Endpoint-trainings-search-by-trainer
  Scenario: Getting all training by trainer (positive)
    When trainer 'Kylian.Garcia' with password 'tuFRGpXIiA' wants to get all own trainings
    Then the application should handle request and return status code of 200
    And user receives all the required trainer trainings

  @Endpoint-trainings-search-by-trainer
  Scenario: Unauthenticated user wants to get all training by trainer (negative)
    When unauthenticated user wants to get trainings by trainer 'Any.Trainer'
    Then the application should handle request and return status code of 401

  @Endpoint-trainings
  Scenario: Trainee adds new training (positive)
    When trainee 'Robert.Martin' with password 'mEWtaomxkS' submits valid data for a new training with 'Koa.King'
    Then the application should handle request and return status code of 200

  @Endpoint-trainings
  Scenario: Trainer tries to add a new training (negative)
    When trainer 'Ezrah.Jackson' with password 'RRFqJnBunA' submits data for a new training with 'Angela.Thompson'
    Then the application should handle request and return status code of 403

  @Endpoint-trainings
  Scenario: Unauthenticated user wants to add new training (negative)
    When unauthenticated user wants to add new training
    Then the application should handle request and return status code of 401
