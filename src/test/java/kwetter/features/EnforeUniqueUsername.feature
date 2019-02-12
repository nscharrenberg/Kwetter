Feature: Enforce unique username
  As a system
  I want to check a username upon registration or profile updates
  So that I can throw errors when a username is not unique.


  Scenario: Change Username of User to a unique username and make sure the Username is unique
    Given the following role:
      | member |
    Given the following users:
      | username          | testUser              | testUser2            |
      | biography         | This is my biography  | This is my biography |
      | locationLongitude | 123123.123123         | 12345456.234234      |
      | locationLatitude  | 123653.234123         | 89237489.23423       |
      | website           | wwww.mysite.io        | www.mysite.com       |
      | role              |                       |                      |
    When testUser changes it's name to "testUser3"
    Then the username of testUser should be "testUser3"

  Scenario: Change Username of User to an existing username and make sure the Username is unique
    Given the following role:
      | member |
    Given the following users:
      | username          | testUser              | testUser2            |
      | biography         | This is my biography  | This is my biography |
      | locationLongitude | 123123.123123         | 12345456.234234      |
      | locationLatitude  | 123653.234123         | 89237489.23423       |
      | website           | wwww.mysite.io        | www.mysite.com       |
      | role              |                       |                      |
    When testUser changes it's name to "testUser2"
    Then an Exception UsernameNotUniqueException saying "Username must be unique."