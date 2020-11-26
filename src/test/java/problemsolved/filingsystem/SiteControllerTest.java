package problemsolved.filingsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import problemsolved.filingsystem.entities.Site;
import problemsolved.filingsystem.entities.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import problemsolved.filingsystem.repositories.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SiteControllerTest {
    
    static {
        Site site = new Site();
        site.setName("HQ 2");
        site.setLocation("6000,Hatezer Hatos utca 6.");
        TEST_SITE = site;
    }
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    private final static Site TEST_SITE;
    
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
    public void shouldGetAllSite() throws Exception{
        System.out.println("Site -- Test 1");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<Iterable<Site>> response = restTemplate.exchange(
                "http://localhost:" + port + "/site",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Iterable<Site>>() {}
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode() , "Request should be successful");
        assertEquals(4, StreamSupport.stream(response.getBody().spliterator(), false).collect(Collectors.toList()).size(),"Request should give back all site.");
    }
    
    @Test
    @Order(2)
    public void shouldGiveBackSiteById() throws Exception{
        System.out.println("Site -- Test 2");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<Site> response = restTemplate.exchange(
                "http://localhost:" + port + "/site/1",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Site>() {}
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode() , "Request should be successful");
        assertEquals("HQ", response.getBody().getName(),"Request should give back the right site");
    }
    
    @Test
    @Order(3)
    public void adminShouldBeAbleToAddNewSite() throws Exception{
        System.out.println("Site -- Test 3");
        
        String token = getTokenForUser("admin", "admin");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        
        String json = DEFAULT_OBJECT_WRITER.writeValueAsString(TEST_SITE);
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        
        ResponseEntity<Site> response = restTemplate.exchange(
                "http://localhost:" + port + "/site/",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Site>() {}
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode() , "POST request should be successful");
        assertEquals("HQ 2", response.getBody().getName(),"Site's name in the request and the response should be the same!");
    }
    
    @Test
    @Order(4)
    public void workerShouldNotBeAbleToAddNewSite() throws Exception{
        System.out.println("Site -- Test 4");
        
        String token = getTokenForUser("worker", "worker");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        
        String json = DEFAULT_OBJECT_WRITER.writeValueAsString(TEST_SITE);
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        
        ResponseEntity<Site> response = restTemplate.exchange(
                "http://localhost:" + port + "/site/",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Site>() {}
        );
        
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode() , "Post request should be forbidden for unatuhorized user (worker)!");
    }  
    
    @Test
    @Order(5)
    public void adminShouldBeAbleToAddUserToSite() throws Exception{
        System.out.println("Site -- Test 5");
        
        String token = getTokenForUser("admin", "admin");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        
        HttpEntity<String> requestEntity = new HttpEntity<String>("{\"username\":\"leader\"}", headers);
        
        ResponseEntity<Site> response = restTemplate.exchange(
                "http://localhost:" + port + "/site/1/add",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Site>() {}
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Request should be successful!");
        Integer leaderSites = userRepository.findById(3).get().getSites().size();
        
        assertEquals(2, leaderSites, () -> "User 'leader' should be assigned to 2 sites instead of " + leaderSites);
    }
}
