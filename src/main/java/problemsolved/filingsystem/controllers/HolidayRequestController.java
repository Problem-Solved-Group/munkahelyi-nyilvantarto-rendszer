package problemsolved.filingsystem.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;
import problemsolved.filingsystem.repositories.HolidayRequestRepository;
import problemsolved.filingsystem.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import problemsolved.filingsystem.entities.HolidayRequest;
import problemsolved.filingsystem.entities.User;
@RestController
@RequestMapping("/holiday")
public class HolidayRequestController {
    
    @Autowired
    private HolidayRequestRepository holidayRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    
    @GetMapping("")
    public ResponseEntity<List<HolidayRequest>> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getHolidayRequests());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HolidayRequest> get(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<HolidayRequest> oRequest = holidayRepository.findById(id);
        if (oRequest.isPresent() && user.isPresent() && user.get().getHolidayRequests().contains(oRequest.get())) {
            return ResponseEntity.ok(oRequest.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Secured({"ROLE_AD","ROLE_ADMIN"})
    @PostMapping("/getbyday")
    public ResponseEntity<Iterable<HolidayRequest>> getByDay(@RequestBody String day) {
        LocalDate reqDay = LocalDate.parse(day);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> oUser = userRepository.findByUsername(auth.getName());
        if (oUser.isPresent()) {
            return ResponseEntity.ok(StreamSupport.stream(holidayRepository.findAll().spliterator(), false).filter(hd -> hd.getRequestedDay().toLocalDate().isEqual(reqDay)).collect(Collectors.toList()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("")
    public ResponseEntity<HolidayRequest> insert(@RequestBody HolidayRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        if(user.isPresent()) {
            request.setStatus(HolidayRequest.Status.UNSEEN);
            request.setUser(user.get());
            return ResponseEntity.ok(holidayRepository.save(request));
        }
        return ResponseEntity.notFound().build();
    }
    
    @Secured({"ROLE_AD","ROLE_ADMIN"})
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<HolidayRequest> evaluate(@PathVariable Integer id,@RequestBody Boolean decision) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<HolidayRequest> oRequest = holidayRepository.findById(id);
        if(user.isPresent() && oRequest.isPresent() && oRequest.get().getStatus() == HolidayRequest.Status.UNSEEN) {
            HolidayRequest request = oRequest.get();
            request.setStatus(decision ? HolidayRequest.Status.PERMITTED : HolidayRequest.Status.REJECTED);
            return ResponseEntity.ok(holidayRepository.save(request));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<HolidayRequest> oRequest = holidayRepository.findById(id);
        if (oRequest.isPresent() && user.isPresent() && 
                user.get().getHolidayRequests().contains(oRequest.get()) && 
                oRequest.get().getStatus() == HolidayRequest.Status.UNSEEN)  {
            holidayRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
