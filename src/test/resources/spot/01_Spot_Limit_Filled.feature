
Feature: Spot Limit Full Filled Order Placement

  Order Type combination: Limit-Limit
  Order Statuses after the trade:
  Limit Sell - Filled
  Limit Buy - Filled

  Scenario Outline: Check seller login is successful with valid credentials
    Given user is on login
    When user enter valid "<email>" and "<password>"
    Then clicks on Login Button
    And user is navigate to market page
    Examples:
      | email                    | password            |
      | seller.account.email      | seller.account.password |


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
#      | 5.00             | 2        |

  Scenario Outline: Check buyer login is successful with valid credentials
    Given user is on login
    When user enter valid "<email>" and "<password>"
    Then clicks on Login Button
    And user is navigate to market page
    Examples:
      | email                    | password            |
      | buyer.account.email      | buyer.account.password |

  Scenario Outline: Validate account buyer account balance before placing limit buy order
    Given buyer navigate to the account page
    When buyer retrieves the before account balances
    Then buyer retrieves the before credit balances for "<carbonCredit>"
    Examples:
      | carbonCredit  |
      | CAR.088       |

  Scenario Outline: Place a Limit Buy Order
    Given the buyer navigates to the spot order placement page "<carbonCredit>"
    When the buyer selects the order type
    Then the buyer enters a limit buy price of "<limit_buy_price>"
    And the buyer enters a quantity of "<quantity>"
    And the buyer submits the order
    And the buy order should be placed successfully
    And the buyer info notification should be displayed
    Examples:
      | carbonCredit  | limit_buy_price  | quantity |
      | CAR.088       | 5.00             | 1        |

  Scenario: Validate buyer account balances after successful filled trade
    Given buyer retrieve the after account balance
    When buyer validate that the gross balance has decreased
    Then buyer validate that the available balance has decreased

  Scenario Outline: Validate buyer credit balances after successful filled trade
    Given buyer retrieves the after credit balances "<carbonCredit>"
    When buyer validate that the total credit balance has increased
    Then buyer validate that the available credit balance has increased
    Examples:
      | carbonCredit  |
      | CAR.088       |

  Scenario: Validate seller account balance after successful trade
    Given seller logs into their account
    When seller navigate to the account page
    Then seller retrieve the after account balance
    And seller validates that the gross balance has increased
    And seller validates that the available balance has increased

  Scenario Outline: Validate seller credit balance after successful trade
    Given seller retrieves the after credit balances "<carbonCredit>"
    When seller validate that the total credit balance has decreased
    Then seller validate that the available credit balance has decreased
    Examples:
      | carbonCredit  |
      | CAR.088       |