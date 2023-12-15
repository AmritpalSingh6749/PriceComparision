package com.pricecomparision.ecommerce.Model;

import com.pricecomparision.ecommerce.dtos.ScrapeData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ShopCluesScrapper implements WebScraper{
    @Override
    public List<ScrapeData> scrape(String search) {
        String url = "https://www.shopclues.com/search?q="+search;
        List<ScrapeData> dataList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<Future<ScrapeData>> futureList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            List<Element> elemList = doc.getElementsByClass("search_blocks");
            elemList.forEach(elem -> {
                futureList.add(executor.submit(() -> {
                    String productURL = getUrl(elem);
                    Document productDoc = getProductDoc(productURL);
                    return ScrapeData.builder()
                            .title(getTitle(productDoc))
                            .URL(productURL)
                            .reviewCount(getReviewCount(productDoc))
                            .rating(getRating(productDoc))
                            .currentPrice(getCurrentPrice(elem))
                            .build();
                }));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return futureList.stream()
                .map(this::getValue)
                .toList();
    }

    private ScrapeData getValue(Future<ScrapeData> future){
        while(!future.isDone()){}
        ScrapeData data;
        try {
            data = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    private Document getProductDoc(String productURL) {
        Document doc;
        try {
            doc = Jsoup.connect(productURL).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    private String getCurrentPrice(Element elem) {
        return elem.select(".p_price").text();
    }

    private String getRating(Document doc) {
        return doc.select("span[itemprop=ratingValue]").text();
    }

    private String getReviewCount(Document doc) {
        return doc.select(".prd_reviews").select("a").text();
    }

    private String getUrl(Element elem) {
        return "https:"+elem.select("a").getFirst().attr("href");
    }

    private String getTitle(Document doc) {
        return doc.select("h1[itemprop = name]").text();
    }
}
