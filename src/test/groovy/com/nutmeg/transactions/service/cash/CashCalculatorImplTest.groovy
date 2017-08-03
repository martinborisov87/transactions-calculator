package com.nutmeg.transactions.service.cash

import com.nutmeg.transactions.fixtures.TransactionFixtures
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.nutmeg.transactions.fixtures.TransactionFixtures.aDepositTransaction
import static com.nutmeg.transactions.fixtures.TransactionFixtures.aWithdrawTransaction

/**
 * Created by Martin on 8/2/2017.
 */
class CashCalculatorImplTest extends Specification {

    @Subject
    CashCalculator subject

    void setup() {
        subject = new CashCalculatorImpl();
    }

    @Unroll
    def "For a sale cash is the result of units multiply by price"() {

        given:
        def saleTransaction = TransactionFixtures.aSoldStockTransaction("ASST", units, price)

        // combined when+then
        expect:
        subject.calculateCash(saleTransaction) == BigDecimal.valueOf(expectedCash)

        // data driven test; you can provide multiple different values, test will run multiple times
        where:
        units  | price || expectedCash
        3      | 2     || 6
        10.512 | 3.335 || 35.0575
    }

    @Unroll
    def "When buying cash is the negated result of units multiply by price"() {

        given:
        def buyingTransaction = TransactionFixtures.aBoughtStockTransaction("ASST", units, price)

        expect:
        subject.calculateCash(buyingTransaction) == BigDecimal.valueOf(expectedCash)

        where:
        units  | price  || expectedCash
        3      | 2      || -6
        2.1325 | 2.2555 || -4.8099
    }

    @Unroll
    def "For deposit, withdraw and divident cash is the units of the transaction"() {

        expect:
        subject.calculateCash(transaction) == BigDecimal.valueOf(expectedCash)

        where:
        transaction                    || expectedCash
        aDepositTransaction(100, 5)    || 100
        aWithdrawTransaction(50, 5)    || 50
        aDepositTransaction(0.0242, 5) || 0.0242
    }
}
