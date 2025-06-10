Feature: User Management
  As a system administrator
  I want to manage users
  So that I can control access to the system

  Scenario: Get user details
    Given there is a user with id "1"
    When I request the user details
    Then I should receive the user information
    And the response should include the username
    And the response should include the email

  Scenario: Create new user
    Given I have valid user details
    When I create a new user
    Then the user should be saved successfully
    And I should receive the created user details 