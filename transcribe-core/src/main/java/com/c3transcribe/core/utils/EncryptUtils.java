package com.c3transcribe.core.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class EncryptUtils {
    
    /**
     * Encryption strength.
     */
    private static final int STRENGTH = 12;
    
    /**
     * This method converts text on code of force 12.
     *
     * @param text Text to encode
     * @return Returns encoded text by force 12
     */
    public static String encrypt(final String text) {
        return new BCryptPasswordEncoder(STRENGTH).encode(text);
    }
    
    /**
     * This method compares texts codes.
     *
     * @param text Text to compare code with text2
     * @param text2 Text to compare code with text
     * @return Returns TRUE if texts are the same
     */
    public static boolean matches(final String text, final String text2) {
        return new BCryptPasswordEncoder(STRENGTH).matches(text, text2);
    }
    
    
    private static final char[] CHARACTERS = ("abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789").toCharArray();
    
    /**
     * This method generates a password.
     *
     * @return Returns the generated password.
     */
    public static String randomPassword() {
        final java.util.Random random = new java.util.Random();
        
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
    public static String randomToken() {
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
}
