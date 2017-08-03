package com.nutmeg.transactions.service

import com.nutmeg.transactions.service.account.AccountHoldingCalculator
import com.nutmeg.transactions.service.filter.TransactionFilterService
import spock.lang.Specification
import spock.lang.Subject

import static com.nutmeg.transactions.fixtures.FileFixtures.aFile
import static com.nutmeg.transactions.fixtures.HoldingFixtures.aCashHolding
import static com.nutmeg.transactions.fixtures.TransactionFixtures.aTransactionForAccount
import static java.time.LocalDate.now

class HoldingCalculatorImplTest extends Specification {

    TransactionFilterService transactionFilterService = Stub()
    AccountHoldingCalculator accountHoldingCalculator = Mock()

    // just a marking annotation, to make the test more explicit
    @Subject
    HoldingCalculator subject

    void setup() {
        subject = new HoldingCalculatorImpl(transactionFilterService, accountHoldingCalculator);
    }

    def "For each account number, calculate its holdings"() {

        given:
        def file = aFile()
        def date = now()

        def acc1Transactions = [aTransactionForAccount('ACC1', now()), aTransactionForAccount('ACC1', now())]
        def acc2Transactions = [aTransactionForAccount('ACC2', now())]
        def transactionsByAccNumber = [
                'ACC1': acc1Transactions,
                'ACC2': acc2Transactions
        ]
        transactionFilterService.getTransactionsFilterByDateGroupByAccNumber(file, date) >> transactionsByAccNumber

        when:
        subject.calculateHoldings(file, date)

        then:
        //interaction testing, verify the number of times this mock is invoked.
        1 * accountHoldingCalculator.calculateHoldings(acc1Transactions)
        1 * accountHoldingCalculator.calculateHoldings(acc2Transactions)
    }

    def "Prepare proper result based on each account holdings"() {

        given:
        def file = aFile()
        def date = now()

        def acc1Holdings = [aCashHolding(100), aCashHolding(200)]
        def acc2Holdings = [aCashHolding(300)]
        prepareData(file, date, acc1Holdings, acc2Holdings)

        when:
        def holdings = subject.calculateHoldings(file, date)

        then:
        holdings?.size() == 2
        holdings['ACC1'] == acc1Holdings
        holdings['ACC2'] == acc2Holdings
    }

    def prepareData(file, date, acc1Holdings, acc2Holdings) {
        def acc1Transactions = [aTransactionForAccount('ACC1', now()), aTransactionForAccount('ACC1', now())]
        def acc2Transactions = [aTransactionForAccount('ACC2', now())]
        def transactionsByAccNumber = [
                'ACC1': acc1Transactions,
                'ACC2': acc2Transactions
        ]
        transactionFilterService.getTransactionsFilterByDateGroupByAccNumber(file, date) >> transactionsByAccNumber

        accountHoldingCalculator.calculateHoldings(acc1Transactions) >> acc1Holdings
        accountHoldingCalculator.calculateHoldings(acc2Transactions) >> acc2Holdings
    }
}