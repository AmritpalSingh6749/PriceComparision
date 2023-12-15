package com.pricecomparision.ecommerce.Model;

import com.pricecomparision.ecommerce.dtos.ScrapeData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.Callable;

@Builder
@Getter
public class AsyncRunner implements Callable<List<ScrapeData>> {
    private WebScraper scraper;
    private String keyword;
    private Integer limit;
    @Override
    public List<ScrapeData> call() throws Exception {
        return webScraper();
    }
    private List<ScrapeData> webScraper(){
        List<ScrapeData> list = scraper.scrape(keyword);
        return list.size()<limit ? list : list.subList(0,limit);
    }
}
