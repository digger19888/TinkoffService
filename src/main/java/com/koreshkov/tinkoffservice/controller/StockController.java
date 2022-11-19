package com.koreshkov.tinkoffservice.controller;

import com.koreshkov.tinkoffservice.dto.FigiesDto;
import com.koreshkov.tinkoffservice.dto.StocksDto;
import com.koreshkov.tinkoffservice.dto.StocksPricesDto;
import com.koreshkov.tinkoffservice.dto.TickersDto;
import com.koreshkov.tinkoffservice.model.Stock;
import com.koreshkov.tinkoffservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StockController {
    private final StockService stockService;
    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks/getStocksByTickers")
    public StocksDto getStocksByTickers(@RequestBody TickersDto tickers) {
        return  stockService.getStocksByTickers(tickers);
    }

    @PostMapping("/prices")
    public StocksPricesDto getPrices (@RequestBody FigiesDto figiesDto) {
        return stockService.getPrices(figiesDto);
    }
}
