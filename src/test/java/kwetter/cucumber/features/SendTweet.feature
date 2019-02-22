Feature: User sends a tweet of max 140 characters
  •	As a user I want to send a new tweet of max. 140 characters so that I can share something that I want to share with others.
  •	As a user I want to mention another user when tweeting so that the other user gets notified that I mentioned him.

  Scenario: Send a tweet of less then 140 characters
    Given the following role:
      | member |
    Given the following users:
      | id                | 1                     | 2                    | 3                |
      | username          | testUser              | testUser2            | testUser3        |
      | biography         | This is my biography  | This is my biography | This is bio      |
      | locationLongitude | 123123.123123         | 12345456.234234      | 123123.123123    |
      | locationLatitude  | 123653.234123         | 89237489.23423       | 1231.123123      |
      | website           | wwww.mysite.io        | www.mysite.com       | www.thissite.com |
      | role              |                       |                      |                  |
    When "testUser" wants to send a tweet with the message "This is my tweet of less then 140 characters"
    Then a tweet should be created

  Scenario: Send a tweet of less then 140 characters and mention "testUser2"
    Given the following role:
      | member |
    Given the following users:
      | id                | 1                     | 2                    | 3                |
      | username          | testUser              | testUser2            | testUser3        |
      | biography         | This is my biography  | This is my biography | This is bio      |
      | locationLongitude | 123123.123123         | 12345456.234234      | 123123.123123    |
      | locationLatitude  | 123653.234123         | 89237489.23423       | 1231.123123      |
      | website           | wwww.mysite.io        | www.mysite.com       | www.thissite.com |
      | role              |                       |                      |                  |
    When "testUser" wants to send a tweet with the message "@testUser2 @testUser3 you guys are awesome!"
    Then a tweet should be created and a mention should be added with "testUser2"

  Scenario: Send a tweet with more then 140 characters
    Given the following role:
      | member |
    Given the following users:
      | id                | 1                     | 2                    | 3                |
      | username          | testUser              | testUser2            | testUser3        |
      | biography         | This is my biography  | This is my biography | This is bio      |
      | locationLongitude | 123123.123123         | 12345456.234234      | 123123.123123    |
      | locationLatitude  | 123653.234123         | 89237489.23423       | 1231.123123      |
      | website           | wwww.mysite.io        | www.mysite.com       | www.thissite.com |
      | role              |                       |                      |                  |
    When "testUser" wants to send a tweet with the message "CtxDOcv6gK8T6D8T2bj0YeezV5FPiCmwKQaTOazBgtXnNOylnwDNbGZDUVx3cRNX4F9s4CtKkZRezWs0IzNK8N0s2ZTtSgMo57sI6amXUbpJJKY1LE10LFFs9EA6435dR7LkpLzmaolkfvOMhEyy2J"
    Then an Exception StringToLongException saying "Tweet can not be longer then 140 characters." is thrown.