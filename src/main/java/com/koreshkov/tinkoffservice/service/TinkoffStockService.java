package com.koreshkov.tinkoffservice.service;

import com.koreshkov.tinkoffservice.dto.FigiesDto;
import com.koreshkov.tinkoffservice.dto.StockPrice;
import com.koreshkov.tinkoffservice.dto.StocksDto;
import com.koreshkov.tinkoffservice.dto.StocksPricesDto;
import com.koreshkov.tinkoffservice.dto.TickersDto;
import com.koreshkov.tinkoffservice.exception.StockNotFoundException;
import com.koreshkov.tinkoffservice.model.Currency;
import com.koreshkov.tinkoffservice.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService{
    private final OpenApi openApi;

    @Async
    public CompletableFuture <MarketInstrumentList> getMarketInstrumentTicket(String ticker) {
        log.info("Getting {} from Tinkoff", ticker);
        var context = openApi.getMarketContext();
        return context.searchMarketInstrumentsByTicker(ticker);
    }

    @Override
    public Stock getStockByTicker(String ticker) {
        var cf = getMarketInstrumentTicket(ticker);
        var list = cf.join().getInstruments();
        if(list.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock %S not found.", ticker));
        }

        var item = list.get(0);
        return new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()),
                "TINKOFF");
    }

    @Override
    public StocksDto getStocksByTickers(TickersDto tickers) {
        List<CompletableFuture<MarketInstrumentList>> marketInstrument = new ArrayList<>();
        tickers.getTickers().forEach(ticker ->marketInstrument.add(getMarketInstrumentTicket(ticker)));
        List<Stock> stocks = marketInstrument.stream()
                .map(CompletableFuture::join)
                .map(mi -> {
                    if(!mi.getInstruments().isEmpty()) {
                       return mi.getInstruments().get(0);
                    }
                    return null;
                })
                .filter(el -> Objects.nonNull(el))
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()),
                        "TINKOFF"
                ))
                .collect(Collectors.toList());
        return new StocksDto(stocks);
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        var orderbook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("Getting price {} from Tinkoff", figi);
        return orderbook;
    }

    @Override
    public StocksPricesDto getPrices(FigiesDto figiesDto) {
    List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
    figiesDto.getFigies().forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));
    var listPrises = orderBooks.stream()
            .map(CompletableFuture::join)
            .map(oo -> oo.orElseThrow(() -> new StockNotFoundException("Stock not found")))
            .map(orderbook -> new StockPrice(
                    orderbook.getFigi(),
                    orderbook.getLastPrice().doubleValue()
            ))
            .collect(Collectors.toList());
    return new StocksPricesDto(listPrises);
    }
}
