package problemsolved.filingsystem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import problemsolved.filingsystem.entities.Announcement;
import problemsolved.filingsystem.entities.User;
import problemsolved.filingsystem.repositories.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AnnouncementControllerTest {
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
    public void shouldReturnAllAnnouncements() throws Exception {
        System.out.println("Announcement -- Test 1");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<Iterable<Announcement>> response = restTemplate.exchange(
                "http://localhost:" + port + "/",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Iterable<Announcement>>() {}
        );
        List<Announcement> announcements = StreamSupport.stream(response.getBody().spliterator(), true).collect(Collectors.toList());
        assertEquals(3,announcements.size(), "Should give back all announcements!");
    }
    
    @Test
    @Order(2)
    public void shouldReturnAnnouncementById() throws Exception {
        System.out.println("Announcement -- Test 2");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<Announcement> response = restTemplate.exchange(
                "http://localhost:" + port + "/announcements/1",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Announcement>() {}
        );
        Announcement announcement = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals( "Home office", announcement.getMessage(), () -> "Value mismatch.Expected: \"Home office\" , found: " + announcement.getMessage());
    }
    
    @Test
    @Order(3)
    public void shouldNotInsertNewAnnouncement() throws Exception {
        System.out.println("Announcement -- Test 3");
        
        /* LOG USER IN AND SETUP HTTP HEADER*/
        String token = getTokenForUser("worker", "worker");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        /* LOG USER IN AND SETUP HTTP HEADER*/
        
        Announcement an = getTestAnnouncementObject();
        String json = DEFAULT_OBJECT_WRITER.writeValueAsString(an);
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        
        ResponseEntity<Announcement> response = restTemplate.exchange(
                "http://localhost:" + port + "/announcements",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Announcement>() {}
        );
        
        assertEquals(403,response.getStatusCodeValue(),"Insertion of new announcement should be forbidden for unauthorized users!");
    }
    
    @Test
    @Order(4)
    public void shouldInsertNewAnnouncement() throws Exception {
        System.out.println("Announcement -- Test 4");

        String token = getTokenForUser("admin", "admin");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        
        Announcement an = getTestAnnouncementObject();
        String json = DEFAULT_OBJECT_WRITER.writeValueAsString(an);
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        
        ResponseEntity<Announcement> response = restTemplate.exchange(
                "http://localhost:" + port + "/announcements",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Announcement>() {}
        );
        
        Announcement announcement = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Admins should be able to create new announcement!");
        assertEquals("Admin felhivas" , announcement.getTitle());
    }
    
    @Test
    @Order(5)
    public void shouldUpdateAnnouncement() throws Exception{
        System.out.println("Announcement -- Test 5");
        String token = getTokenForUser("admin", "admin");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        
        Announcement an =  getTestAnnouncementObject();      
        String json = DEFAULT_OBJECT_WRITER.writeValueAsString(an);
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        
        ResponseEntity<Announcement> response = restTemplate.exchange(
                "http://localhost:" + port + "/announcements/1",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<Announcement>() {}
        );
        Announcement announcement = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Creator of the announcement should be able to overwrite it!");
        assertEquals("Admin felhivas" , announcement.getTitle() , "The updated announcement contains the old values!");
    }
    
    @Test
    @Order(6)
    public void shouldDeleteAnnouncement() throws Exception{
        System.out.println("Announcement -- Test 6");
        HttpEntity requestEntity = getRequestEntityForUser("admin", "admin");
        
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/announcements/1",
                HttpMethod.DELETE,
                requestEntity,
                new ParameterizedTypeReference<Void>() {}
        );
        
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode(), "Creator of the announcement should be able to delete it!");
    }
    
    private static Announcement getTestAnnouncementObject(){
        Announcement an =  new Announcement();
        an.setTitle("Admin felhivas");
        an.setMessage("Szia");
        an.setShowUntil(LocalDateTime.parse("2021-01-01T10:15:30" , DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return an;
    }
}
