package com.test.demo.controller;

import com.test.demo.CompanyStockData;
import com.test.demo.HistoricalDataMap;
import com.test.demo.HistoricalSortedDataMapImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Controller
@RestController
public class HomeController {

  private HistoricalDataMap<String, CompanyStockData> companytData = new HistoricalSortedDataMapImpl<>();

  @GetMapping("/add")
  public String add(@RequestParam("ticker") String ticker, @RequestParam("price") BigDecimal price,
                    @RequestParam("timestamp") Long timestamp) {

    companytData.add(ticker, new CompanyStockData(ticker, price, timestamp));
    return "Added timestamp {} " + timestamp;
  }

  @GetMapping("/get/{ticker}")
  public CompanyStockData get(@PathVariable("ticker") String ticker, @RequestParam("timestamp") Long timestamp) {
    CompanyStockData companyStockData = companytData.get(ticker, timestamp);
    return companyStockData;
  }


}
