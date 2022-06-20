package com.c3transcribe.transcribeapi.api.controller.search;

import com.c3transcribe.transcribeapi.api.models.dto.search.SearchResultDto;
import com.c3transcribe.transcribeapi.api.services.search.manager.SolrSearchService;
import com.c3transcribe.transcribeapi.api.models.SearchRequest;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(
        value = "SearchController",
        description = "Default SearchController to search users transcriptions. ")
@RestController
public class SearchController {
    
    private final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private SolrSearchService solrSearchService;
    @Value("${solr.transcribeapiapp.colName}")
    private String solrCollName;
    
    @Autowired
    public SearchController(SolrSearchService solrSearchService) {
        this.solrSearchService = solrSearchService;
    }
    
    @GetMapping(value = "/search")
    public HttpEntity<?> search(
            @RequestBody SearchRequest searchRequest,
            HttpServletRequest request, HttpServletResponse response
    ) {
        String token = request.getHeader("token");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        
        SearchResultDto searchResultDto = new SearchResultDto();
        if(null == searchRequest){
            searchRequest = new SearchRequest();
        } else {
           //TODO
           //Handle not null usecase if there will be any
        }
        solrSearchService.getSolrDocList(null,searchRequest );
        
        return  ResponseEntity.ok(searchResultDto);
    }


}
