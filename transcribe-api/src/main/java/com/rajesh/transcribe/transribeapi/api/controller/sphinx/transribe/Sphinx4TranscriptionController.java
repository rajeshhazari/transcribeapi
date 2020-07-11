package com.rajesh.transcribe.transribeapi.api.controller.sphinx.transribe;

import com.c3transcribe.core.utils.EncryptUtils;
import com.rajesh.transcribe.transribeapi.api.controller.AppServiceUtils;
import com.rajesh.transcribe.transribeapi.api.models.AppError;
import com.rajesh.transcribe.transribeapi.api.models.dto.sphinx.TranscriptionResponseDto;
import com.rajesh.transcribe.transribeapi.api.services.sphinx4.transcribe.Sphinx4TranscribitionService;
import com.rajesh.transcribe.transribeapi.api.util.JwtUtil;
import edu.cmu.sphinx.linguist.UnitSearchState;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
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
@Api(value = "TranscriptionController", description = "Controller for Transcription service Using Sphinx4 Transcription models")
public class Sphinx4TranscriptionController {
    
    Environment env;
    Sphinx4TranscribitionService tService;
    JwtUtil jwtUtil;
    SecureRandom secureRandom;
    @Value("${app.io.uploadDir}")
    private String uploadDir;
    private AppServiceUtils appServiceUtils;
    
    private Logger logger = LoggerFactory.getLogger(Sphinx4TranscriptionController.class);
    
    public Sphinx4TranscriptionController(Environment env,
                                          Sphinx4TranscribitionService tService,
                                          JwtUtil jwtUtil,
                                          SecureRandom secureRandom, AppServiceUtils appServiceUtils) {
        this.env = env;
        this.tService = tService;
        this.jwtUtil = jwtUtil;
        this.secureRandom = secureRandom;
        this.appServiceUtils = appServiceUtils;
        
    }
    
    @PostConstruct
    private void checkUploadDirStats() throws NotDirectoryException, IOException {
        logger.debug("File System info: isReadable:: {}", Files.isReadable(Paths.get(uploadDir)));
        logger.debug("File System info: isWriteable:: {}", Files.isWritable(Paths.get(uploadDir)));
        logger.debug("File System info: size:: {}", Files.size(Paths.get(uploadDir)));
        Assert.isTrue(Files.exists(Paths.get(uploadDir)), "uploadDir does not exist");
        Assert.isTrue(Files.isReadable(Paths.get(uploadDir)),"uploadDir is not Readable");
        Assert.isTrue(Files.isWritable(Paths.get(uploadDir)),"uploadDir is not Writable ");
    }
    @ApiOperation(value = "Get Transcription id for current user ", response = Map.class, tags = "getTranscribeReqId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @GetMapping(path = "/transcribeReqId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/transcribeReqId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> getTranscribeReqId(HttpServletRequest req, HttpServletResponse res) throws NoSuchAlgorithmException {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        res.addCookie(new Cookie("session", req.getSession().getId()));
        Map<String, Object> resp = new HashMap<String, Object>();
        resp.put("reqid", EncryptUtils.randomString(20, null));
        final String uid = UuidUtil.getTimeBasedUuid().toString();
        resp.put("uid", uid);
        return resp;
    }
    
    //http://jsfiddle.net/danialfarid/tqgr7pur/
    @ApiOperation(value = "Get Transcription response from the Audio/mp3/wav file ", response = TranscriptionResponseDto.class, tags = "getTranscription")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @PostMapping(path = "/transcribe", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<TranscriptionResponseDto> getTranscription(
            @RequestParam("reqId") @NotNull @NotBlank final String reqId,
            //@RequestParam("file") MultipartFile[] file) throws IOException {
            @RequestParam("file") final MultipartFile file, HttpServletRequest req, HttpServletResponse res
    ) throws IOException {
        String token = req.getHeader("token");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Upload: name {} and size: {} ", file.getOriginalFilename(), file.getSize());
        //getFileChunkFromStream(file);
        TranscriptionResponseDto response = new TranscriptionResponseDto();
        if(file.isEmpty()){
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.flushBuffer();
            AppError error = new AppError(HttpStatus.BAD_REQUEST,"File size is zero OR File is empty!");
            response.setError(error);
            return new ResponseEntity<TranscriptionResponseDto>(response, HttpStatus.BAD_REQUEST);
        } // MetaData document = objectMapper.readValue(metaData, MetaData.class);
        response.setTrancriptionId(reqId);
        response.setFileName(file.getOriginalFilename());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        
        
        if (Objects.isNull(file)) {
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.flushBuffer();
            AppError error = new AppError(HttpStatus.BAD_REQUEST,"Uploaded file recevied as null!");
            response.setError(error);
            return new ResponseEntity<TranscriptionResponseDto>(response, HttpStatus.BAD_REQUEST);
        }
        if (!Objects.isNull(file) && !file.isEmpty()) {
            //TODO handle the file upload status logic and make this service more responsive
            // rather than user to wait until all of the transcription is completed, which may take some time
            File convFile = appServiceUtils.convertMultipartFileToFile(file,uploadDir);
            try {
                response = tService.transribeAudioforText(convFile, reqId, token, userEmail, req.getSession().getId(), file.getSize());
            } catch (NumberFormatException | ExecutionException e) {
                AppError error = new AppError(HttpStatus.INTERNAL_SERVER_ERROR,"Exception occurred while transcribe from service");
                response.setError(error);
                logger.error("Exception occurred while transcribe from service {}", e.getCause());
                return new ResponseEntity<TranscriptionResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            // store the bytes somewhere
            res.flushBuffer();
            return new ResponseEntity<TranscriptionResponseDto>(response, HttpStatus.ACCEPTED);
        }
        res.flushBuffer();
        return new ResponseEntity<TranscriptionResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * @param file
     * @return
     */
    private String getFileChunkFromStream(MultipartFile file) {
        
        FileChannel fileChannel = null;
        String content = "";
        FileInputStream fis = null;
        int chunkSize = 128;
        try {
            //file = new File(filePath.toString());
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //checksum = getFileCheckSum(messageDigest, file);
            fis = new FileInputStream(file.getName());
            int fileSize = (int) file.getSize();
            //int fileSize = (int) fis.available();
            fileChannel = fis.getChannel();
            int numberOfChunks = (int) Math.ceil(fileChannel.size() / (double) chunkSize);
            int totalpacket = numberOfChunks;
            int totalFileSize = fileSize;
            int read = 0;
            
            while (numberOfChunks > 0) {
                
                //logger.info("file is :" + file + "checksum is:" + checksum);
                fileSize -= read;
                
                if (numberOfChunks > 1) {
                    ByteBuffer bytebuffer = ByteBuffer.allocate(chunkSize);
                    read = fileChannel.read(bytebuffer);
                    bytebuffer.flip();
                    
                    if (bytebuffer.hasRemaining()) {
                        content = new String(bytebuffer.array(), StandardCharsets.UTF_8);
                        
                    }
                    bytebuffer.clear();
                    
                } else {
                    String chunkData = "";
                    ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);
                    fileChannel.read(byteBuffer);
                    chunkData = new String(byteBuffer.array(), StandardCharsets.UTF_8);
                    byteBuffer.clear();
                }
                
                numberOfChunks--;
            }
        } catch (IOException e) {
            logger.error("IOException occured : {} ", e);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("NoSuchAlgorithm error occurred : {} ", nsae);
        } finally {
            try {
                fis.close();
                fileChannel.close();
            } catch (IOException ioe) {
                logger.error("IOException occured while releasing the resources: ", ioe);
            }
            
        }
        return content;
    }
    
    
}


/**
 * @RequestMapping(value = "/upload", method = RequestMethod.POST)
 * public @ResponseBody
 * Advertisement storeAd(@RequestPart("ad") String adString, @RequestPart("file") MultipartFile file) throws IOException {
 * <p>
 * Advertisement jsonAd = new ObjectMapper().readValue(adString, Advertisement.class);
 * //do whatever you want with your file and jsonAd
 */