package com.c3transcribe.transribeapi.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;


public class DeepSpeechTranscribeService {
    
    private Logger logger = LoggerFactory.getLogger(DeepSpeechTranscribeService.class);
    
    /**
     *
     * @param url
     * @param data
     * @return
     */
    public byte[] postContent(final String url, Object... data) {
        
        /*Map<String, String> keyValuePairs = SystemUtils.getInstance().buildMap(new HashMap<String, String>(), data);
        final long startTiming = System.currentTimeMillis();
        logger.info("posting, url: " + url);
        HttpClient httpClient = getHttpClient();
        
        try {
            HttpPost httpPost = new HttpPost(url);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            for (String key : keyValuePairs.keySet()) nvps.add(new BasicNameValuePair(key, keyValuePairs.get(key)));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            
            ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {
                public byte[] handleResponse(final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();
                    logger.info("done posting, process: " + (System.currentTimeMillis() - startTiming) + "ms, url: " + url);
                    return entity != null ? EntityUtils.toByteArray(entity) : null;
                }
            };
            return httpClient.execute(httpPost, responseHandler); // cannot execute
        }
        catch (Exception e) {
            throw new SystemCommandException(url + ", " + keyValuePairs.toString(), e);
        }*/
        return null;
    }
    
    /**
     *
     * @return
     */
    private HttpClient getHttpClient() {
        
        
        /*try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
                
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }
                
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
            HttpClient client = new DefaultHttpClient(ccm, base.getParams());
            return client;
        } catch(Exception ex) {
            ex.printStackTrace();
            return base;
        }*/
        return null;
    }
}
