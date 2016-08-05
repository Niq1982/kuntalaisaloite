package fi.om.municipalityinitiative.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


public class HashCreator {

    private String salt;

    public HashCreator(String salt) {
        this.salt = salt;
    }

    public boolean isHash(String value, String expected) {
        return hash(value).equals(expected);
    }

    public String hash(String value) {
        return toSha1(salt + value);
    }

    private static String toSha1(String password) {
        String sha1;
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
