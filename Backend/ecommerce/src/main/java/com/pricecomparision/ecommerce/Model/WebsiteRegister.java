package com.pricecomparision.ecommerce.Model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class WebsiteRegister {
    public static Map<Websites, WebScraper> register =
            Map.of(Websites.Flipkart, new FlipkartScrapper(),
                    Websites.SnapDeal, new SnapDealScraper(),
                    Websites.ShopClues, new ShopCluesScrapper());

}
