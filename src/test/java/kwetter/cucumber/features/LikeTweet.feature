Feature: User should be able to like a tweet
  As a user I want to like a tweet so that I can interact with another user.

  Scenario: Like a Tweet
    Given the following role:
      | member |
    Given the following users:
      | id                | 1                     | 2                    |
      | username          | testUser              | testUser2            |
      | biography         | This is my biography  | This is my biography |
      | locationLongitude | 123123.123123         | 12345456.234234      |
      | locationLatitude  | 123653.234123         | 89237489.23423       |
      | website           | wwww.mysite.io        | www.mysite.com       |
      | role              |                       |                      |
    Given the following tweet:
      | message           | This is a tweet       |
      | author            | testUser2             |
      | createdAt         | 12/01/2019            |
    When "testUser" likes a Tweet
    Then the Tweet should have one like by "testUser"

  Scenario: Unlike a Tweet
    Given the following role:
      | member |
    Given the following users:
      | id                | 1                     | 2                    |
      | username          | testUser              | testUser2            |
      | biography         | This is my biography  | This is my biography |
      | locationLongitude | 123123.123123         | 12345456.234234      |
      | locationLatitude  | 123653.234123         | 89237489.23423       |
      | website           | wwww.mysite.io        | www.mysite.com       |
      | role              |                       |                      |
    Given the following tweet:
      | message           | This is a tweet       |
      | author            | testUser2             |
      | createdAt         | 12/01/2019            |
    When "testUser" unlikes a Tweet
    Then the Tweet should not have a like by "testUser" anymore