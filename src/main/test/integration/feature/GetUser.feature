Feature: Get a User

  Scenario: Verify one user by id
    Given I perform GET operation for "/users"
    When I perform GET for the user with id "1"
    Then I should see the user username as "nscharrenberg"
    And I should see the statuscode "200"

  Scenario: Verify one user by id that does not exist
    Given I perform GET operation for "/users"
    When I perform GET for the user with id "9845"
    Then I should see the statuscode "404"

  Scenario: Verify one user by username
    Given I perform GET operation for "/users/username"
    When I perform GET for the user with username "nscharrenberg"
    Then I should see the user username as "nscharrenberg"
    And I should see the statuscode "200"

  Scenario: Verify one user by username that does not exist
    Given I perform GET operation for "/users/username"
    When I perform GET for the user with username "idonotexist"
    Then I should see the statuscode "404"

  Scenario: Verify one user by email
    Given I perform GET operation for "/users/email"
    When I perform GET for the user with username "nscharrenberg@hotmail.com"
    Then I should see the user username as "nscharrenberg"
    And I should see the statuscode "200"

  Scenario: Verify one user by email that does not exist
    Given I perform GET operation for "/users/email"
    When I perform GET for the user with username "idonotexist@mail.com"
    Then I should see the statuscode "404"
