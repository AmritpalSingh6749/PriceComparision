package com.pricecomparision.ecommerce.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScrapeData {
    private String URL;
    private String title;
    private String reviewCount;
    private String rating;
    private String currentPrice;
}
