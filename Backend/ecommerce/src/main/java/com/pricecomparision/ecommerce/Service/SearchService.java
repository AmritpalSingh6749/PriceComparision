package com.pricecomparision.ecommerce.Service;

import com.pricecomparision.ecommerce.Model.*;
import com.pricecomparision.ecommerce.Repository.SearchRepository;
import com.pricecomparision.ecommerce.dtos.ScrapeData;
import com.pricecomparision.ecommerce.dtos.SearchResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class SearchService {
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
//    private static final List<Websites> site = List.of(Websites.Flipkart, Websites.SnapDeal, Websites.ShopClues);
    private SearchRepository repository;
    public SearchResponse search(String keyword, Integer num, List<Websites> siteList) {
        List<Websites> sites = siteList.isEmpty() ?  List.of(Websites.Flipkart, Websites.SnapDeal, Websites.ShopClues) : siteList;
        List<Future<List<ScrapeData>>> futureList = new ArrayList<>();
        for(Websites site : sites){
            AsyncRunner runner = AsyncRunner.builder()
                    .limit(num)
                    .keyword(keyword)
                    .scraper(WebsiteRegister.register.get(site))
                    .build();
            futureList.add(executor.submit(runner));
        }
        return retrieveFuture(futureList, sites);
    }
    private SearchResponse retrieveFuture(List<Future<List<ScrapeData>>> futureList, List<Websites> sites){
        SearchResponse response = new SearchResponse();
        IntStream.range(0,futureList.size()).forEach(index -> {
            Future<List<ScrapeData>> future = futureList.get(index);
            while (!future.isDone()){}
            try {
                response.getResponse().put(sites.get(index), future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        return response;
    }
}
