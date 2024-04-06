Feature: Testing trainee REST API
  Users should be able to submit GET, PUT, POST and DELETE requests to the Trainee web service endpoint

  @Endpoint-trainees
  Scenario: Adding new valid trainee (positive)
    When user submits valid new trainee data with firstName 'New' and lastName 'Trainee'
    Then the server should handle request and return status code of 201
    And the user receives new trainee credentials with userName 'New.Trainee'

  @Endpoint-trainees
  Scenario: Trying to add new invalid trainee (negative)
    When user submits invalid new trainee data with firstName 'N' and lastName 'T'
    Then the server should handle request and return status code of 422

  @Endpoint-trainees-profile
  Scenario: Authenticated trainee updating existing own profile (positive)
     When trainee 'Charlotte.Thomas' with password 'IZsyHnLVdq' wants to update own profile with firstName 'Charlie' and lastName 'Smith'
     Then the server should handle request and return status code of 200
     And updated trainee data with firstName 'Charlie' and lastName 'Smith' is returned

  @Endpoint-trainees-profile
  Scenario: Authenticated trainee trying to update another trainee profile (negative)
    When trainee 'Mia.Taylor' with password 'rivnVifYui' wants to update 'Another.Trainee' with firstName 'Charlie' and lastName 'Smith'
    Then the server should handle request and return status code of 403

  @Endpoint-trainees-profile
  Scenario: Unauthenticated user trying to update trainee profile (negative)
    When unauthenticated user wants to update any trainee profile
    Then the server should handle request and return status code of 401

  @Endpoint-trainees-profile
  Scenario: Authenticated trainee finds existing own profile by userName (positive)
    When trainee 'Abigail.Brown' with password 'LjeUGZeUaT' wants to find own profile by userName
    Then the server should handle request and return status code of 200
    And the 'Abigail.Brown' trainee data is returned

  @Endpoint-trainees-profile
  Scenario: Authenticated trainee trying to find another trainee profile by userName (negative)
    When trainee 'Olivia.Wilson' with password 'ACnAzwOust' wants to find another trainee by userName 'Another.Trainee'
    Then the server should handle request and return status code of 403

  @Endpoint-trainees-profile
  Scenario: Unauthenticated user trying to find trainee profile by userName (negative)
    When unauthenticated user wants to find a trainee by userName 'New.Trainee'
    Then the server should handle request and return status code of 401
    And no data is returned

  @Endpoint-trainees-profile-update-trainers
  Scenario: Authenticated trainee updating own trainer list (positive)
    When trainee 'Emily.Smith' with password 'OpMncWkKTJ' wants to update own trainer list
    Then the server should handle request and return status code of 200
    And updated trainer list should be returned

  @Endpoint-trainees-profile-update-trainers
  Scenario: Authenticated trainee updating another trainee trainer list (negative)
    When trainee 'Lucas.Rodriguez' with password 'uNcobCHiCd' wants to update another trainee 'Another.Trainee' trainer list
    Then the server should handle request and return status code of 403

  @Endpoint-trainees-profile-update-trainers
  Scenario: Unauthenticated user trying to update any trainee trainer list (negative)
    When unauthenticated user wants to update any trainee trainer list
    Then the server should handle request and return status code of 401

  @Endpoint-trainees-profile
  Scenario: Authenticated trainee deleting own profile by userName
    When trainee 'Elijah.Johnson' with password 'xCAchGoelf' wants to delete own profile by userName
    Then the server should handle request and return status code of 200

  @Endpoint-trainees-profile
  Scenario: Authenticated trainee trying to delete another trainee profile by userName (negative)
    When trainee 'James.Williams' with password 'cbveybhpMt' wants to delete another trainee by userName 'Another.Trainee'
    Then the server should handle request and return status code of 403

  @Endpoint-trainees-profile
  Scenario: Unauthenticated user trying to delete any trainee profile by userName (negative)
    When unauthenticated user wants to delete any trainee profile
    Then the server should handle request and return status code of 401
