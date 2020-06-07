/**
 * Created by rhazari on 2/25/14.
 */

package com.rajesh.transcribe.transribeapi.api.services.solr.manager;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;

import org.springframework.stereotype.Component;

import static org.apache.logging.log4j.LogManager.getLogger;

@Component
public class SolrManager {
private static final Logger logger = getLogger(SolrManager.class);
    private UpdateRequest updateRequest = null;
    private SolrRequest solrRequest = null;
    private String environment = null;
    private static HttpSolrClient server = null;

   
    /**
     * deletes all of the documents for solr server
     *
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteAll(HttpSolrClient server, String collection) throws SolrServerException,
            IOException {
        server.deleteByQuery("*:*");// delete everything!
    }
    
    /**
     * returns singleton instance of sole server
     * 
     * @return HttpSolrServer
     */
    public HttpSolrClient getSolrServer(){
    	
    	
    	return server;
    }
   

}

