package com.pricecomparision.ecommerce.Controller;

import com.pricecomparision.ecommerce.Model.Websites;
import com.pricecomparision.ecommerce.Service.SearchService;
import com.pricecomparision.ecommerce.dtos.ScrapeData;
import com.pricecomparision.ecommerce.dtos.SearchResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/")
public class SearchController {
    private SearchService searchService;
    @GetMapping("/search")
    public SearchResponse getComparison(@RequestParam(value = "k", defaultValue = "") String  search,
                                        @RequestParam(value="n", defaultValue = "5") Integer num,
                                        @RequestParam(value="s", defaultValue = "")List<Websites> sites
    ){
        System.out.println(search+" - "+num+" - "+sites.size());
        return searchService.search(search, num, sites);
    }

}
