package com.pricecomparision.ecommerce.Model;

import com.pricecomparision.ecommerce.dtos.ScrapeData;

import java.net.URL;
import java.util.List;

public interface WebScraper {
    List<ScrapeData> scrape(String url);
}
