package com.pricecomparision.ecommerce.Model;

import lombok.Builder;

import java.util.List;

@Builder
public class PriceComparision {
    private String search;
    private Integer top;
    private User user;
//    private List<WebsiteAdapter> websiteAdapters;

}