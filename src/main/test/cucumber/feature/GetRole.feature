Feature: Get a Role


  Scenario: Verify one role by id
    Given I perform GET operation for "/roles"
    When I perform GET for the role with id "1"
    Then I should see the role name as "admin"
    And I should see the statuscode "200"

  Scenario: Verify one role by id that does not exist
    Given I perform GET operation for "/roles"
    When I perform GET for the role with id "9845"
    Then I should see the statuscode "404"

  Scenario: Verify one role by name
    Given I perform GET operation for "/roles/name"
    When I perform GET for the role with name "member"
    Then I should see the role name as "member"
    And I should see the statuscode "200"

  Scenario: Verify one role by name that does not exist
    Given I perform GET operation for "/roles/name"
    When I perform GET for the role with name "idonotexist"
    Then I should see the statuscode "404"