package com.pricecomparision.ecommerce.dtos;

import com.pricecomparision.ecommerce.Model.Websites;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SearchResponse {
    public Map<Websites, List<ScrapeData>> response = new HashMap<>();
}
