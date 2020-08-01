package com.rajesh.transcribe.transribeapi.api.controller.search;

import com.rajesh.transcribe.transribeapi.api.models.SearchRequest;
import com.rajesh.transcribe.transribeapi.api.models.dto.search.SearchResultDto;
import com.rajesh.transcribe.transribeapi.api.services.solr.manager.SolrSearchService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(
        value = "SearchController",
        description = "Default SearchController to search users transcriptions. ")
@RestController
public class SearchController {
    
    private final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private SolrSearchService solrSearchService;
    
    @Autowired
    public SearchController(SolrSearchService solrSearchService) {
        this.solrSearchService = solrSearchService;
    }
    
    @GetMapping(value = "/search")
    public HttpEntity<?> search(
            @RequestParam final SearchRequest searchRequest,
            HttpServletRequest request, HttpServletResponse response
    ) {
        String token = request.getHeader("token");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        
        SearchResultDto searchResultDto = new SearchResultDto();
        
        return  ResponseEntity.ok(searchResultDto);
    }
    
}
