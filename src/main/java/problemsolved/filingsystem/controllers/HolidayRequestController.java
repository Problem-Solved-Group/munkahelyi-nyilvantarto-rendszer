package problemsolved.filingsystem.controllers;

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
    
    @PutMapping("/{id}")
    public ResponseEntity<HolidayRequest> update(@PathVariable Integer id, @RequestBody HolidayRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<HolidayRequest> oRequest = holidayRepository.findById(id);
        if (oRequest.isPresent() && user.isPresent() && user.get().getHolidayRequests().contains(oRequest.get())) {
            request.setId(id);
            return ResponseEntity.ok(holidayRepository.save(request));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HolidayRequest> delete(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<HolidayRequest> oRequest = holidayRepository.findById(id);
        if (oRequest.isPresent() && user.isPresent() && user.get().getHolidayRequests().contains(oRequest.get())) {
            holidayRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
