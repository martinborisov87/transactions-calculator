package com.nutmeg.transactions.service.filter

import com.nutmeg.transactions.file.FileReader
import com.nutmeg.transactions.fixtures.FileFixtures
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

import static com.nutmeg.transactions.fixtures.FileFixtures.aFile
import static com.nutmeg.transactions.fixtures.TransactionFixtures.aTransactionForAccount

/**
 * Created by Martin on 8/2/2017.
 */
class TransactionFilterServiceTest extends Specification {

    FileReader fileReader = Stub()

    @Subject
    TransactionFilterService subject

    void setup() {
        subject = new TransactionFilterService(fileReader);
    }

    def "FIlter out transactions for later dates"() {

        given:
        def file = aFile()
        def date = LocalDate.now()

        def prevDate = date.minusDays(1)
        def laterDate = date.plusDays(1)
        fileReader.getTransactions(file) >> [
            aTransactionForAccount("ACC1", prevDate),
            aTransactionForAccount("ACC1", date),
            aTransactionForAccount("ACC1", laterDate),
            aTransactionForAccount("ACC1", date.plusDays(10))
        ]

        when:
        def transByAcc = subject.getTransactionsFilterByDateGroupByAccNumber(file, date)

        then:
        transByAcc
        transByAcc["ACC1"].size() == 2
    }

    def "Transactions get grouped properly by account"() {

        given:
        def file = aFile()
        def date = LocalDate.now()

        fileReader.getTransactions(file) >> [
                aTransactionForAccount("ACC1", date),
                aTransactionForAccount("ACC1", date),
                aTransactionForAccount("ACC1", date),

                aTransactionForAccount("ACC2", date),
                aTransactionForAccount("ACC2", date)
        ]

        when:
        def transByAcc = subject.getTransactionsFilterByDateGroupByAccNumber(file, date)

        then:
        transByAcc
        transByAcc["ACC1"].size() == 3
        transByAcc["ACC2"].size() == 2
    }
}
