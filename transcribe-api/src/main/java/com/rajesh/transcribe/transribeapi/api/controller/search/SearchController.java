package com.rajesh.transcribe.transribeapi.api.controller.search;

import com.rajesh.transcribe.transribeapi.api.models.dto.search.SearchResultDto;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
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
    
    public SearchController() {
    }
    
    @GetMapping(value = "/search")
    public HttpEntity<?> search(
            @RequestParam final String type,
            @RequestParam final String q,
            @RequestParam(defaultValue = "1") final int page,
            @RequestParam(defaultValue = "1") final int pageSize,
            HttpServletRequest request, HttpServletResponse response
    ) {
        SearchResultDto searchResultDto = new SearchResultDto();
        return  ResponseEntity.ok(searchResultDto);
    }
}
