package com.nutmeg.transactions.service.filter;

import com.nutmeg.transactions.file.FileReader;
import com.nutmeg.transactions.model.Transaction;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by Martin on 8/1/2017.
 * <p>
 * I prefer to not create an interface for classes that for sure won't have multiple implementations.
 * In this case, this class just needs to group by account number and filter by date, so I don't see
 * how we can change the implementation. Once again, for testing, Spock will allow me to just easyly
 * stub/mock it.
 */
public class TransactionFilterService {

    private final FileReader fileReader;

    public TransactionFilterService(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    //--------------------------------------------

    public Map<String, List<Transaction>> getTransactionsFilterByDateGroupByAccNumber(File transactionFile, LocalDate date) {
        List<Transaction> transactions = fileReader.getTransactions(transactionFile);

        Map<String, List<Transaction>> transactionsByAccount = transactions.stream()
                .filter(t -> t.getDate().isBefore(date) || t.getDate().isEqual(date))
                .collect(groupingBy(Transaction::getAccount));

        return transactionsByAccount;
    }
}
