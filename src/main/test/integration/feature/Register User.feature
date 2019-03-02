Feature: User registration
  •	As a visitor I want to create a new account so that I can use kwetter to its full potential.

  Scenario: Create an account
    Given I want to create an account with the following information
    | username  | myUser                               |
    | email     | user@mail.com                        |
    | password  | Password123                          |
    | biography | Im a human and this is my biography  |
    | website   | www.myUser.co.nl                     |
    | longitude | 654.957                              |
    | latitude  | 945.273                              |
    When I submit my account information
    Then The status code is 200
    And response includes the following
    | user.username   | myUser                               |
    | user.email      | user@mail.com                        |
    | user.biography  | Im a human and this is my biography  |
    | user.website    | wwww.myUser.co.nl                    |
    | user.longitude  | 654.957                              |
    | user.latitude   | 945.273                              |
