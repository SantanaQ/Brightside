package org.saveload;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA256 {

    String encodeText(String text) {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = text.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            String encoded = Base64.getEncoder().encodeToString(hashedByetArray);
            return encoded;
        } catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    boolean verifyHash(String originalText, String originalHash)
    {
        if(originalHash == null || originalHash.isEmpty()) {
            return false;
        }
        String newHash = encodeText(originalText);
        return originalHash.equals(newHash);
    }

}
