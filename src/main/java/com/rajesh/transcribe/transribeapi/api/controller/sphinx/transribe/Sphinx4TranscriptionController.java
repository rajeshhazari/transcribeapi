package com.rajesh.transcribe.transribeapi.api.controller.sphinx.transribe;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rajesh.transcribe.transribeapi.api.models.sphinx4.TranscribtionResponseDto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Base64Utils;
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
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
	
	@Autowired
    SecureRandom secureRandom;

    @ApiOperation(value = "Get Transcibtion id for current user ", response = Map.class, tags = "getTranscribeReqId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path="/transcribeReqId" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	 @RequestMapping( value= "/transcribeReqId" , method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public Map<String,Object> getTranscribeReqId(HttpServletRequest req, HttpServletResponse res) throws NoSuchAlgorithmException {
         Authentication authentication = SecurityContextHolder.getContext()
                 .getAuthentication();
	     //Assert.notNull(req.getSession().isNew(), "Invalid Session.., Please authenticate");
		 /*Assert.notNull(req.getSession(), (req.getSession()) => {
                 HttpSession ses = req.getSession();
         });*/
		 /*if(req.getSession().isNew()){
		     res.setStatus(401);
             res.addCookie(new Cookie("session", req.getSession().getId()));
         }*/
	    Map<String,Object> resp = new HashMap<String,Object>();
	    	resp.put("reqid", secureRandom.nextLong());
        //generate a random number
        String randomNum = Integer.valueOf(secureRandom.nextInt()).toString();
    
        //get its digest
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] result =  sha.digest(randomNum.getBytes());
        final  String uid = Base64Utils.encodeToString(result);
        res.addCookie(new Cookie("session", req.getSession().getId()));
        resp.put("uid", uid);
	    return resp;
	  }
	 
	 
    //Angular example
    //http://jsfiddle.net/danialfarid/tqgr7pur/
	 @ApiOperation(value = "Get Transcibtion response from the Audio/mp3/wav file ", response = TranscribtionResponseDto.class, tags = "getTranscribtion")
	    @ApiResponses(value = { 
	            @ApiResponse(code = 200, message = "Suceess|OK"),
	            @ApiResponse(code = 401, message = "not authorized!"), 
	            @ApiResponse(code = 403, message = "forbidden!!!"),
	            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping(path="/transcribe" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE , consumes = {  MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE} )
    public ResponseEntity<TranscribtionResponseDto> getTranscribtion(@RequestParam("reqId")   @NotNull @NotBlank final String reqId,
                                                                     //@RequestParam("file") MultipartFile[] file) throws IOException {
                                                                     @RequestParam("file") final MultipartFile file, HttpServletRequest req, HttpServletResponse res) throws JsonParseException, JsonMappingException, IOException {
         String token = req.getHeader("token");
         String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
         logger.debug("Upload: name {} and size: {} ", file.getOriginalFilename(), file.getSize());
         //getFileChunkFromStream(file);
         TranscribtionResponseDto response = new TranscribtionResponseDto();
         // MetaData document = objectMapper.readValue(metaData, MetaData.class);
         response.setTrancribtionId(reqId);
         response.setFname(file.getOriginalFilename());
         HttpHeaders httpHeaders = new HttpHeaders();
         httpHeaders.add("Accept", "application/json");
         
         
         if (Objects.isNull(file)){
             res.setStatus(HttpStatus.BAD_REQUEST.value());
             res.flushBuffer();
             return new ResponseEntity<TranscribtionResponseDto>(response, HttpStatus.BAD_REQUEST);
        }
         if (!Objects.isNull(file) && !file.isEmpty()) {
             //TODO handle the file upload status logic and make this service more responsive
             // rather than user to wait untill all of the transcription is completed, which may take some time
            File convFile = convertMultipartFileToFile(file);
            try {
                    response = tService.transribeAudioforText(convFile,reqId,token, userEmail,req.getSession().getId(),file.getSize());
                } catch (NumberFormatException | ExecutionException e) {
                    logger.error("Exception occured while transcribe from service {}", e.getCause());
					return new ResponseEntity<TranscribtionResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			
            // store the bytes somewhere
             res.flushBuffer();
             return new ResponseEntity<TranscribtionResponseDto>(response, HttpStatus.ACCEPTED);
        }
         res.flushBuffer();
        return new ResponseEntity<TranscribtionResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    private File convertMultipartFileToFile(MultipartFile file) throws IOException
    {    
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        logger.info("Audio file content type :: {}  with size {} ",file.getContentType(), file.getSize());
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
    private String getFileChunkFromStream(MultipartFile file) {
    	
    	FileChannel fileChannel = null;
    	String content = new String();
    	FileInputStream fis = null;
    	int chunkSize=128;
		try
        {
            //file = new File(filePath.toString());
            MessageDigest  messageDigest = MessageDigest.getInstance("MD5");
            //checksum = getFileCheckSum(messageDigest, file);
            fis = new FileInputStream(file.getName());
            int fileSize = (int) file.getSize();
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
