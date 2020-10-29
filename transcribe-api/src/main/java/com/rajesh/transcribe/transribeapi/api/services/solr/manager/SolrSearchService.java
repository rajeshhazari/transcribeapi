/**
 * Created by rhazari on 2/25/14.
 */

package com.rajesh.transcribe.transribeapi.api.services.solr.manager;


import com.rajesh.transcribe.transribeapi.api.models.SearchRequest;
import com.rajesh.transcribe.transribeapi.api.models.dto.TranscribedRespSolrInputDoc;
import com.rajesh.transcribe.transribeapi.api.models.dto.sphinx.TranscriptionResponseDto;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import static org.apache.logging.log4j.LogManager.getLogger;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

@Component
public class SolrSearchService {
    private static final Logger logger = getLogger(SolrSearchService.class);
    private final BeanUtilsBean beanUtilsBean2 = BeanUtilsBean2.getInstance();
    @Autowired
    public SolrClient solrClient;
    private final UpdateRequest updateRequest = null;
    private final SolrRequest solrRequest = null;
    @Value("${solr.transcribeapiapp.colName}")
    private String solrCollectionName;

    /**
     * @param collection
     * @param solrdoc
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteSolrDocList(String collection, SolrDocument solrdoc) throws SolrServerException,
            IOException {

        UpdateResponse updateResponse = solrClient.deleteById(collection, solrdoc.getFieldValue("id").toString());
    }

    /**
     * deletes list of the documents for solr sfor a given collection
     *
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteSolrDocList(String collection, List<SolrDocument> solrDocList) throws SolrServerException,
            IOException {
        List<String> idList = new ArrayList<>();
        solrDocList.forEach(doc -> {
            idList.add(doc.getFieldValue("id").toString());
        });

        UpdateResponse updateResponse = solrClient.addBeans(solrCollectionName, idList);
        if (updateResponse.getStatus() == 0) {
            logger.debug("Deleted docs from solr server {}  :: msg :: ", updateResponse.getRequestUrl(), updateResponse.getResponse());
        } else {
            logger.error("Problem while deleting docs from solr server {}  :: msg :: ", updateResponse.getRequestUrl(), updateResponse.getResponse());
            throw new SolrServerException("Problem while deleting docs from solr server");
        }
    }


    /**
     * deletes list of the documents for solr sfor a given collection
     *
     * @throws SolrServerException
     * @throws IOException
     */
    public void deleteSolrDocListByIdList(String collection, List<String> idList) throws SolrServerException,
            IOException {

        UpdateResponse updateResponse = solrClient.addBeans(solrCollectionName, idList);
        if (updateResponse.getStatus() == 0) {
            logger.debug("Deleted docs from solr server {}  :: ", updateResponse.getResponse());
        } else {
            logger.error("Problem while deleting idlists from solr server {}  :: msg :: ", updateResponse.getRequestUrl(), updateResponse.getResponse());
            throw new SolrServerException("Problem while deleting idlists from solr server");
        }
    }

    /**
     * @param responseDto
     * @throws SolrServerException
     * @throws IOException
     */
    public boolean indexTranscriptions(TranscriptionResponseDto responseDto) throws SolrServerException,
            IOException {
        Boolean status = Boolean.FALSE;

        TranscribedRespSolrInputDoc transcribedRespSolrDoc = createSolrInputDocFromDto(responseDto);
        UpdateResponse updateResponse = solrClient.addBean(solrCollectionName, transcribedRespSolrDoc);
        Map<String, Object> responseMap = updateResponse.getResponse().asShallowMap(false);
        responseMap.keySet().stream().forEach(key -> {
            logger.debug(" key :: {}   value :: {}", key, responseMap.get(key));
        });
        if (updateResponse.getStatus() == 0) {
            status = Boolean.TRUE;
        } else {
            logger.error("Problem while indexing response in solr server {}  :: msg :: {} transcriptionReqId:: {} ", updateResponse.getRequestUrl(), updateResponse.getResponse(), responseDto.getTrancriptionId());
        }
        return status;
    }

    /**
     * @param responseDtoList
     * @throws SolrServerException
     * @throws IOException
     */
    public boolean indexTranscriptionsList(List<TranscriptionResponseDto> responseDtoList) {
        Boolean status = Boolean.FALSE;
        AutoPopulatingList<TranscribedRespSolrInputDoc> solrDocumentsList = new AutoPopulatingList<>(TranscribedRespSolrInputDoc.class);
        responseDtoList.stream().forEach(dto -> {
            TranscribedRespSolrInputDoc transcribedRespSolrDoc = createSolrInputDocFromDto(dto);
            solrDocumentsList.add(transcribedRespSolrDoc);
        });
        UpdateResponse updateResponse = null;
        try {
            updateResponse = solrClient.addBeans(solrCollectionName, solrDocumentsList);
        } catch (SolrServerException ex) {
            ex.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Map<String, Object> responseMap = updateResponse.getResponse().asShallowMap(false);
        responseMap.keySet().stream().forEach(key -> {
            logger.debug(" key :: {}   value :: {}", key, responseMap.get(key));
        });
        if (updateResponse.getStatus() == 0) {
            status = Boolean.TRUE;
        } else {
            logger.error("Problem while indexing response in solr server {}  :: msg :: {} ", updateResponse.getRequestUrl(), updateResponse.getResponse());
        }
        return status;
    }

    /**
     * Create solrInputDoc from Transcribtion response
     *
     * @param responseDto
     * @return
     */
    private TranscribedRespSolrInputDoc createSolrInputDocFromDto(TranscriptionResponseDto responseDto) {
        TranscribedRespSolrInputDoc transcribedRespSolrInputDoc = new TranscribedRespSolrInputDoc();
        try {
            beanUtilsBean2.copyProperties(responseDto, transcribedRespSolrInputDoc);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return transcribedRespSolrInputDoc;
    }


    //TODO this need to be revisted

    /**
     * @param solrColName
     * @param searchRequest
     * @return
     */
    public List<SolrDocument> getSolrDocList(final Optional<String> solrColName, final SearchRequest searchRequest) {
        SolrQuery solrQuery = new SolrQuery();
        List<SolrDocument> solrDocumentList = new ArrayList<>();
        solrQuery.add("fileName", searchRequest.getFileName());
        String colName = null;
        if(solrColName.isPresent()){
            colName = solrColName.orElse(solrCollectionName);
        }
        try {

            QueryResponse response = solrClient.query(colName, solrQuery);
            solrDocumentList = response.getResults();
        } catch (SolrServerException e) {
            e.printStackTrace();
            logger.error("SolrServer error:: ", e.getMessage());

        } catch (IOException ioException) {
            ioException.printStackTrace();
            logger.error("IOException occured error:: ", ioException.getMessage());
        }
        return solrDocumentList;
    }
    
    
    /**
     * 
     * @param pageJsonObject
     * @return
     * @throws JSONException 
     */
    public SolrInputDocument createPageSolrDoc(JSONObject pageJsonObject) throws JSONException {

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", pageJsonObject.get("id"));
        doc.addField("title", pageJsonObject.get("title"));
        doc.addField("body", pageJsonObject.get("body"));
        doc.addField("url", pageJsonObject.get("url"));
        doc.addField("description", pageJsonObject.get("description"));
        doc.addField("lastModified", pageJsonObject.get("lastModified"));
        doc.addField("contentType", pageJsonObject.get("contentType"));
        doc.addField("tags", pageJsonObject.get("tags"));
        return doc;

    }
    
    
    /**
     * This method connects to the Solr server and indexes page content using
     * Solrj api. This is used by bulk update handler (servlet)
     *
     * @param Takes Json array and iterates over each object and index to solr
     * @return boolean true if it indexes successfully to solr server, else
     * false.
     */
    public boolean indexPagesToSolr(JSONArray indexPageData, String  colName) throws JSONException, SolrServerException,
            IOException {

        if (null != indexPageData) {
            for (int i = 0; i < indexPageData.length(); i++) {
                JSONObject pageJsonObject = indexPageData.getJSONObject(i);
                SolrInputDocument doc = createPageSolrDoc(pageJsonObject);
                solrClient.add(doc);
            }
            return true;
        }

        return false;
    }
}

