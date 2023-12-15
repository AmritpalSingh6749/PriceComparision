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

public class SnapDealScraper implements WebScraper{
    @Override
    public List<ScrapeData> scrape(String search) {
        String url = "https://www.snapdeal.com/search?keyword="+search+"&sort=rlvncy";
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<Future<ScrapeData>> futureList = new ArrayList<>();
        List<ScrapeData> dataList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            List<Element> elemList = doc.getElementsByClass("product-tuple-description");
            elemList.forEach(elem -> {
                futureList.add(executor.submit(()->{
                    String productURL = getUrl(elem);
                    Document productDoc = getProductDoc(productURL);
                    return ScrapeData.builder()
                            .title(getTitle(productDoc))
                            .URL(productURL)
                            .reviewCount(getReviewCount(productDoc))
                            .rating(getRating(productDoc))
                            .currentPrice(getCurrentPrice(productDoc))
                            .build();
                }));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return futureList.stream().map(this::getValue).toList();
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

    private String getCurrentPrice(Document doc) {
        return doc.select(".product-offer-price").text();
    }

    private String getRating(Element elem) {
        return elem.select(".avrg-rating").text();
    }

    private String getReviewCount(Element elem) {
        return elem.select(".numbr-review").text();
    }

    private String getUrl(Element elem) {
        return elem.select("a").getFirst().attr("href");
    }

    private String getTitle(Document doc) {
        return doc.select(".product-title").text();
    }
}
