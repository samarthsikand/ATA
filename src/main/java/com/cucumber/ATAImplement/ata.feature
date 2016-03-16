Feature: Testing the redbus.in website services

    Scenario: Testing the Bell website
	Given I am on "www.bell.ca" website
	When I click "SMALL BUSINESS" near "PERSONAL" button
	And I click on "Learn more" near "Business TV" button
	Then I verify the results