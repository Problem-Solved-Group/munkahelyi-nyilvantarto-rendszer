package problemsolved.filingsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import problemsolved.filingsystem.entities.HolidayRequest;
import problemsolved.filingsystem.entities.User;

/*Check main/resources/data-h2.sql for the initial datas!*/

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HolidayRequestControllerTest {
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
    public void shouldGetAllHolidayRequest() throws Exception{
        System.out.println("Holiday Requests -- Test 1");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<Iterable<HolidayRequest>> response = restTemplate.exchange(
                "http://localhost:" + port + "/holiday",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Iterable<HolidayRequest>>() {}
        );
        List<HolidayRequest> hrequests = StreamSupport.stream(response.getBody().spliterator(), true).collect(Collectors.toList());
        assertEquals(2,hrequests.size(), "Should give back all holiday requests!");
    }
    
    @Test
    @Order(2)
    public void shouldGetHolidayRequestById() throws Exception{
        System.out.println("Holiday Requests -- Test 2");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<HolidayRequest> response = restTemplate.exchange(
                "http://localhost:" + port + "/holiday/4",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<HolidayRequest>() {}
        );
        assertEquals(HttpStatus.OK,response.getStatusCode(),"Request should be successful!");
        assertEquals("2020-07-14",response.getBody().getRequestedDay().format(DateTimeFormatter.ISO_LOCAL_DATE), "Should give back ID 4's requested day!");
    }
    
    @Test
    @Order(3)
    public void userShouldNotSeeOthersHolidayRequest() throws Exception{
        System.out.println("Holiday Requests -- Test 3");
        HttpEntity requestEntity = getRequestEntityForUser("worker", "worker");
        ResponseEntity<HolidayRequest> response = restTemplate.exchange(
                "http://localhost:" + port + "/holiday/1",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<HolidayRequest>() {}
        );
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode(),"Request should be unsuccessful!");
        Assertions.assertNull(response.getBody());
    }
    
    @Test
    @Order(4)
    public void requestShouldBeInserted() throws Exception{
        System.out.println("Holiday Requests -- Test 4");
        /* LOG USER IN AND SETUP HTTP HEADER*/
        String token = getTokenForUser("leader", "leader");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        /* LOG USER IN AND SETUP HTTP HEADER*/
        
        HolidayRequest an = getTestHolidayRequestObject();
        String json = DEFAULT_OBJECT_WRITER.writeValueAsString(an);
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        
        ResponseEntity<Iterable<HolidayRequest>> getResponse = restTemplate.exchange(
                "http://localhost:" + port + "/holiday/",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Iterable<HolidayRequest>>() {}
        );
        
        Integer hrCount = StreamSupport.stream(getResponse.getBody().spliterator(), true).collect(Collectors.toList()).size();
        
        ResponseEntity<HolidayRequest> response = restTemplate.exchange(
                "http://localhost:" + port + "/holiday/",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<HolidayRequest>() {}
        );
        assertEquals(HttpStatus.OK,response.getStatusCode(),"Request should be successful!");
      
        ResponseEntity<Iterable<HolidayRequest>> getResponse2 = restTemplate.exchange(
                "http://localhost:" + port + "/holiday/",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Iterable<HolidayRequest>>() {}
        );
        
        Integer newHrCount = StreamSupport.stream(getResponse2.getBody().spliterator(), true).collect(Collectors.toList()).size();
        assertEquals(newHrCount, hrCount + 1);
        
    }
    
    
    @Test
    @Order(5)
    public void requestShouldBeDeleted() throws Exception{
        System.out.println("Holiday Requests -- Test 6");
        HttpEntity requestEntity = getRequestEntityForUser("admin", "admin");
        
        ResponseEntity<Iterable<HolidayRequest>> getAllResponse = restTemplate.exchange(
                "http://localhost:" + port + "/holiday",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Iterable<HolidayRequest>>() {}
        );
        
        Integer hrCount = StreamSupport.stream(getAllResponse.getBody().spliterator(), true).collect(Collectors.toList()).size();
        
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/holiday/1",
                HttpMethod.DELETE,
                requestEntity,
                new ParameterizedTypeReference<Void>() {}
        );
        
        assertEquals(HttpStatus.OK,response.getStatusCode(), "Delete of ID 1 holiday request should be successful!");
        
        ResponseEntity<Iterable<HolidayRequest>> getAllResponse2 = restTemplate.exchange(
                "http://localhost:" + port + "/holiday",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Iterable<HolidayRequest>>() {}
        );
        
        Integer newHrCount = StreamSupport.stream(getAllResponse2.getBody().spliterator(), true).collect(Collectors.toList()).size();
        
        assertEquals(newHrCount,hrCount-1,"Count of admin's holiday request should be 2,not "+ hrCount);
    }
    
    
    private static HolidayRequest getTestHolidayRequestObject(){
        HolidayRequest hr =  new HolidayRequest();
        hr.setRequestedDay(LocalDateTime.parse("2021-01-01T10:15:30" , DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return hr;
    }
    
}
