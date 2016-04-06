Feature: Testing the redbus.in website services

    Scenario: Testing the Bell website
	Given I am on "http://redbus.in" website
	When I click "Search Buses" near "From" button
	Then I verify the results