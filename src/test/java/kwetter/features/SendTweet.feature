Feature: User sends a tweet of max 140 characters
  •	As a user I want to send a new tweet of max. 140 characters so that I can share something that I want to share with others.

  Scenario: Send a tweet of less then 140 characters
    Given the following role:
      | member |
    Given the following users:
      | username          | testUser              | testUser2            |
      | biography         | This is my biography  | This is my biography |
      | locationLongitude | 123123.123123         | 12345456.234234      |
      | locationLatitude  | 123653.234123         | 89237489.23423       |
      | website           | wwww.mysite.io        | www.mysite.com       |
      | role              |                       |                      |
    When testUser wants to send a tweet with the message "This is my tweet of less then 140 characters"
    Then a tweet should be created

  Scenario: Send a tweet with more then 140 characters
    Given the following role:
      | member |
    Given the following users:
      | username          | testUser              | testUser2            |
      | biography         | This is my biography  | This is my biography |
      | locationLongitude | 123123.123123         | 12345456.234234      |
      | locationLatitude  | 123653.234123         | 89237489.23423       |
      | website           | wwww.mysite.io        | www.mysite.com       |
      | role              |                       |                      |
    When testUser wants to send a tweet with the message "CtxDOcv6gK8T6D8T2bj0YeezV5FPiCmwKQaTOazBgtXnNOylnwDNbGZDUVx3cRNX4F9s4CtKkZRezWs0IzNK8N0s2ZTtSgMo57sI6amXUbpJJKY1LE10LFFs9EA6435dR7LkpLzmaolkfvOMhEyy2J"
    Then an Exception StringToLongException saying "Tweet can not be more then 140 characters." is thrown.