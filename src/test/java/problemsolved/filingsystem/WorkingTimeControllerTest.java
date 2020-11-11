package problemsolved.filingsystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import problemsolved.filingsystem.entities.Site;
import problemsolved.filingsystem.entities.User;
import problemsolved.filingsystem.entities.WorkingTime;
import problemsolved.filingsystem.repositories.WorkingTimeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WorkingTimeControllerTest {
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private WorkingTimeRepository workingTimeRepository;
    
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
    public void userShouldGetAllWorkingTime() throws Exception{
         System.out.println("Working time -- Test 1");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<Iterable<WorkingTime>> response = restTemplate.exchange(
                "http://localhost:" + port + "/wt",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Iterable<WorkingTime>>() {}
        );
        
        assertEquals(HttpStatus.OK,response.getStatusCode(),"Request should be successful!");
        assertEquals(2,StreamSupport.stream(response.getBody().spliterator(), false).collect(Collectors.toList()).size(),"Request should give back all working time.");
    }
    
    @Test
    @Order(2)
    public void shouldReturnWorkingTimeById() throws Exception{
        System.out.println("Working time -- Test 2");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<WorkingTime> response = restTemplate.exchange(
                "http://localhost:" + port + "/wt/1",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<WorkingTime>() {}
        );
        WorkingTime wt = response.getBody();
        assertEquals(HttpStatus.OK,response.getStatusCode(),"Request should be successful!");
        assertEquals(1,wt.getId(), "Repsonse working time's ID should be 1");
        assertEquals(LocalDateTime.parse("2020-07-13T08:00:00",DateTimeFormatter.ISO_LOCAL_DATE_TIME),wt.getStart(),"Request should give back Id 1 working time!");
    }
    
    @Test
    @Order(3)
    public void userShouldBeAbleToUploadWorkingTime() throws Exception{
        System.out.println("Working time -- Test 3");
        
        String token = getTokenForUser("admin", "admin");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        
        WorkingTime wt = new WorkingTime();
        wt.setStart(LocalDateTime.parse("2021-01-01T10:15:30" , DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        wt.setEnd(LocalDateTime.parse("2021-01-01T18:15:30" , DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        Integer initSize = StreamSupport.stream(workingTimeRepository.findAll().spliterator(), true).collect(Collectors.toList()).size();
        
        String json = DEFAULT_OBJECT_WRITER.writeValueAsString(wt);
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        
        ResponseEntity<WorkingTime> response = restTemplate.exchange(
                "http://localhost:" + port + "/wt",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<WorkingTime>() {}
        );
        
        Integer updatedSize = StreamSupport.stream(workingTimeRepository.findAll().spliterator(), true).collect(Collectors.toList()).size();
        
        assertEquals(HttpStatus.OK,response.getStatusCode(),"Request should be successful!");
        assertEquals(initSize + 1,updatedSize);
    }
}
