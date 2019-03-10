Feature: Get a Permission


  Scenario: Verify one permission by id
    Given I perform GET operation for "/permissions"
    When I perform GET for the permission with id "1"
    Then I should see the permission name as "create_tweet"
    And I should see the statuscode "200"

  Scenario: Verify one permission by id that does not exist
    Given I perform GET operation for "/permissions"
    When I perform GET for the permission with id "9845"
    Then I should see the statuscode "404"

  Scenario: Verify one permission by name
    Given I perform GET operation for "/permissions/name"
    When I perform GET for the permission with name "create_tweet"
    Then I should see the permission name as "create_tweet"
    And I should see the statuscode "200"

  Scenario: Verify one role by permission that does not exist
    Given I perform GET operation for "/permissions/name"
    When I perform GET for the permission with name "idonotexist"
    Then I should see the statuscode "404"