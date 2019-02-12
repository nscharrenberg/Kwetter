Feature: Follow User
  As a user
  I want to follow a user
  So that i can get status updates from the user I'm following

  Scenario: Follow a user I'm not yet following
    Given the following role:
      | member |
    Given the following users:
      | id                | 1                     | 2                    | 3                    | 4                    | 5                    | 6                    | 7                    | 8                    | 9                    | 10                   |
      | username          | testUser              | testUser1            | testUser2            | testUser3            | testUser4            | testUser5            | testUser6            | testUser7            | testUser8            | testUser9            |
      | biography         | This is my biography  | This is my Biography | This is my biography | This is my biography | This is my biography | This is my biography | this is my biography | This is my biography | This is my biography | This is my biography |
      | locationLongitude | 123123.123123         | 12121.2555           | 12345456.234234      | 564.56433            | 548946.5646          | 15613.545613         | 548654.156131        | 5611.35646           | 222.111              | 444.3333             |
      | locationLatitude  | 123653.234123         | 666.5555             | 89237489.23423       | 6894.354563          | 4623156.67946        | 16453.564513         | 3516.5131            | 1111.2222            | 3333.222             | 444.222              |
      | website           | wwww.user.io          | www.user1.com        | www.user2.com        | www.user3.com        | www.user4.com        | www.user5.com        | www.user6.com        | www.user7.com        | www.site8.com        | www.site9.com        |
      | role              |                       |                      |                      |                      |                      |                      |                      |                      |                      |                      |
    When user at index 0 wants to follow user at index 2
    Then user at index 0 should be following user at index 2
