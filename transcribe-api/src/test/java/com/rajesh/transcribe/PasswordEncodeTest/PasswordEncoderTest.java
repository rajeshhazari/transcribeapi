package transribeapi.PasswordEncodeTest;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class PasswordEncoderTest {
    
    @Test
    public void testBcryptPasswordEncoder(){
        // Create an encoder with strength 16
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode("admin321");
        System.out.println("result:: "+result);
        assertTrue(encoder.matches("admin321", result));
    }
}
