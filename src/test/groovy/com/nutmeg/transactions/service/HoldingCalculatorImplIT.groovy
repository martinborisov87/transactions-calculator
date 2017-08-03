package com.nutmeg.transactions.service

import com.nutmeg.transactions.file.FileReader
import com.nutmeg.transactions.model.Holding
import com.nutmeg.transactions.service.account.AccountHoldingCalculatorImpl
import com.nutmeg.transactions.service.cash.CashCalculatorImpl
import com.nutmeg.transactions.service.filter.TransactionFilterService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

import static com.nutmeg.transactions.fixtures.HoldingFixtures.aHolding
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs
import static spock.util.matcher.HamcrestSupport.expect

class HoldingCalculatorImplIT extends Specification {

    @Subject
    HoldingCalculator subject

    void setup() {
        def reader = new FileReader()
        def transactionFilterService = new TransactionFilterService(reader)

        def cashCalculator = new CashCalculatorImpl()
        def accountHoldingCalculator = new AccountHoldingCalculatorImpl(cashCalculator)

        subject = new HoldingCalculatorImpl(transactionFilterService, accountHoldingCalculator)
    }

    def "Calculate holding properly for a specified set of data and date"() {

        given:
        def filename = "transactions-test-data.txt";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        def reportDate = LocalDate.of(2017, 2, 1)

        when:
        def holdings = subject.calculateHoldings(file, reportDate)

        then:
        holdings?.size() == 2

        and:
        def neaa0000Holdings = holdings['NEAA0000'] as ArrayList<Holding>
        neaa0000Holdings?.size() == 4

        expect(neaa0000Holdings, sameBeanAs([
                aHolding('VUKE', 20),
                aHolding('VUSA', 10),
                aHolding('GILS', 10.512),
                aHolding('CASH', 17.6849)
        ]))

        and:
        def neaa0001Holdings = holdings['NEAB0001'] as ArrayList<Holding>
        neaa0001Holdings?.size() == 1

        expect(neaa0001Holdings, sameBeanAs([
                aHolding('CASH', 10000.0000)
        ]))
    }
}