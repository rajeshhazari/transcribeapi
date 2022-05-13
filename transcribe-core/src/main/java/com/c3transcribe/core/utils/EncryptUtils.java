package com.c3transcribe.core.utils;

import org.apache.calcite.util.Static;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static org.apache.logging.log4j.LogManager.getLogger;

@Component
public final class EncryptUtils {

    private static final Logger logger = getLogger(EncryptUtils.class);
    private static final char[] CHARACTERS = ("abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789").toCharArray();
    /**
     * Encryption strength.
     */
    private static final int STRENGTH = 12;
    
    /**
     * This method converts text on code of force 12.
     *
     * @param text Text to encode
     * @return Returns encoded text encrypted with STRENGTH of 12
     */
    public  String encryptWithBCryptAlgo(final String text) {
        return new BCryptPasswordEncoder(STRENGTH).encode(text);
    }
    
    /**
     *
     * @param text
     * @param strength
     * @return
     */
    public  String encryptWithBCryptAlgo(final String text, final Integer strength) {
        return new BCryptPasswordEncoder(strength).encode(text);
    }
    
    /**
     * This method compares texts codes.
     *
     * @param text Text to compare code with text2
     * @param text2 Text to compare code with text
     * @return Returns TRUE if texts are the same with encryption algorith as BCryptPasswordEncode and Stength of 12
     */
    public  boolean matches(final String text, final String text2) {
        return new BCryptPasswordEncoder(STRENGTH).matches(text, text2);
    }
    
    /**
     *
     * @param text
     * @param text2
     * @param strength
     * @return
     */
    public boolean matches(final String text, final String text2, final Integer strength) {
        return new BCryptPasswordEncoder(strength).matches(text, text2);
    }
    
    /**
     * This method generates a alphanumeric password
     * @return Returns the generated alphanumeric password.
     *
     * @see #CHARACTERS
     */
    public String generateRandomPassword() {
        final SecureRandom random = new SecureRandom();
        final byte randomLength = (byte) (random.nextInt(14) + 6);
        byte randomElementNumber;
        String password = "";
        
        for (int i = 0; i < randomLength; ++i) {
            randomElementNumber = (byte) random.nextInt(CHARACTERS.length);
            
            password = new StringBuilder(password).append(CHARACTERS[randomElementNumber]).toString();
        }
        
        return password;
    }
    
    
    /**
     * This method generates a token.
     *
     * @return Returns the generated token.
     */
    public String randomToken() {
        final java.util.Random random = new java.util.Random();
        
        final byte randomLength = (byte) (random.nextInt(28) + 12);
        byte randomElementNumber;
        String token = "";
        
        for (int i = 0; i < randomLength; ++i) {
            randomElementNumber = (byte) random.nextInt(CHARACTERS.length);
            
            token = new StringBuilder(token).append(CHARACTERS[randomElementNumber]).toString();
        }
        
        return token;
    }
    
    /**
     *This method generated random String using "SHA1PRNG" algorithm
     * @param size
     * @param algorithm
     * @return random string
     */
    public static String randomString(int size, String algorithm) {
        
        StringBuilder generatedToken = new StringBuilder();
        String defaultAlg = "SHA1PRNG";
        if(StringUtils.hasText(algorithm)){
            defaultAlg = algorithm;
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
