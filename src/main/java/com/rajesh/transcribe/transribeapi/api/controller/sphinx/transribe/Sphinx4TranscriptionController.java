package com.rajesh.transcribe.transribeapi.api.controller.sphinx.transribe;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rajesh.transcribe.transribeapi.api.models.sphinx4.TranscribtionResponse;
import com.rajesh.transcribe.transribeapi.api.services.sphinx4.transcribe.Sphinx4TranscribitionService;
import com.rajesh.transcribe.transribeapi.api.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 *
 */
@RestController
@Api(value="TranscribtionController", description="Main Controller for Transcription service")
public class Sphinx4TranscriptionController {
	
	
    private Logger logger = LoggerFactory.getLogger(Sphinx4TranscriptionController.class);

    
	@Autowired
    Environment env;
	
	@Autowired
    Sphinx4TranscribitionService tService;
    
	@Autowired
    JwtUtil jwtUtil;

    @ApiOperation(value = "Get Transcibtion id for current user ", response = Map.class, tags = "getTranscribeReqId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path="/transcribeReqId" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	 @RequestMapping( value= "/transcribeReqId" , method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public Map<String,Object> getTranscribeReqId(HttpServletRequest req, HttpServletResponse res) {
         Authentication authentication = SecurityContextHolder.getContext()
                 .getAuthentication();
	     Assert.notNull(req.getSession().isNew(), "Invalid Session.., Please authenticate");
		 /*Assert.notNull(req.getSession(), (req.getSession()) => {
                 HttpSession ses = req.getSession();
         });*/
		 /*if(req.getSession().isNew()){
		     res.setStatus(401);
             res.addCookie(new Cookie("session", req.getSession().getId()));
         }*/
	    Map<String,Object> resp = new HashMap<String,Object>();
	    	resp.put("reqid", UUID.randomUUID().toString());
	    	res.addCookie(new Cookie("session", req.getSession().getId()));
	    return resp;
	  }
	 
	 
    //Angular example
    //http://jsfiddle.net/danialfarid/tqgr7pur/
	 @ApiOperation(value = "Get Transcibtion response from the Audio/mp3/wav file ", response = TranscribtionResponse.class, tags = "getTranscribtion")
	    @ApiResponses(value = { 
	            @ApiResponse(code = 200, message = "Suceess|OK"),
	            @ApiResponse(code = 401, message = "not authorized!"), 
	            @ApiResponse(code = 403, message = "forbidden!!!"),
	            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping(path="/transcribe" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TranscribtionResponse> getTranscribtion(@RequestParam("reqId")   @NotNull @NotBlank final String reqId,
                                                                  @RequestParam("fname")   @NotNull @NotBlank final String fname,
                                                                  //@RequestParam("file") MultipartFile[] file) throws IOException {
                                                                  @RequestParam("file") final MultipartFile file, HttpServletRequest req, HttpServletResponse res) throws JsonParseException, JsonMappingException, IOException {
    	logger.debug("Upload: {}", fname);
    	TranscribtionResponse response = null;
         Authentication authentication = SecurityContextHolder.getContext()
                 .getAuthentication();
         String authHeader =    req.getHeader("");
         jwtUtil.extractUsername(authHeader);
    	// MetaData document = objectMapper.readValue(metaData, MetaData.class);
    	if (Objects.isNull(file)){
    		return new ResponseEntity<TranscribtionResponse>(response, HttpStatus.BAD_REQUEST);
    	}
        if (!Objects.isNull(file) && !file.isEmpty()) {
        	 	File convFile = convertMultipartFileToFile(file);
				try {
					response = tService.transcribeAudio(convFile,reqId);
				} catch (NumberFormatException | ExecutionException e) {
					// TODO Auto-generated catch block
					logger.error("Exception occured while transcribe from service {}", e.getCause());
					
					return new ResponseEntity<TranscribtionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			
            // store the bytes somewhere
            return new ResponseEntity<TranscribtionResponse>(response, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<TranscribtionResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    
    private File convertMultipartFileToFile(MultipartFile file) throws IOException
    {    
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        logger.info("Audio file content type :: {}  with size {} ",file.getContentType(), file.getSize());
        file.getResource();
        try(InputStream is = file.getInputStream()) {
          Files.copy(is, convFile.toPath(), StandardCopyOption.REPLACE_EXISTING); 
        }
      return convFile;
    }
    
    
    /**
     * 
     * @param file
     * @return
     */
    private String getFileChunkFromStream(File file) {
    	
    	FileChannel fileChannel = null;
    	String content = new String();
    	FileInputStream fis = null;
    	int chunkSize=128;
		try
        {
            //file = new File(filePath.toString());
            MessageDigest  messageDigest = MessageDigest.getInstance("MD5");
            //checksum = getFileCheckSum(messageDigest, file);
            fis = new FileInputStream(file);
            int fileSize = (int) file.length();
            //int fileSize = (int) fis.available();
            fileChannel = fis.getChannel();
            int numberOfChunks = (int) Math.ceil(fileChannel.size() / (double) chunkSize);
            int totalpacket = numberOfChunks;
            int totalFileSize = fileSize;
            int read = 0;

            while (numberOfChunks > 0)
            {

                //logger.info("file is :" + file + "checksum is:" + checksum);
                fileSize -= read;

                if (numberOfChunks > 1)
                {
                    ByteBuffer bytebuffer = ByteBuffer.allocate(chunkSize);
                    read = fileChannel.read(bytebuffer);
                    bytebuffer.flip();
                    
                    if (bytebuffer.hasRemaining())
                    {
                        content = new String(bytebuffer.array(), Charset.forName("UTF-8"));

                    }


                    bytebuffer.clear();

                }
                else
                {

                    String chunkData = new String();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);
                    fileChannel.read(byteBuffer);
                    chunkData = new String(byteBuffer.array(), Charset.forName("UTF-8"));
                    byteBuffer.clear();
                }

                numberOfChunks--;
            }
        }
        catch (IOException e)
        {
        	logger.error("IOException occured : ",e);
        }
        catch (NoSuchAlgorithmException nsae)
        {
        	logger.error("NoSuchAlgorithm error occured : ",nsae);
        }
        finally
        {
            try
            {
                fis.close();
                fileChannel.close();
            }
            catch (IOException ioe)
            {
            	logger.error("IOException occured while releasing the resources: ",ioe);
            }

        }
		return content;
    }
    

}






/**
 * @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    Advertisement storeAd(@RequestPart("ad") String adString, @RequestPart("file") MultipartFile file) throws IOException {

        Advertisement jsonAd = new ObjectMapper().readValue(adString, Advertisement.class);
//do whatever you want with your file and jsonAd
 * 
 */
