package com.pricecomparision.ecommerce.Model;

import com.pricecomparision.ecommerce.dtos.ScrapeData;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmazonScraper implements WebScraper{
    @Override
    public List<ScrapeData> scrape(String url) {
        List<ScrapeData> dataList = new ArrayList<>();
        try {
            Document doc = Jsoup
                    .connect(url)
//                    .headers(res.headers())
//                    .cookie("session-token","6d7nWG/VnyAJrIEw3gl2TXox1eaCIrd4bQmt2M3LHSCa85YsK5xDtOuRzC08dD6S0KqTlMWBzJhMOaiueVSQrLSclWSc1JqKo9pc6OjWt+J2Qz+ou+sN21LZWcDv1TPBXqCWJcsqfnbb5t1J89+qfczJlRI+kc3bdBVVHhRb2PA9hUzIVcCtIPJhyvdqU9d7Ve34pVqE+C59Rc793k1+buvpGafQ8rJ7stUt/wakRXG9sADCLFCveE1BDPce7HGEkD/imxGJ3QMG3FFgS395OJP6gWuqTGpq6xy5/DLNzShhTNbNuWTEdfsxaJFXhIUW9PwEwJRAQGor0smU4YQbkV+m6tAJ3e41")
                    .get();

            List<Element> elemList = doc.getElementsByClass("template=SEARCH_RESULTS");
            elemList.forEach(elem -> {
                ScrapeData data = ScrapeData.builder()
                        .title(getTitle(elem))
                        .URL(getUrl(elem))
                        .reviewCount(getReviewCount(elem))
                        .rating(getRating(elem))
                        .currentPrice(getCurrentPrice(elem))
                        .build();
                dataList.add(data);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataList;
    }

    private String getCurrentPrice(Element elem) {
        return elem.getElementsByClass("a-price-symbol").text()+elem.getElementsByClass("a-price-whole").text();
    }

    private String getRating(Element elem) {
        return elem.getElementsByClass("a-icon-alt").text();
    }

    private String getReviewCount(Element elem) {
        return elem.getElementsByClass("a-size-base s-underline-text").text();
    }

    private String getUrl(Element elem) {
        return "https://www.amazon.in" + elem.getElementsByClass("s-no-outline").getFirst().attr("href");
    }

    private String getTitle(Element elem) {
        List<Element> elemList = elem.getElementsByClass("a-size-base-plus");
        List<String> stringList = elemList.stream()
                .map(Element::text)
                .toList();
        return String.join(" ", stringList);
    }
}
