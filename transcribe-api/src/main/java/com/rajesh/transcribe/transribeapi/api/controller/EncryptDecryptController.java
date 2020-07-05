package com.rajesh.transcribe.transribeapi.api.controller;

import io.swagger.annotations.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.*;

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
    public EncryptDecryptController(BCryptPasswordEncoder bCryptPasswordEncoder,
                                    SecureRandom secureRandom){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.secureRandom = secureRandom;
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
    @RequestMapping(value = "/encrypt", method = RequestMethod.POST, produces = "application/json")
    public Map<String, String> encrypt(@RequestParam String data, @RequestParam String secret,
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
    @RequestMapping(value = "/decrypt", method = RequestMethod.POST, produces = "application/json")
    public Map<String, String> decrypt(@RequestParam String strToDecrypt, @RequestParam String secret,
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
    @RequestMapping(value = "/bencrypt", method = RequestMethod.POST, produces = "application/json")
    public Map<String, String> bencrypt(@RequestParam String data,
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
        final String defaultAlgorithm = "MD5";
        byte [] digest = new DigestUtils(MessageDigestAlgorithms.SHA_224).digest(msg);
        
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
     * @param messageDigestAlgorithms
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
    @RequestMapping(value = "/encode/", method = RequestMethod.POST, produces = "application/json")
    public static ResponseEntity<?> encodeDataUsingGivenAlg(@RequestParam @ApiParam(name="data", allowableValues = "" , defaultValue = "" , example = "test") String data,
                                                            @RequestParam @ApiParam (name = "messageDigestAlgorithms" , defaultValue = MessageDigestAlgorithms.MD5 ) String messageDigestAlgorithms,
                                                         HttpServletRequest req, HttpServletResponse res){
        String defaultAlgorithm = MessageDigestAlgorithms.MD5;
        if(CollectionUtils.contains(Arrays.asList(MessageDigestAlgorithms.values()).iterator(),messageDigestAlgorithms)){
            defaultAlgorithm = messageDigestAlgorithms;
        }else {
            defaultAlgorithm = MD5;
        }
        final String hdigest = new DigestUtils(defaultAlgorithm).digestAsHex(data);
            return new ResponseEntity(hdigest, HttpStatus.OK);
    }
    
    
    /**
     *
     * @param data
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
    @RequestMapping(value = "/encode/", method = RequestMethod.POST, produces = "application/json")
    public static ResponseEntity<?> encodeFileUsingGivenAlg(@RequestParam @ApiParam String data, @RequestParam @ApiParam  String messageDigestAlgorithms,
                                                           HttpServletRequest req, HttpServletResponse res){
    
        String defaultAlgorithm = MessageDigestAlgorithms.MD5;
        if(CollectionUtils.contains(Arrays.asList(MessageDigestAlgorithms.values()).iterator(),messageDigestAlgorithms)){
            defaultAlgorithm = messageDigestAlgorithms;
        }else {
            defaultAlgorithm = MD5;
        }
        final String hdigest = new DigestUtils(defaultAlgorithm).digestAsHex(data);
        return new ResponseEntity(hdigest, HttpStatus.OK);
    }
    
    
    
    public String random(int size, String algorith) {
        
        StringBuilder generatedToken = new StringBuilder();
        String defaultAlg = "SHA1PRNG";
        if(StringUtils.hasText(algorith)){
            defaultAlg = algorith;
        }
        try {
            String number = SecureRandom.getInstance(defaultAlg).generateSeed(size).toString();
            generatedToken.append(RandomStringUtils.random(size, 0, 0, true, true, null, new SecureRandom()));
        } catch (NoSuchAlgorithmException e) {
            logger.error("unable to generate random password ", e.getMessage());
        }
        return generatedToken.toString();
    }
}
