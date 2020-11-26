package problemsolved.filingsystem.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import problemsolved.filingsystem.entities.WorkingTime;
import problemsolved.filingsystem.entities.User;
import problemsolved.filingsystem.repositories.UserRepository;
import problemsolved.filingsystem.repositories.WorkingTimeRepository;

@RestController
@RequestMapping("/wt")
public class WorkingTimeController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkingTimeRepository workingTimeRepository;
    
    @GetMapping("")
    public ResponseEntity<List<WorkingTime>> getAll() {
        Optional<User> user = getUser();
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getWorkingTimes());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WorkingTime> get(@PathVariable Integer id) {
        Optional<User> user = getUser();
        if (user.isPresent()) {
            Optional<WorkingTime> workingTime = user.get().getWorkingTimes().stream().filter(wt -> wt.getId() == id).findFirst();
            if(workingTime.isPresent()){
                return ResponseEntity.ok(workingTime.get());
            }
            else{
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(403).build();
    }
    
    @PostMapping("")
    public ResponseEntity<WorkingTime> post(@RequestBody WorkingTime wt) {
        Optional<User> user = getUser();
        if (user.isPresent()) {
            wt.setUser(user.get());
            wt.setValidated(false);
            return ResponseEntity.ok(workingTimeRepository.save(wt));
        }
        return ResponseEntity.status(403).build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<WorkingTime> delete(@PathVariable Integer id) {
        Optional<User> user = getUser();
        Optional<WorkingTime> oWorkingTime = workingTimeRepository.findById(id);
        if(user.isPresent()){
            if (oWorkingTime.isPresent() && oWorkingTime.get().getUser().getUsername().equals(user.get().getUsername())) {
                workingTimeRepository.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(403).build();    
    }
    
    private Optional<User> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }
}
