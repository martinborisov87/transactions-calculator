# Stock Calculator
You are required to write a calculator that determines what stocks and cash our customers hold on a given date given a list of transactions.

The transactions are supplied in a CSV file in the format specified below, you may assume that all transactions for an account are grouped together.

Your solution should implement the following interface:
```
public interface HoldingCalculator {
    Map<String,List<Holding>> calculateHoldings(File transactionFile, LocalDate date);
}
```
The key on the map is the users account number, and ```Holding``` is a bean as defined at the end of these instructions.
## Calculation Requirements

- For the Stock purchase(sale) transactions you should deduct(increase) the cash balance of the customer by the value of the transaction increment(decrement) the holdings for that stock
- For a deposit(withdrawal) you should increase(decrease) the customers cash balance by the given amount
- For a dividend you should increase the customers cash balance by the given amount
- You may assume all decimal values are calculated to 4 decimal places with RoundingMode.HALF_UP if necessary
- If you are unable to parse a line in the input file simply ignore it

## Some notes on the implementation

- We are as interested in how you approach the solution to this problem as we are in a correct solution.  In addition to correctness, we will be looking at how it is tested, how it will scale and how it is designed.
- The only third party libraries you may make use of in your solution are those used for testing and building your solution. Some examples of acceptable third party libraries:
  - Testing frameworks (e.g. JUnit, TestNG, Cucumber-JVM)
  - Build tools (e.g. Maven, Gradle)
  - Mocking Frameworks (e.g. Mockito, Powermock)
- To be more explicit, do not make use of any third party libraries in the core implementation itself, use only what is available in standard JDK 8.
- There will be no header in the input file
- You should always report a customers cash balance even if it is zero
- Stock holdings of zero should not be included in the output
- You can assume customers can be overdrawn

# Input File Format

```
Account,Date,TxnType,Units,Price,Asset
```

You may assume that the input is sorted by account number and date, that is all transactions for a given account will be together in the input and ordered by date from earliest to most recent.

Where:
- ```Account``` - A string of 8 alphanumberic characters
- ```Date``` - In YYYYMMDD format
- ```TxnType``` - A 3 character string, one of 
  - ```BOT``` Stock Purchase
  - ```SLD``` Stock sale
  - ```DIV``` Dividend payment
  - ```DEP``` Deposit
  - ```WDR``` Withdrawal
- ```Units``` - The number of units bought or sold to 4 decimal places
- ```Asset``` - A four character string
  - For ```BOT```/```SLD``` transactions, this is the stock purchased/sold
  - For ```DIV``` transactions this will be the stock for which the dividend was issued
  - For ```DEP```/```WDR``` transactions it will be ```CASH```
- ```Units``` - Number of units purchased/sold to 4 decimal places
- ```Price``` - Price per unit to 4 decimal places.  For ```CASH``` transactions this will always be ```1```.

# Holding.java

[Holding.java](Holding.java)

# Example
## Example input file

```
NEAA0000,20170101,DEP,100,1,CASH
NEAA0000,20170102,BOT,20,2.123,VUKE
NEAA0000,20170102,BOT,30,1.500,VUSA
NEAA0000,20170201,DIV,0.2024,1,VUKE
NEAA0000,20170201,SLD,20,2.000,VUSA 
NEAA0000,20170201,BOT,10.512,3.3350,GILS



NEAB0001,20161201,DEP,10000,1,CASH
NEAB0001,20170301,WDR,5000,1,CASH
```

## Holdings given a date of 20170201

```
Current Holdings on 20170201:
    NEAA0000:
        VUSA    10.0000
        VUKE    20.0000
        GILS    10.5120
        CASH    17.6848
    NEAB0001:
        CASH    10000.0000
```

