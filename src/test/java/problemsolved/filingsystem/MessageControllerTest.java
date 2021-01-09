package problemsolved.filingsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.json.JSONObject;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;
import problemsolved.filingsystem.entities.Message;
import problemsolved.filingsystem.entities.MessageRequest;
import problemsolved.filingsystem.entities.User;
import problemsolved.filingsystem.repositories.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessageControllerTest {
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private final static ObjectWriter DEFAULT_OBJECT_WRITER = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writer()
                .withDefaultPrettyPrinter();
    
    private String getTokenForUser(String username, String password) throws Exception {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        
        ResponseEntity<String> responseAuth = restTemplate.postForEntity("/users/login", user, String.class);
        String jsonString = responseAuth.getBody();
        JSONObject json2 = new JSONObject(jsonString);
        return json2.getString("token");
    }
    
    private HttpEntity getRequestEntityForUser(String username, String password) throws Exception {
        String token = getTokenForUser(username, password);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return new HttpEntity(null, headers);
    }
    
    
    @Test
    @Order(1)
    public void userShouldGetSentMessages() throws Exception {
        System.out.println("Messages -- Test 1");
        HttpEntity adminRequestEntity = getRequestEntityForUser("admin", "admin");
        HttpEntity workerRequestEntity = getRequestEntityForUser("worker", "worker");
        
        ResponseEntity<List<Message>> adminResponse = restTemplate.exchange(
                "http://localhost:" + port + "/messages/sent",
                HttpMethod.GET,
                adminRequestEntity,
                new ParameterizedTypeReference<List<Message>>() {}
        );
        
        ResponseEntity<List<Message>> workerResponse = restTemplate.exchange(
                "http://localhost:" + port + "/messages/sent",
                HttpMethod.GET,
                workerRequestEntity,
                new ParameterizedTypeReference<List<Message>>() {}
        );
        assertFalse(adminResponse.getBody().isEmpty());
        assertTrue(workerResponse.getBody().isEmpty());
    }
    
    @Test
    @Order(2)
    public void userShouldGetReceivedMessages() throws Exception {
        System.out.println("Messages -- Test 2");
        HttpEntity adminRequestEntity = getRequestEntityForUser("admin", "admin");
        HttpEntity workerRequestEntity = getRequestEntityForUser("worker", "worker");
        
        ResponseEntity<List<Message>> adminResponse = restTemplate.exchange(
                "http://localhost:" + port + "/messages/received",
                HttpMethod.GET,
                adminRequestEntity,
                new ParameterizedTypeReference<List<Message>>() {}
        );
        
        ResponseEntity<List<Message>> workerResponse = restTemplate.exchange(
                "http://localhost:" + port + "/messages/received",
                HttpMethod.GET,
                workerRequestEntity,
                new ParameterizedTypeReference<List<Message>>() {}
        );
        assertFalse(adminResponse.getBody().isEmpty());
        assertTrue(workerResponse.getBody().isEmpty());
    }
   
    
    @Test
    @Order(3)
    public void userShouldBeAbleToSendMessage() throws Exception{
        System.out.println("Messages -- Test 4");
        /* LOG USER IN AND SETUP HTTP HEADER*/
        String token = getTokenForUser("admin", "admin");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        /* LOG USER IN AND SETUP HTTP HEADER*/
        MessageRequest message = new MessageRequest();
        message.setTitle("Title");
        message.setMessage("Message");
        message.setReceiver("worker");
        
        String json = DEFAULT_OBJECT_WRITER.writeValueAsString(message);
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        
        ResponseEntity<Message> response = restTemplate.exchange(
                "http://localhost:" + port + "/messages/",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Message>() {}
        );
        
        Message resMessage = response.getBody();
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Title",resMessage.getTitle(),"Message's title in the request and the response should be the same!");
    }
}
