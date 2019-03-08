Feature: Follow User
  •	As a user I want to follow a user so that I can get status updates from the user I’m following.

  Background:
    Given a group with the name "member" exists.

  Scenario Outline: Follow a user
    Given a user with "<username>", "<email>", "<password>", "<biography>", "<website>", "<longitude>", "<latitude>"
    Given a second user with "<username>", "<email>", "<password>", "<biography>", "<website>", "<longitude>", "<latitude>"
    When User with username "<username>" calls the follow api to follow "<username>"
    Then The server responds with status code 200
    And A user containing "<username>" should be added to the followers list of "<username>"
    Examples:
      | username        | email             | password            | biography               | website         | longitude      | latitude      |
      | testUser        | testUser@mail.com | pass123             | my biography of test    | www.test.com    | 563.3234       | 1235.1212     |
      | noah            | noah@mail.com     | myPass345           | noahs biography         | www.noah.com    | 453.34         | 45.6578       |