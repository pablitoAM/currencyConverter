Feature: Convert Currency

	Tests the the currency conversion is done.
	To prove that it checks if the invocatioon to the external provider has been done.
	
	Scenario Outline: Current  Conversion for CurrencyLayer
		Given a ProviderRequest with the <source> currency
		And a <list> of expected currencies
		When we invoke the provider
		Then the provider returns the invocation <result>
		and the <result> is translated into a ProviderResponse
		