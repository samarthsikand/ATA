Feature: Testing the redbus.in website services

  Scenario: Testing the booking feature of redbus.in
    Given I am on "redbus.in" website
   When I set "From" field to "Delhi"
    And I set "To" field to "Jaipur"
    And I set "Date of Journey" field to "01-Mar-2016"
    And I set "Date of Return" field to "03-Mar-2016"
    And I click "Search Buses" button
    Then I verify the results
	
	Scenario: Testing the login feature of amazon.in
	Given I am on "amazon.in" website
	When I click on "Sign in" link
	And I set "Email or mobile phone number" field to "sammyman1691@yahoo.com"
	And I set "Password" field to "voldemortz91"
	And I click "Login" button
	Then I verify the results
	
#	Scenario: Testing the contextual feature of gsmarena
#	Given I am on "gsmarena.com" website
#	When I click on "LG G5" near "LATEST DEVICES"
#	Then I verify the results

	Scenario: Testing the login feature on wikipedia.in
	Given I am on "en.wikipedia.org/wiki/Login" website
	When I click "login" button
	Then I verify the results
	
	Scenario: Testing the booking feature of redbus.in
    Given I am on "redbus.in" website
   When I set "From" field near "Online Bus Tickets Booking with Zero Booking Fees" to "Delhi"
    And I set "To" field near "Online Bus Tickets Booking with Zero Booking Fees" to "Jaipur"
    And I click "Date of Journey" field near "Online Bus Tickets Booking with Zero Booking Fees"
    And I click "1" button near "Apr 2016"
    And I click "Search Buses" button near "Online Bus Tickets Booking with Zero Booking Fees"
    Then I verify the results
    
    Scenario: Testing the login feature of amazon.in
	Given I am on "amazon.in" website
	When I click on "Sign in" near "Your Orders" link
	And I set "Email or mobile phone number" field near "Forgot Password" to "sammyman1691@yahoo.com"
	And I set "Password" field near "Forgot Password" to "voldemortz91"
	And I click "Login" button
	Then I verify the results
	
	Scenario: Testing the Bell website
	Given I am on "www.bell.ca" website
	When I click "SMALL BUSINESS" near "PERSONAL"
	And I click on "Learn more" near "Business TV"
	Then I verify the results