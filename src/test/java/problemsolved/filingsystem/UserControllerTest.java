package problemsolved.filingsystem;

import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import problemsolved.filingsystem.entities.User;
import problemsolved.filingsystem.repositories.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    
    static {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("test_password");
        user.setEmail("email@mail.com");
        user.setName("Name Name");
        TEST_USER = user;
    }
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    private final static User TEST_USER;
    @Test
    @Order(1)
    public void userShouldBeAbleToRegistrate() throws Exception{
        ResponseEntity<Void> responseAuth = restTemplate.postForEntity("/users/", TEST_USER, Void.class);
        assertEquals(HttpStatus.CREATED,responseAuth.getStatusCode(),"User should be created!");
    }
    
    @Test
    @Order(2)
    public void userShouldBeAbleToLogin() throws Exception{        
        ResponseEntity<String> responseAuth = restTemplate.postForEntity("/users/login", TEST_USER, String.class);
        
        assertEquals(HttpStatus.OK,responseAuth.getStatusCode(),"User should be logged in!");
        String jsonString = responseAuth.getBody();
        JSONObject json2 = new JSONObject(jsonString);
        json2.getString("token");
        assertFalse(json2.getString("token").isEmpty(),"Received token shouldn't be empty after successful login");
    }
}
