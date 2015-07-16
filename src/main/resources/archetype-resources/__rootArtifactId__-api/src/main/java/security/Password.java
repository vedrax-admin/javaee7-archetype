package ${package}.security;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author VEDRAX SAS
 */
public class Password {

    private SecureRandom random;
    private static final String CHARSET = "UTF-8";
    private static final String ENCRYPTION_ALGORITHM = "SHA-512";

    /**
     * Generate a secure salt from SecureRandom with a given length
     *
     * @param length
     * @return
     */
    public byte[] getSalt(int length) {
        random = new SecureRandom();
        byte bytes[] = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Hash a password with a salt.
     *
     * @param password
     * @param salt
     * @return
     */
    public byte[] hashWithSalt(String password, byte[] salt) {
        byte[] hash = null;
        try {
            byte[] bytesOfMessage = password.getBytes(CHARSET);
            MessageDigest md;
            md = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            md.reset();
            md.update(salt);
            md.update(bytesOfMessage);
            hash = md.digest();

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, "Encoding Problem", ex);
        }
        return hash;
    }

    /**
     * Hash with a slow salt. PBKDF2WithHmacSHA1
     *
     * @param password
     * @param salt
     * @return
     */
    public byte[] hashWithSlowsalt(String password, byte[] salt) {
        SecretKeyFactory factory;
        Key key = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keyspec = new PBEKeySpec(password.toCharArray(), salt, 1000, 512);
            key = factory.generateSecret(keyspec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        }
        return key.getEncoded();
    }

    /**
     * Get a string from byte[] with bas64 encoding
     *
     * @param text
     * @return
     */
    public String base64FromBytes(byte[] text) {
       String result = null;
        try {
            //Result in one line by not using chunk 
            result = new String(Base64.encodeBase64(text, false),CHARSET);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Get a byte[] from a string with base64 encoding
     *
     * @param text
     * @return
     */
    public byte[] bytesFrombase64(String text) {
        return Base64.decodeBase64(text);
    }
}
