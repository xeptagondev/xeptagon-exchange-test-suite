Feature: Seller Spot Limit Full Filled Order Placement and Cancellation

  Order Type combination: Limit-Limit
  Order Statuses after the trade:
  Limit Sell - Cancelled

  Scenario Outline: Check seller login is successful with valid credentials
    Given user is on login
    When user enter valid "<email>" and "<password>"
    And clicks on Login Button
    Then user is navigate to market page
#    And close the browser
    Examples:
      | email | password |
      | shehans+EX2@xeptagon.com | EX2@xeptagon.coM |

  Scenario Outline: Validate seller account balance before placing limit sell order
    Given seller navigate to the account page
    When seller retrieves the before account balances
    Then seller retrieves the before credit balances for "<carbonCredit>"
    Examples:
      | carbonCredit  |
      | CAR.088       |


  Scenario Outline: Place a Limit Sell Order
    Given the seller navigates to the spot order placement page "<carbonCredit>"
    When the seller selects the order type
    Then the seller enters a limit sell price of "<limit_sell_price>"
    And the seller enters a quantity of "<quantity>"
    And the seller submits the order
    And the sell order should be placed successfully
    Examples:
      | carbonCredit  | limit_sell_price | quantity |
      | CAR.088       | 5.00             | 1        |
      | CAR.088       | 6.00             | 1        |
      | CAR.088       | 7.00             | 1        |
      | CAR.088       | 8.00             | 1        |

  Scenario: Seller should able to cancel all spot limit sell orders
    Given seller cancels all open spot limit sell orders
    Then seller all orders should be canceled successfully

  Scenario: Validate seller account balance after successful cancel all open orders
    Given seller navigate to the account page
    When seller retrieve the after account balance
    Then  seller's available account balance should be unchanged from the initial available account balance

  Scenario Outline: Validate seller credit balance after successful trade
    Given seller retrieves the after credit balances "<carbonCredit>"
    When the available carbon credit balance should equal the initial credit balance
    Examples:
      | carbonCredit  |
      | CAR.088       |