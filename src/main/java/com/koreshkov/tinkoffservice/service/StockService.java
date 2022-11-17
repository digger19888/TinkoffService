package com.koreshkov.tinkoffservice.service;

import com.koreshkov.tinkoffservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
}
