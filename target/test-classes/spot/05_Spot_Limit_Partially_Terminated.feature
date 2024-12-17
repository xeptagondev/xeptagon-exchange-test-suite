
Feature: Seller Spot Limit Partially Filled Order Placement and Terminated

  Order Type combination: Limit-Limit
  Order Statuses after the trade:
  Limit First Sell - Filled
  Limit First Buy - Partially Filled
  Limit Second Sell - Open
  Limit First Buy - Partially Terminated


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

  Scenario Outline: Place a Sell Spot Limit Order
    Given the seller navigates to the spot order placement page "<carbonCredit>"
    When the seller selects the order type
    And the seller enters a limit sell price of "<limit_sell_price>"
    And the seller enters a quantity of "<quantity>"
    And the seller submits the order
    And the sell order should be placed successfully
    Examples:
      | carbonCredit  | limit_sell_price | quantity |
      | CAR.088       | 5.00             | 2        |


  Scenario Outline: Check buyer login is successful with valid credentials
    Given user is on login
    When user enter valid "<email>" and "<password>"
    And clicks on Login Button
    Then user is navigate to market page

    Examples:
      | email                    | password            |
      | shehans+EX1@xeptagon.com | EX1@xeptagon.coM    |

  Scenario Outline: Validate account buyer account balance before placing limit buy order
    Given buyer navigate to the account page
    When buyer retrieves the before account balances
    Then buyer retrieves the before credit balances for "<carbonCredit>"
    Examples:
      | carbonCredit  |
      | CAR.088       |

  Scenario Outline: Place a Buy Spot Limit Order
    Given the buyer navigates to the spot order placement page "<carbonCredit>"
    When the buyer selects the order type
    And the buyer enters a limit buy price of "<limit_buy_price>"
    And the buyer enters a quantity of "<quantity>"
    And the buyer submits the order
    And the buy order should be placed successfully
    And the buyer info notification should be displayed
    Examples:
      | carbonCredit  | limit_buy_price  | quantity |
      | CAR.088       | 5.00             | 7        |

  Scenario: Validate buyer account balance after executing spot limit matching order
    Given buyer retrieve the after account balance
    When buyer validate that the gross balance has decreased
    Then buyer validate that the available balance has decreased
    And buyer validate that the block amount has increased

  Scenario Outline: Validate buyer credit balance after executing spot limit matching order
    Given buyer retrieves the after credit balances "<carbonCredit>"
    When buyer validate that the total credit balance has increased
    Then buyer validate that the available credit balance has increased
    Examples:
      | carbonCredit  |
      | CAR.088       |

    # for self matching buyer user now act as seller
  Scenario Outline: Buyer placed sell order to partially terminated
#    Given buyer retrieve the after account balance
    Given the seller navigates to the spot order placement page "<carbonCredit>"
    When the seller selects the order type
    And the seller enters a limit sell price of "<limit_sell_price>"
    And the seller enters a quantity of "<quantity>"
    And the seller submits the order
    And the sell order should be placed successfully
    And the seller info notification should be displayed
    Examples:
      | carbonCredit  | limit_sell_price  | quantity |
      | CAR.088       | 5.00             | 1        |

#  Scenario: Validate buyer account balance after executing a spot limit partially terminated order
#    Given buyer retrieve the new buyer account balance after partially terminated
#    When buyer fiat currency block amount should be released and available balance increased after partially terminated
#
#  Scenario Outline: Validate seller credit balance after executing spot limit partially terminated order
#    Given buyer retrieve the new buyer credit balance after partially terminated "<carbonCredit>"
#    When buyer validate that the blocked credit balance has increased
#    Then seller validate that the available credit balance has decreased
#    Examples:
#      | carbonCredit  |
#      | CAR.088       |

  Scenario Outline: Seller cancel all open orders
    Given the seller navigates to the spot order placement page "<carbonCredit>"
    When seller cancels all open spot limit sell orders
    Then seller all orders should be canceled successfully
    Examples:
      | carbonCredit  |
      | CAR.088       |