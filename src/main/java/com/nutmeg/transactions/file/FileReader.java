package com.nutmeg.transactions.file;

import com.nutmeg.transactions.model.Transaction;
import com.nutmeg.transactions.model.enums.TxnType;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Martin on 8/1/2017.
 * <p>
 * If I expect multiple implementations, I would definitely create an Interface, something
 * like DataSource with a method of getTransactions(). This can than be implemented by multiple
 * data sources, like File or Database one. But the main interface HoldingService that I need to implemented takes
 * as an input a File, so it makes it difficult to code to different data sources.
 * Because of that I just prefer to have a single class, with Spock is easy to Stub/Mock classes as well.
 */
public class FileReader {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final int NUM_OF_TRANSACTION_COLUMNS = 6;

    public List<Transaction> getTransactions(File dataFile) {
        List<Transaction> transactions = new ArrayList<>();
        try (Scanner scanner = new Scanner(dataFile)) {

            while (scanner.hasNext()) {
                String transactionLine = scanner.nextLine();
                Transaction transaction = getTransaction(transactionLine);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public Transaction getTransaction(String transactionLine) {
        if (transactionLine == null || transactionLine.isEmpty()) {
            return null;
        }

        String[] tStrArr = transactionLine.split(",");

        if (tStrArr.length != NUM_OF_TRANSACTION_COLUMNS) {
            return null;
        }

        try {
            Transaction transaction = new Transaction();
            transaction.setAccount(tStrArr[0]);
            transaction.setDate(LocalDate.parse(tStrArr[1], formatter));
            transaction.setType(TxnType.valueOf(tStrArr[2]));
            transaction.setUnits(new BigDecimal(tStrArr[3]));
            transaction.setPrice(new BigDecimal(tStrArr[4]));
            transaction.setAsset(tStrArr[5]);

            return transaction;
        } catch (Exception e) {
            return null;
        }
    }

}
