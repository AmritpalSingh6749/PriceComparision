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

public class FlipkartScrapper implements WebScraper{
    @Override
    public List<ScrapeData> scrape(String search) {
        String url = "https://www.flipkart.com/search?q="+search;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<Future<ScrapeData>> futurelist = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            List<Element> elemList = doc.getElementsByClass("_2B099V");
            elemList.forEach(elem -> {
                futurelist.add(executor.submit(() ->{
                    String productLink = getUrl(elem);
                    Document productDoc = getProductDocument(productLink);
                    return ScrapeData.builder()
                            .title(getTitle(elem))
                            .URL(productLink)
                            .reviewCount(getReviewCount(productDoc))
                            .rating(getRating(productDoc))
                            .currentPrice(getCurrentPrice(elem))
                            .build();
                }));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return futurelist.stream()
                .map(this::getValue)
                .toList();
    }

    private ScrapeData getValue(Future<ScrapeData> future) {
        while(!future.isDone()){}
        ScrapeData data;
        try {
            data = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    private Document getProductDocument(String productLink) {
        Document doc;
        try {
            doc = Jsoup.connect(productLink).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    private String getCurrentPrice(Element elem) {
        return elem.getElementsByClass("_30jeq3").text();
    }

    private String getRating(Document doc) {
        Element elem = doc.getElementsByClass("_3LWZlK").getFirst();
        return elem.toString().contains("_1D-8OL")? null : elem.text();
    }

    private String getReviewCount(Document doc) {
        List<Element> elementList = doc.getElementsByClass("_2_R_DZ");
        List<Element> reviewList = elementList.stream()
                .filter(element -> element.text().contains("reviews") || element.text().contains("Reviews"))
                .toList();
        return reviewList.isEmpty() ? null : (reviewList.getFirst().select("span").get(1).text());
    }

    private String getUrl(Element elem) {
        return "https://www.flipkart.com" + elem.select("a").getFirst().attr("href");
    }

    private String getTitle(Element elem) {
        return elem.getElementsByClass("IRpwTa").text();

    }
}
