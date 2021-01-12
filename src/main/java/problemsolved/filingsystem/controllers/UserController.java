package problemsolved.filingsystem.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import problemsolved.filingsystem.repositories.UserRepository;
import problemsolved.filingsystem.entities.User;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/whoami")
    public ResponseEntity<User> whoami() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @GetMapping("/allusers")
    public ResponseEntity<Iterable<User>> allusers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return ResponseEntity.ok(userRepository.findAll());
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @PostMapping("")
    public ResponseEntity<Void> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.ROLE_WORKER);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}/update")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> oUser = userRepository.findById(id);
        if(oUser.isPresent()){
            User us = oUser.get();
            us.setName(user.getName());
            us.setEmail(user.getEmail());
            us.setRole(user.getRole());
            us.setSites(user.getSites());
            userRepository.save(us);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    
}
