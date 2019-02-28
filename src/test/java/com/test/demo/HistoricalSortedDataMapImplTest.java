package com.test.demo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HistoricalSortedDataMapImplTest {

  private HistoricalDataMap<String, CompanyStockData> historicalDataMap;

  @Before
  public void setUp() {
    historicalDataMap = new HistoricalSortedDataMapImpl<>();
    historicalDataMap.add("AAPL", new CompanyStockData("AAPL", BigDecimal.valueOf(25.0), LocalDateTime.of(2019, 2, 25, 12, 10, 10).toEpochSecond(ZoneOffset.UTC)));
    historicalDataMap.add("AAPL", new CompanyStockData("AAPL", BigDecimal.valueOf(30.0), LocalDateTime.of(2019, 2, 25, 13, 10, 10).toEpochSecond(ZoneOffset.UTC)));
    historicalDataMap.add("AAPL", new CompanyStockData("AAPL", BigDecimal.valueOf(35.0), LocalDateTime.of(2019, 2, 26, 13, 10, 10).toEpochSecond(ZoneOffset.UTC)));
    historicalDataMap.add("SPY", new CompanyStockData("SPY", BigDecimal.valueOf(10.0), LocalDateTime.of(2019, 2, 26, 13, 10, 10).toEpochSecond(ZoneOffset.UTC)));
    historicalDataMap.add("SPY", new CompanyStockData("SPY", BigDecimal.valueOf(12.0), LocalDateTime.of(2019, 2, 26, 13, 11, 10).toEpochSecond(ZoneOffset.UTC)));
  }

  @Test
  public void historicalDataMapShouldContainTwoElements() {
    assertEquals(2, historicalDataMap.size());
  }

  @Test
  public void sizeShouldIncrementWhenAddingElements() {
    CompanyStockData companyStockData = new CompanyStockData("FB", BigDecimal.valueOf(100.0), LocalDateTime.of(2019, 2, 26, 13, 11, 10).toEpochSecond(ZoneOffset.UTC));
    historicalDataMap.add("FB", companyStockData);
    assertEquals(3, historicalDataMap.size());
  }

  @Test
  public void getEntityByCorrectTimestampShouldReturnCorrectPrice() {
    CompanyStockData companyStockData = historicalDataMap.get("AAPL", LocalDateTime.of(2019, 2, 25, 13, 10, 10).toEpochSecond(ZoneOffset.UTC));
    assertEquals(BigDecimal.valueOf(30.0), companyStockData.getStockPrice());
  }

  @Test
  public void getByMissingTimestampShouldFallBackToFirstAvailableLowerTimestamp() {
    CompanyStockData companyStockData = historicalDataMap.get("AAPL", LocalDateTime.of(2030, 2, 26, 13, 10, 10).toEpochSecond(ZoneOffset.UTC));
    assertEquals(BigDecimal.valueOf(35.0), companyStockData.getStockPrice());
  }


  @Test(expected = NoSuchElementException.class)
  public void missingLowerThanOthersTimestampShouldThrowException() {
    CompanyStockData companyStockData = historicalDataMap.get("AAPL", LocalDateTime.of(1900, 2, 25, 13, 10, 10).toEpochSecond(ZoneOffset.UTC));
    assertNull(companyStockData);
  }


  @Test(expected = NoSuchElementException.class)
  public void throwsExceptionIfKeyDoesNotExist() {
    historicalDataMap.get("GOOGLE", 1234567890);
  }

  @Test
  public void validateThreadSafety() throws InterruptedException {
    historicalDataMap = new HistoricalSortedDataMapImpl<>();
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    for (int i = 0; i < 4; i++) {
      executorService.execute(() -> {
        for (int j = 0; j < 100_000; j++) {
          historicalDataMap.add(""+j+"", new CompanyStockData(""+j+"", BigDecimal.valueOf(j), LocalDateTime.of(2019, 2, 25, 13, 10, 10).toEpochSecond(ZoneOffset.UTC)));
        }
      });
    }
    executorService.shutdown();
    executorService.awaitTermination(2, TimeUnit.SECONDS);
    assertEquals(100_000, historicalDataMap.size());
  }


}