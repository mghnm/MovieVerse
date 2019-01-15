/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movieverse.movieverse.store.web.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *MD5 conversion for passwords which are stored in a basic MD5 encryption rather than plain text
 * @author mghan
 */
public class MD5Util {

    public static String generateMD5(String value) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdArray = messageDigest.digest(value.getBytes());

            BigInteger number = new BigInteger(1, mdArray);
            return number.toString(16);

        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
}
