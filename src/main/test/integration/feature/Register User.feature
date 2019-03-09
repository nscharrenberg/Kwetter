Feature: User registration
  •	As a visitor I want to create a new account so that I can use kwetter to its full potential.

  Background:
    Given a group with the name "member" exists.

  Scenario Outline: Create an account
    When I register a new account with "<username>", "<email>", "<password>", "<biography>", "<website>", "<longitude>", "<latitude>"
    Then The server responds with status code 201
    And A user containing "<username>" should exist
    Examples:
    | username        | email             | password            | biography               | website         | longitude      | latitude      |
    | testUser        | testUser@mail.com | pass123             | my biography of test    | www.test.com    | 563.3234       | 1235.1212     |
    | noah            | noah@mail.com     | myPass345           | noahs biography         | www.noah.com    | 453.34         | 45.6578       |



  Scenario Outline: Create an account with an existing username
    Given a user with "<username>", "<email>", "<password>", "<biography>", "<website>", "<longitude>", "<latitude>"
    When I register a new account with "<username>", "<email>", "<password>", "<biography>", "<website>", "<longitude>", "<latitude>"
    Then The server responds with status code 409
    Examples:
      | username        | email             | password            | biography               | website         | longitude      | latitude      |
      | testUser        | testUser@mail.com | pass123             | my biography of test    | www.test.com    | 563.3234       | 1235.1212     |
      | noah            | noah@mail.com     | myPass345           | noahs biography         | www.noah.com    | 453.34         | 45.6578       |