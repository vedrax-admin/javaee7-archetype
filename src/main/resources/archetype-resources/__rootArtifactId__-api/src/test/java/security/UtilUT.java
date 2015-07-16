package ${package}.security;


import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author VEDRAX SAS
 */
public class UtilUT {

    @Test
    public void testHashPassword() {
        String expectedPassword = "ZxRQG2h7AYaOe17iZa1pjLmpI7emTXfwdZo7V/LV05Y=";
        String hashPassword = SecurityUtil.createPasswordHash("SHA-256","lagonisi1983");
        Assert.assertNotNull("password should not be null", hashPassword);
        Assert.assertEquals("hashPassword sould be equal to " + expectedPassword, expectedPassword, hashPassword);
    }
 
}
