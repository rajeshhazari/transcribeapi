package com.rajesh.transcribe.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SampleUniqueKeyCodeGenerator {

        public static String random(int size) {

            StringBuilder generatedToken = new StringBuilder();
            try {
                String number = SecureRandom.getInstance("SHA1PRNG").generateSeed(size).toString();
                // Generate 20 integers 0..20
                /*for (int i = 0; i < size; i++) {
                    generatedToken.append(number.nextInt(9));
                }*/

                generatedToken.append(RandomStringUtils.random(20, 0, 0, true, true, null, new SecureRandom()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return generatedToken.toString();
        }

        public static void main(String[] args) {
            System.out.println(random(20));
        }
}
