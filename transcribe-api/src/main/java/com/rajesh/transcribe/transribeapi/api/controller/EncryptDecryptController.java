package com.rajesh.transcribe.transribeapi.api.controller;

import com.rajesh.transcribe.transribeapi.api.models.AppError;
import com.rajesh.transcribe.transribeapi.api.models.dto.digest.DigestResponseDto;
import io.swagger.annotations.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

@Api(
        value = "EncryptionDecryptionController",
        description = "Encrypts and Decrypts given string ")
@RestController
public class EncryptDecryptController {
    
    private final Logger logger = LoggerFactory.getLogger(EncryptDecryptController.class);
    private static final String ALGO = "AES"; // Default uses ECB PKCS5Padding
    private static MessageDigestAlgorithms messageDigestAlgorithms;
    @Value("${app.io.uploadDir}")
    private String uploadDir;
    
    private SecureRandom secureRandom;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AppServiceUtils appServiceUtils;
    
    public EncryptDecryptController(BCryptPasswordEncoder bCryptPasswordEncoder,
                                    SecureRandom secureRandom,
                                    AppServiceUtils appServiceUtils){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.secureRandom = secureRandom;
        this.appServiceUtils = appServiceUtils;
    }
    
    @ApiOperation(value = "Encrypt given string api service", response = Map.class, httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully Encrypted."),
                    @ApiResponse(code = 401, message = "You are not authorized to encrypt."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @RequestMapping(value = "/encryptData", method = RequestMethod.POST, produces = "application/json")
    public Map<String, String> encryptData(@RequestParam String data, @RequestParam String secret,
                                       HttpServletRequest req, HttpServletResponse res) throws Exception {
        final Map<String, String> resp = new HashMap<>();
    
        final Key key = generateKey(secret);
        final Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        final byte[] encVal = c.doFinal(data.getBytes());
        final  String encodedString = Base64.getEncoder().encodeToString(encVal);
        res.setStatus(HttpStatus.OK.value());
        resp.put("encVal", encodedString);
        return resp;
        
    }
    
    @ApiOperation(value = "Decrypts given string api service", response = Map.class, httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully Decrypted."),
                    @ApiResponse(code = 401, message = "You are not authorized to encrypt."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @RequestMapping(value = "/decryptData", method = RequestMethod.POST, produces = "application/json")
    public Map<String, String> decryptData(@RequestParam String strToDecrypt, @RequestParam String secret,
                                       HttpServletRequest req, HttpServletResponse res) {
        Map<String, String> resp = new HashMap<>();
        try {
            Key key = generateKey(secret);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            resp.put("decVal",new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt))));
            res.setStatus(HttpStatus.OK.value());
        } catch (Exception ex) {
            logger.error("Error while decrypting: {}" , ex.toString());
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.put("resp", "Error while decrypting");
            resp.put("decVal", null);
        }
        return resp;
    }
    
    
    @ApiOperation(value = "Encrypt given string using springboot BCryptPasswordEncoder api service", response = Map.class, httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully Encrypted."),
                    @ApiResponse(code = 401, message = "You are not authorized to encrypt."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @RequestMapping(value = "/bencryptEncode", method = RequestMethod.POST, produces = "application/json")
    public Map<String, String> bencryptEncode(@RequestParam String data,
                                       HttpServletRequest req, HttpServletResponse res) throws Exception {
        final Map<String, String> resp = new HashMap<>();
        
        final String encVal = bCryptPasswordEncoder.encode(data);
        final  String encodedString = Base64.getEncoder().encodeToString(encVal.getBytes());
        res.setStatus(HttpStatus.OK.value());
        resp.put("bcryptEncVal", encodedString);
        resp.put("base64EncVal", Base64.getEncoder().encodeToString(data.getBytes()));
        return resp;
        
    }
    
    /**
     *
     * @param secret
     * @return
     * @throws Exception
     */
    private static Key generateKey(String secret) throws Exception {
        
        byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
        
        Key key = new SecretKeySpec(secret.getBytes(), ALGO);
        
        return key;
        
    }
    
    /**
     *
     * @param msg
     * @param algorithm
     * @return
     */
    public static byte[] encodeUsingGivenAlg(Charset charset,String msg, String algorithm) throws NoSuchAlgorithmException {
        final Charset defaultCharset = StandardCharsets.UTF_8;
        String defaultAlgorithm = MessageDigestAlgorithms.MD5;
        //byte [] digest = new DigestUtils(MessageDigestAlgorithms.SHA_224).digest(msg);
        
        if(null == charset){
            charset = defaultCharset;
        }
        if(StringUtils.hasText(algorithm)){
            algorithm = defaultAlgorithm;
        }
        byte[] bytesOfMessage = msg.getBytes(charset);
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        return thedigest;
    }
    
    /**
     *
     * @param data
     * @param algorithmName
     * @param req
     * @param res
     * @return
     */
    @ApiOperation(value = "Encrypt given string using MessageDigestAlgorithms algorithm ", response = Map.class, httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully Encrypted."),
                    @ApiResponse(code = 401, message = "You are not authorized to encrypt."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @RequestMapping(value = "/encode", method = RequestMethod.POST, produces = "application/json")
    public static ResponseEntity<?> encodeDataUsingGivenAlg(@RequestParam @ApiParam(name="data", allowableValues = "" , defaultValue = "" , example = "test") String data,
                                                            @RequestParam @ApiParam (name = "messageDigestAlgorithms" , defaultValue = MessageDigestAlgorithms.MD5 ) String algorithmName,
                                                         HttpServletRequest req, HttpServletResponse res){
        String token = req.getHeader("token");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        DigestResponseDto response = new DigestResponseDto();
        String defaultAlgorithm = MessageDigestAlgorithms.MD5;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        if(CollectionUtils.contains(Arrays.asList(MessageDigestAlgorithms.values()).iterator(),algorithmName)){
            defaultAlgorithm = algorithmName;
        }else {
            defaultAlgorithm = MD5;
        }
        final String hdigest = new DigestUtils(defaultAlgorithm).digestAsHex(data);
        response.setData(hdigest);
        response.setAlgorithm(defaultAlgorithm);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    
    /**
     *
     * @param file
     * @param messageDigestAlgorithms
     * @param req
     * @param res
     * @return
     */
    @ApiOperation(value = "Encrypt given file using MessageDigestAlgorithms algorithm ", response = Map.class, httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully Encrypted."),
                    @ApiResponse(code = 401, message = "You are not authorized to encrypt."),
                    @ApiResponse(
                            code = 403,
                            message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @RequestMapping(value = "/encodeFile", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<DigestResponseDto> encodeFileUsingGivenAlg(@RequestParam("file") final MultipartFile file,
                                                                     @RequestParam @ApiParam  String messageDigestAlgorithms,
                                                                     HttpServletRequest req, HttpServletResponse res)  {
        String token = req.getHeader("token");
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Upload: name {} and size: {} ", file.getOriginalFilename(), file.getSize());
        DigestResponseDto response = new DigestResponseDto();
        String content = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        try {
            if(file.isEmpty()){
                res.setStatus(HttpStatus.BAD_REQUEST.value());
                res.flushBuffer();
                AppError error = new AppError(HttpStatus.BAD_REQUEST,"File size is zero OR File is empty!");
                return new ResponseEntity<DigestResponseDto>(response, HttpStatus.BAD_REQUEST);
            } else if (!Objects.isNull(file) && !file.isEmpty() ){
                    //&& file.getContentType().equalsIgnoreCase(res.getContentType())) {
                String contentType = Files.probeContentType(Paths.get(file.getName()));
                //TODO handle the file upload status logic and make this service more responsive
                // rather than user to wait until all of the transcription is completed, which may take some time
                content = FileUtils.readFileToString(appServiceUtils.convertMultipartFileToFile(file, uploadDir), StandardCharsets.UTF_8);
            }
        }catch (IOException ioException){
            logger.error("IO Error while reading the file {} {} , contentType: {}  message : {}",file.getName(),uploadDir, file.getContentType(),ioException.getMessage() );
            AppError appError = new AppError(HttpStatus.INTERNAL_SERVER_ERROR, ioException.getMessage());
            new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(StringUtils.hasText(content)) {
            String defaultAlgorithm = MessageDigestAlgorithms.MD5;
            if (CollectionUtils.contains(Arrays.asList(MessageDigestAlgorithms.values()).iterator(), messageDigestAlgorithms)) {
                defaultAlgorithm = messageDigestAlgorithms;
            } else {
                defaultAlgorithm = MD5;
            }
            final String hdigest = new DigestUtils(defaultAlgorithm).digestAsHex(content);
            response.setAlgorithm(defaultAlgorithm);
            response.setData(hdigest);
        }else{
            AppError appError = new AppError(HttpStatus.NO_CONTENT,"Either File can't be read or the file is Empty!");
            response.setError(appError);
            return new ResponseEntity(response, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
