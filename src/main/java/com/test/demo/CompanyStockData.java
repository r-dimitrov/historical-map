package com.test.demo;

import java.math.BigDecimal;

public class CompanyStockData implements Timestamp {

  private String ticker;
  private BigDecimal stockPrice;
  private Long timestamp;

  public CompanyStockData(String ticker, BigDecimal stockPrice, Long timestamp) {
    this.ticker = ticker;
    this.stockPrice = stockPrice;
    this.timestamp = timestamp;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public BigDecimal getStockPrice() {
    return stockPrice;
  }

  public void setStockPrice(BigDecimal stockPrice) {
    this.stockPrice = stockPrice;
  }

  @Override
  public Long getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "CompanyStockData{" +
            "ticker='" + ticker + '\'' +
            ", stockPrice=" + stockPrice +
            ", timestamp=" + timestamp +
            '}';
  }
}
