package com.c3transcribe.transcribeapi.api.controller.search;

import com.c3transcribe.transcribeapi.api.models.dto.search.SearchResultDto;
import com.c3transcribe.transcribeapi.api.services.solr.manager.SolrSearchService;
import com.c3transcribe.transcribeapi.api.models.SearchRequest;
import io.swagger.annotations.Api;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Api(
        value = "SearchController",
        description = "Default SearchController to search users transcriptions. ")
@RestController
public class SearchController {
    
    private final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final Environment environment;
    private SolrSearchService solrSearchService;
    @Value("${solr.transcribeapiapp.colName}")
    private String solrCollName;
    
    @Autowired
    public SearchController(SolrSearchService solrSearchService, Environment environment) {
        this.solrSearchService = solrSearchService;
        this.environment = environment;
    }
    
    @GetMapping(value = "/search")
    public HttpEntity<?> search( @NotNull  @RequestBody SearchRequest searchRequest,
            HttpServletRequest request, HttpServletResponse response) throws SolrServerException, IOException {
        String token = request.getHeader("token");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Cookie[] cookies = request.getCookies();

        SearchResultDto searchResultDto = new SearchResultDto();
        if(null == searchRequest){
            //TODO Ideally this is not required, we need to test this before we remove this code
            return ResponseEntity.badRequest()
                    .body("Search request object cannot be null/empty.");
        } else {
           //TODO Make a search request to solr instance and return the transcribed files and more metadata
           //Handle not null use-case if there will be any
        }
        List<SolrDocument> solrDocumentList =  solrSearchService.getSolrDocList(Optional.of(solrCollName),searchRequest );
        
        return  ResponseEntity.ok(searchResultDto);
    }


}
