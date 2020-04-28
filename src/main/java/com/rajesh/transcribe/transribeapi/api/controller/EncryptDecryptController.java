package com.rajesh.transcribe.transribeapi.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Api(
        value = "EncryptionDecryptionController",
        description = "Encrypts and Decrypts given string ")
@RestController
public class EncryptDecryptController {
    
    private static final String ALGO = "AES"; // Default uses ECB PKCS5Padding
    
    private final Logger logger = LoggerFactory.getLogger(EncryptDecryptController.class);
    
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public EncryptDecryptController(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
}
