package ${package}.security;

import java.security.MessageDigest;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author VEDRAX SAS
 */
public class SecurityUtil {

    private static final Logger logger = LogManager.getLogger(SecurityUtil.class.getName());

    /**
     * BASE64 encoder implementation. Provides encoding methods, using the
     * BASE64 encoding rules, as defined in the MIME specification
     * @param bytes
     * @return 
     */
    public static String encodeBase64(byte[] bytes) {
        String base64 = null;
        try {
            base64 = Base64Encoder.encode(bytes);
        } catch (Exception e) {
            logger.error("Password hash calculation failed", e);
        }
        return base64;
    }

    /**
     * Calculate a password hash using a MessageDigest.
     *
     * @param hashAlgorithm - the MessageDigest algorithm name
     * @param password - the password string to be hashed
     * @return 
     */
    public static String createPasswordHash(String hashAlgorithm, String password) {
        Validate.notNull(hashAlgorithm, "Null not allowed for hashAlgorithm");
        Validate.notNull(password, "Null not allowed for password");
        String passwordHash = null;

        // convert password to byte data
        byte[] passBytes = password.getBytes();

        // calculate the hash and apply the encoding.
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            md.update(passBytes);
            byte[] hash = md.digest();
            passwordHash = encodeBase64(hash);
        } catch (Exception e) {
            logger.error("Password hash calculation failed", e);
        }
        return passwordHash;
    }
}

