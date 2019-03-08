Feature: Update User
  •	As a user I want to change profile information so that I have my latest profile information available.

  Background:
    Given a group with the name "member" exists.

  Scenario Outline: Create an account
    Given a user with "<username>", "<email>", "<password>", "<biography>", "<website>", "<longitude>", "<latitude>"
    When I update my biography with username "<username>" to "<updatedBiography>"
    Then The server responds with status code 201
    And A user containing "<username>" should exist
    Examples:
      | username        | email             | password            | biography               | website         | longitude      | latitude      | updatedBiography                  |
      | testUser        | testUser@mail.com | pass123             | my biography of test    | www.test.com    | 563.3234       | 1235.1212     | This is my updated biography      |
      | noah            | noah@mail.com     | myPass345           | noahs biography         | www.noah.com    | 453.34         | 45.6578       | This is another updated biography |



  Scenario Outline: Create an account with an existing username
    Given a user with "<username>", "<email>", "<password>", "<biography>", "<website>", "<longitude>", "<latitude>"
    When I register a new account with "<username>", "<email>", "<password>", "<biography>", "<website>", "<longitude>", "<latitude>"
    Then The server responds with status code 409
    Examples:
      | username        | email             | password            | biography               | website         | longitude      | latitude      |
      | testUser        | testUser@mail.com | pass123             | my biography of test    | www.test.com    | 563.3234       | 1235.1212     |
      | noah            | noah@mail.com     | myPass345           | noahs biography         | www.noah.com    | 453.34         | 45.6578       |