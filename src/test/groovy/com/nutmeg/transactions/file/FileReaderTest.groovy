package com.nutmeg.transactions.file

import com.nutmeg.transactions.model.enums.TxnType
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.LocalDate

/**
 * Created by Martin on 8/2/2017.
 */
class FileReaderTest extends Specification {

    @Subject
    FileReader subject

    void setup() {
        subject = new FileReader();
    }

    def "Parse a single line to a single transaction"() {

        given:
        def transactionLine = "NEAA0000,20170201,BOT,10.512,3.3350,GILS"

        when:
        def transaction = subject.getTransaction(transactionLine)

        then:
        transaction
        transaction.account == "NEAA0000"
        transaction.date == LocalDate.of(2017, 2, 1)
        transaction.type == TxnType.BOT
        transaction.units == BigDecimal.valueOf(10.512)
        transaction.price == BigDecimal.valueOf(3.3350)
        transaction.asset == "GILS"
    }

    @Unroll
    def "If a transaction line has different number of columns than expected, ignore it"() {

        when:
        def transaction = subject.getTransaction(transactionLine)

        then:
        !transaction

        // data driven testing, multiple execution of this method with different values for the variable
        where:
        transactionLine << ["NEAA0000,20170201,BOT,10.512", "NEAA0000,20170201,BOT,10.512,10.512,10.512,10.512"]
    }

    @Unroll
    def "If a transaction line is null or empty, ignore it"() {

        when:
        def transaction = subject.getTransaction(transactionLine)

        then:
        !transaction

        where:
        transactionLine << [null, "", " "]
    }

    def "If a transaction line has invalid date, ignore it"() {

        given:
        def transactionLine = "NEAA0000,2017,BOT,10.512,3.3350,GILS"

        when:
        def transaction = subject.getTransaction(transactionLine)

        then:
        !transaction
    }

    def "If a transaction line has invalid type, ignore it"() {

        given:
        def transactionLine = "NEAA0000,20170201,AAA,10.512,3.3350,GILS"

        when:
        def transaction = subject.getTransaction(transactionLine)

        then:
        !transaction
    }

    def "If a transaction line has invalid units, ignore it"() {

        given:
        def transactionLine = "NEAA0000,20170201,BOT,AA,3.3350,GILS"

        when:
        def transaction = subject.getTransaction(transactionLine)

        then:
        !transaction
    }

    def "If a transaction line has invalid price, ignore it"() {

        given:
        def transactionLine = "NEAA0000,20170201,BOT,3350,BB,GILS"

        when:
        def transaction = subject.getTransaction(transactionLine)

        then:
        !transaction
    }
}
