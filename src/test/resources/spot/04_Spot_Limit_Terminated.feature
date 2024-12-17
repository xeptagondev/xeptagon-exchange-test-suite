
Feature: Seller Spot Limit Terminated Order Placement

  Order Type combination: Limit-Limit
  Order Statuses after the trade:
  Limit Sell - Terminated
  Limit Buy - Open

  Scenario Outline: Check seller login is successful with valid credentials
    Given user is on login
    When user enter valid "<email>" and "<password>"
    Then clicks on Login Button
    And user is navigate to market page
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

  Scenario Outline: Place a Sell Spot Limit Order
    Given the seller navigates to the spot order placement page "<carbonCredit>"
    When the seller selects the order type
    Then the seller enters a limit sell price of "<limit_sell_price>"
    And the seller enters a quantity of "<quantity>"
    And the seller submits the order
    And the sell order should be placed successfully
    Examples:
      | carbonCredit  | limit_sell_price | quantity |
      | CAR.088       | 5.00             | 1        |
#      | 5.00             | 2        |

  #In here seller and buyer will be same user because seller login system and placed sell order and buy order for system terminated scenario (self matching)
  Scenario Outline: Place a Buy Spot Limit Order
    Given the buyer navigates to the spot order placement page "<carbonCredit>"
    When the buyer selects the order type
    Then the buyer enters a limit buy price of "<limit_buy_price>"
    And the buyer enters a quantity of "<quantity>"
    And the buyer submits the order
    And the buy order should be placed successfully
    And the buyer info notification should be displayed
    Examples:
      | carbonCredit  | limit_buy_price  | quantity |
      | CAR.088       | 5.00             | 5        |

  Scenario: Validate seller account balance after executing a spot limit sell order
    Given seller retrieve the after account balance
    Then the seller validates that the blocked balance has increased
    And the seller validates that the available balance has decreased

  Scenario Outline: Validate seller credit balance after executing spot limit matching order
    Given seller retrieves the after credit balances "<carbonCredit>"
    Then the available carbon credit balance should equal the initial credit balance
    Examples:
      | carbonCredit  |
      | CAR.088       |

  Scenario Outline: Seller cancel all open orders
    Given the seller navigates to the spot order placement page "<carbonCredit>"
    When seller cancels all open spot limit sell orders
    Then seller all orders should be canceled successfully
    Examples:
      | carbonCredit  |
      | CAR.088       |