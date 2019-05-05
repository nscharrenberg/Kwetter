Feature: Get a Tweet


  Scenario: Verify one tweet by id
    Given I perform GET operation for "/tweets"
    When I perform GET for the tweet with id "1"
    Then I should see the tweet id as "1"
    And I should see the statuscode "200"

  Scenario: Verify one tweet by id that does not exist
    Given I perform GET operation for "/tweets"
    When I perform GET for the tweet with id "9845"
    Then I should see the statuscode "404"

  Scenario: Verify one tweet by author username
    Given I perform GET operation for "/tweets/author/name"
    When I perform GET for the tweets with author name "nscharrenberg"
    Then I should see the tweets author name as "nscharrenberg"
    And I should see the statuscode "200"

  Scenario: Verify one tweet by author name that does not exist
    Given I perform GET operation for "/tweets/author/name"
    When I perform GET for the tweets with author name "idonotexist"
    Then I should see the statuscode "404"