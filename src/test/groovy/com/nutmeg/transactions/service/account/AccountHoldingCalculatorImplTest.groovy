package com.nutmeg.transactions.service.account

import com.nutmeg.transactions.model.Holding
import com.nutmeg.transactions.service.cash.CashCalculator
import spock.lang.Specification
import spock.lang.Subject

import static com.nutmeg.transactions.fixtures.TransactionFixtures.*

/**
 * Created by Martin on 8/2/2017.
 */
class AccountHoldingCalculatorImplTest extends Specification {

    CashCalculator cashCalculator = Stub()

    @Subject
    AccountHoldingCalculator subject

    void setup() {
        subject = new AccountHoldingCalculatorImpl(cashCalculator);
    }

    def "The cash holding get properly updated"() {

        given:
        def botTransaction = aBoughtStockTransaction()
        prepareCashCalculation(botTransaction, -20)

        def soldTransaction = aSoldStockTransaction()
        prepareCashCalculation(soldTransaction, 40)

        when:
        def holdings = subject.calculateHoldings([botTransaction, soldTransaction])

        then:
        assertHolding(holdings, "CASH", 20)
    }

    def "Buying stocks accumulates the holding"() {

        given:
        def transactions = [
                aBoughtStockTransaction("VUKA", 20),
                aBoughtStockTransaction("VUKA", 30),

                aBoughtStockTransaction("GILS", 20.1234),
                aBoughtStockTransaction("GILS", 11.4321)
        ]

        when:
        def holdings = subject.calculateHoldings(transactions)

        then:
        assertHolding(holdings, "VUKA", 50)
        assertHolding(holdings, "GILS", 31.5555)
    }

    def "Selling stocks decreases the holding value"() {

        given:
        def transactions = [
                aBoughtStockTransaction("VUKA", 20),

                aSoldStockTransaction("VUKA", 5.1122),
                aSoldStockTransaction("VUKA", 3.3321),
        ]

        when:
        def holdings = subject.calculateHoldings(transactions)

        then:
        assertHolding(holdings, "VUKA", 11.5557)
    }

    def "Deposit, withdraw and dividend only affects cash"() {

        given:
        def deposit = aDepositTransaction()
        prepareCashCalculation(deposit, 20)

        def withdraw = aWithdrawTransaction()
        prepareCashCalculation(withdraw, -10)

        def dividend = aDividendTransaction()
        prepareCashCalculation(dividend, 1.1234)

        when:
        def holdings = subject.calculateHoldings([deposit, withdraw, dividend])

        then:
        holdings
        holdings.size() == 1
        assertHolding(holdings, "CASH", 11.1234)
    }

    def "An account can be overdrawn"() {

        given:
        def deposit = aDepositTransaction()
        prepareCashCalculation(deposit, 20)

        def withdraw = aWithdrawTransaction()
        prepareCashCalculation(withdraw, -100)

        when:
        def holdings = subject.calculateHoldings([deposit, withdraw])

        then:
        assertHolding(holdings, "CASH", -80)
    }

    def "Report cash even if it is zero"() {

        given:
        def deposit = aDepositTransaction()
        prepareCashCalculation(deposit, 20)

        def withdraw = aWithdrawTransaction()
        prepareCashCalculation(withdraw, -20)

        when:
        def holdings = subject.calculateHoldings([deposit, withdraw])

        then:
        assertHolding(holdings, "CASH", 0)
    }

    def "If not enough stock to sell throw not sufficient stock amount exception"() {

        given:
        def transactions = [
                aBoughtStockTransaction("VUKA", 20),
                aSoldStockTransaction("VUKA", 40)
        ]

        when:
        subject.calculateHoldings(transactions)

        then:
        NotSufficientStockAmountException ex = thrown()
        ex.message == 'Not enough VUKA in order to sell, for account ACC1'
    }

    def assertHolding(List<Holding> holdings, lookFor, expectedHolding) {
        def foundHolding = holdings.find { it.asset == lookFor }
        // safe access to the object
        foundHolding?.holding == BigDecimal.valueOf(expectedHolding)
    }

    def prepareCashCalculation(transaction, cash) {
        cashCalculator.calculateCash(transaction) >> cash
    }

}
