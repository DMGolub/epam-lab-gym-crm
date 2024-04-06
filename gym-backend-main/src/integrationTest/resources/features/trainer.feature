Feature: Testing trainer REST API
  Users should be able to submit GET, PUT and POST requests to the Trainer web service endpoint

  @Endpoint-trainers
  Scenario: Adding new valid trainer (positive)
    When user submits valid new trainer data with firstName 'New' and lastName 'Trainer'
    Then server should handle request and return status code of 201
    And the user receives new trainer credentials with userName 'New.Trainer'

  @Endpoint-trainers
  Scenario: Trying to add new invalid trainer (negative)
    When user submits invalid new trainer data with firstName 'N' and lastName 'T'
    Then server should handle request and return status code of 422

  @Endpoint-trainers-profile
  Scenario: Authenticated trainer updating existing own profile (positive)
     When trainer 'Koa.King' with password 'tcsCLkwaSa' wants to update own profile with firstName 'Koah' and lastName 'Smith'
     Then server should handle request and return status code of 200
     And updated trainer data with firstName 'Koah' and lastName 'Smith' is returned

  @Endpoint-trainers-profile
  Scenario: Authenticated trainer trying to update another trainer profile (negative)
    When trainer 'Teo.Thompson' with password 'jnVOcOoMUV' wants to update 'Another.Trainer' with firstName 'Charlie' and lastName 'Smith'
    Then server should handle request and return status code of 403

  @Endpoint-trainers-profile
  Scenario: Unauthenticated user trying to update trainer profile (negative)
    When unauthenticated user wants to update any trainer profile
    Then server should handle request and return status code of 401

  @Endpoint-trainers-profile
  Scenario: Authenticated trainer finds existing own profile by userName (positive)
    When trainer 'Colter.Moore' with password 'AphJOjtFvA' wants to find own profile by userName
    Then server should handle request and return status code of 200
    And the 'Colter.Moore' trainer data is returned

  @Endpoint-trainers-profile
  Scenario: Authenticated trainer trying to find another trainer profile by userName (positive)
    When trainer 'Kanan.Martinez' with password 'oIBEkhmTrZ' wants to find another trainer by userName 'Colter.Moore'
    Then server should handle request and return status code of 200
    And the 'Colter.Moore' trainer data is returned

  @Endpoint-trainers-profile
  Scenario: Unauthenticated user trying to find trainer profile by userName (negative)
    When unauthenticated user wants to find a trainer by userName 'New.Trainer'
    Then server should handle request and return status code of 401
    And no trainer data is returned

  @Endpoint-trainers-not-assigned-on
  Scenario: Authenticated trainee getting all trainers not assigned on himself (positive)
    When trainee 'Oliver.Jones' with password 'FdwRnKyPSM' wants to find all trainers not assigned on himself
    Then server should handle request and return status code of 200
    And trainers not assigned on trainee are returned

  @Endpoint-trainers-not-assigned-on
  Scenario: Unauthenticated user trying to find trainers not assigned on trainee (negative)
    When unauthenticated user wants to find trainers not assigned on trainee 'Any.Trainee'
    Then server should handle request and return status code of 401

  @Endpoint-trainers-assigned-on
  Scenario: Authenticated trainee getting all trainers assigned on himself (positive)
    When trainee 'Liam.Anderson' with password 'GhOYGnECBM' wants to find all trainers assigned on himself
    Then server should handle request and return status code of 200
    And trainers assigned on trainee are returned

  @Endpoint-trainers-assigned-on
  Scenario: Unauthenticated user trying to find trainers assigned on trainee (negative)
    When unauthenticated user wants to find trainers assigned on trainee 'Any.Trainee'
    Then server should handle request and return status code of 401
