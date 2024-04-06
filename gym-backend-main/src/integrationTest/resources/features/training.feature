Feature: Testing training REST API
  Users should be able to submit GET and POST requests to the Training web service endpoint

  Scenario: Getting all training by trainee (positive)
    When trainee 'Angela.Thompson' with password 'fRySAkoV1H' wants to get all own trainings
    Then the application should handle request and return status code of 200
    And user receives all the required trainee trainings

  Scenario: Unauthenticated user wants to get all training by trainee (negative)
    When unauthenticated user wants to get trainings by trainee 'Any.Trainee'
    Then the application should handle request and return status code of 401

  Scenario: Getting all training by trainer (positive)
    When trainer 'Kylian.Garcia' with password 'tuFRGpXIiA' wants to get all own trainings
    Then the application should handle request and return status code of 200
    And user receives all the required trainer trainings

  Scenario: Unauthenticated user wants to get all training by trainer (negative)
    When unauthenticated user wants to get trainings by trainer 'Any.Trainer'
    Then the application should handle request and return status code of 401

  Scenario: Trainee adds new training (positive)
    When trainee 'Robert.Martin' with password 'mEWtaomxkS' submits valid data for a new training with 'Koa.King'
    Then the application should handle request and return status code of 200

  Scenario: Trainer tries to add a new training (negative)
    When trainer 'Ezrah.Jackson' with password 'RRFqJnBunA' submits data for a new training with 'Angela.Thompson'
    Then the application should handle request and return status code of 403

  Scenario: Unauthenticated user wants to add new training (negative)
    When unauthenticated user wants to add new training
    Then the application should handle request and return status code of 401
