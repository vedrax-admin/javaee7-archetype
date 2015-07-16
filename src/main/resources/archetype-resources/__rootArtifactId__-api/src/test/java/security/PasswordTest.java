package ${package}.security;

import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author VEDRAX SAS
 */
public class PasswordTest {

    private final static Logger LOGGER = Logger.getLogger(PasswordTest.class.getName());

    @Test
    public void test() {
        String passwordStr = "lagonisi1983";
        String salt = "b6XMvXlUClGuPZUfnJjlUnhLPtrmwU88towszvGHrvO416WfCz/PiyvN9joPn2F8Nq9E1F0X1Hz1f+/dEU2vMw==";
        String password = "oLF0pbQu9Y5b/Gm3+FdPCsb2OaohiqQJwv8dJ66fesEMRjP8uUawrD5LbDqq9OEJAcJIUBDCJ6USLxFqfhuiHg==";

        Password pwd = new Password();

        byte[] saltBytes = pwd.bytesFrombase64(salt);
        byte[] passwordBytes = pwd.hashWithSalt(passwordStr, saltBytes);

        String passwordTest = pwd.base64FromBytes(passwordBytes);
        LOGGER.info(password);
        LOGGER.info(passwordTest);
     
        Assert.assertEquals(password, passwordTest);
    }

}
