Feature: Update profile information
  As a user
  I want to change my profile information
  So that i have my latest profile information available

  Scenario: Change website of User
    Given the following role:
      | member |
    And the following users:
      | username          | testUser              |
      | biography         | This is my biography  |
      | locationLongitude | 123123.123123         |
      | locationLatitude  | 123653.234123         |
      | website           | wwww.mysite.io        |
      | role              |                       |
    When the website field with the value "www.newsite.org" is entered
    Then the website field for the user is "www.newsite.org"

  Scenario: Change Biography of User
    Given the following role:
      | member |
    Given the following users:
      | username          | testUser              |
      | biography         | This is my biography  |
      | locationLongitude | 123123.123123         |
      | locationLatitude  | 123653.234123         |
      | website           | wwww.mysite.io        |
      | role              |                       |
    When the biography field on the profile page with the value "this is my new biography" is entered
    Then the biography for the user is "this is my new biography"

  Scenario: Change Biography of User with more then 160 characters
    Given the following role:
      | member |
    Given the following users:
      | username          | testUser              |
      | biography         | This is my biography  |
      | locationLongitude | 123123.123123         |
      | locationLatitude  | 123653.234123         |
      | website           | wwww.mysite.io        |
      | role              |                       |
    When the biography field on the profile page with the value "7vIQjpT47BP1KJNRww4HhiyCW3xAILcs9h87yBsdjOigzcvLjFhV5yMlhnHdcTttdxPcGk8TTcUblYJ0HWufNCzimntyabU0DcBPNK2PtgBE8JytEgsE9WGC3QQj4nkcTopoRO8vwDPdPZzieVlQWIIpulAUVlDTaDfHlZ4K8N" is entered
    Then an Exception StringToLongException saying "Biography can not be more then 160 characters long." is thrown.