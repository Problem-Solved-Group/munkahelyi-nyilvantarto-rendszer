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
        List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            List<HolidayRequest> requests = new ArrayList<>();
            for(HolidayRequest hr: holidayRepository.findAll()) {
                if(hr.getUser().getUsername().equals(username)) requests.add(hr);
            }
            return ResponseEntity.ok(requests);
        }
        return ResponseEntity.notFound().build();
    }
}
