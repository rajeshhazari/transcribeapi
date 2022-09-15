package transribeapi.jdbc.test;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

import static org.apache.logging.log4j.LogManager.getLogger;

@SpringBootTest
@Profile("dev-test")
public class SpringJdbcPostgresTest {
    private static final Logger logger = getLogger(SpringJdbcPostgresTest.class);
    
    @Autowired
    DataSource dataSource;
    
    @Test
    public void testConnectivity(){
        Assertions.assertNotNull(dataSource);
    }
}
