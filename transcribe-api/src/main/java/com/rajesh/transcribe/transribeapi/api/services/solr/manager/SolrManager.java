/**
 * Created by rhazari on 2/25/14.
 */

package com.rajesh.transcribe.transribeapi.api.services.solr.manager;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.rajesh.transcribe.transribeapi.api.models.dto.TranscribedRespSolrInputDoc;
import com.rajesh.transcribe.transribeapi.api.models.dto.sphinx.TranscriptionResponseDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.util.CollectionUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;

import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.util.SolrPluginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.repository.query.DtoInstantiatingConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import static org.apache.logging.log4j.LogManager.getLogger;

@Component
public class SolrManager {
private static final Logger logger = getLogger(SolrManager.class);
    
    
    private UpdateRequest updateRequest = null;
    private SolrRequest solrRequest = null;
    
    @Value("${solr.transcribeapiapp.colName}")
    private  String solrCollectionName;
    @Autowired
    public SolrClient solrClient;
    
    /**
     *
     * @param collection
     * @param solrdoc
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteSolrDocList( String collection, SolrDocument solrdoc) throws SolrServerException,
            IOException {
        
        UpdateResponse updateResponse = solrClient.deleteById(collection,solrdoc.getFieldValue("id").toString());
    }
    
    /**
     * deletes list of the documents for solr sfor a given collection
     *
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteSolrDocList( String collection, List<SolrDocument> solrDocList) throws SolrServerException,
            IOException {
        List<String> idList = new ArrayList<>();
                solrDocList.forEach(doc -> {
            idList.add(doc.getFieldValue("id").toString());
        });
        
        UpdateResponse updateResponse = solrClient.addBeans(solrCollectionName,idList);
        if(updateResponse.getStatus() == 0){
            logger.debug("Deleted docs from solr server {}  :: msg :: ",updateResponse.getRequestUrl(), updateResponse.getResponse());
        }else {
            logger.error("Problem while deleting docs from solr server {}  :: msg :: ",updateResponse.getRequestUrl(), updateResponse.getResponse());
            throw new SolrServerException("Problem while deleting docs from solr server");
        }
    }
    
    
    /**
     * deletes list of the documents for solr sfor a given collection
     *
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteSolrDocListByIdList( String collection, List<String> idList) throws SolrServerException,
            IOException {
        
        UpdateResponse updateResponse = solrClient.addBeans(solrCollectionName,idList);
        if(updateResponse.getStatus() == 0){
            logger.debug("Deleted docs from solr server {}  :: ", updateResponse.getResponse());
        }else {
            logger.error("Problem while deleting idlists from solr server {}  :: msg :: ",updateResponse.getRequestUrl(), updateResponse.getResponse());
            throw new SolrServerException("Problem while deleting idlists from solr server");
        }
    }
    
    /**
     *
     * @param responseDto
     * @throws SolrServerException
     * @throws IOException
     */
    public boolean indexTranscibtionResp(TranscriptionResponseDto responseDto) throws SolrServerException,
            IOException {
        Boolean status = Boolean.FALSE;
        
        TranscribedRespSolrInputDoc transcribedRespSolrDoc = getSolrInputDocFromObject(responseDto);
        AutoPopulatingList<TranscribedRespSolrInputDoc> solrDocumentsList = new AutoPopulatingList<>(TranscribedRespSolrInputDoc.class);
        solrDocumentsList.add(transcribedRespSolrDoc);
        UpdateResponse updateResponse = solrClient.addBeans(solrCollectionName,solrDocumentsList);
        Map<String, Object> responseMap = updateResponse.getResponse().asShallowMap(false);
        responseMap.keySet().stream().forEach(key -> {
            logger.debug(" key :: {}   value :: {}", key , responseMap.get(key) );
        });
        if(updateResponse.getStatus() == 0){
            status = Boolean.TRUE;
        }else {
            logger.error("Problem while indexing response in solr server {}  :: msg :: ",updateResponse.getRequestUrl(), updateResponse.getResponse());
        }
        return status;
    }
    
    /**
     * Create solrInputDoc from Transcribtion response
     * @param responseDto
     * @return
     */
    private TranscribedRespSolrInputDoc getSolrInputDocFromObject(TranscriptionResponseDto responseDto){
        TranscribedRespSolrInputDoc transcribedRespSolrInputDoc = new TranscribedRespSolrInputDoc();
        
        return transcribedRespSolrInputDoc;
    }
}

