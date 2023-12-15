package com.pricecomparision.ecommerce.Model;

import com.pricecomparision.ecommerce.dtos.ScrapeData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EtsyScraper implements WebScraper{
    @Override
    public List<ScrapeData> scrape(String search) {
        String url = "https://www.etsy.com/in-en/search?q="+search;
        List<ScrapeData> dataList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc);
            List<Element> elemList = doc.select(".listing-link .wt-display-inline-block");
            System.out.println(elemList);
            elemList.forEach(elem -> {
                System.out.println(elem);
                String productURL = getUrl(elem);
                Document productDoc = getProductDoc(productURL);
                ScrapeData data = ScrapeData.builder()
                        .title(getTitle(productDoc))
                        .URL(productURL)
                        .reviewCount(getReviewCount(productDoc))
                        .rating(getRating(productDoc))
                        .currentPrice(getCurrentPrice(elem))
                        .build();
                dataList.add(data);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataList;
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
