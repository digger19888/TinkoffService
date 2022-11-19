package com.koreshkov.tinkoffservice.service;

import com.koreshkov.tinkoffservice.dto.FigiesDto;
import com.koreshkov.tinkoffservice.dto.StocksDto;
import com.koreshkov.tinkoffservice.dto.StocksPricesDto;
import com.koreshkov.tinkoffservice.dto.TickersDto;
import com.koreshkov.tinkoffservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
    StocksDto getStocksByTickers(TickersDto tickers);
    StocksPricesDto getPrices(FigiesDto figiesDto);
}
