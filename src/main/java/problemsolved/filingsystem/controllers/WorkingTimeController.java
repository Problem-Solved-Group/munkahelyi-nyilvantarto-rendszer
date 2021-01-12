package problemsolved.filingsystem.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    @Secured({"ROLE_LEADER","ROLE_ADMIN"})
    @PostMapping("/getbyday")
    public ResponseEntity<Iterable<WorkingTime>> getByDay(@RequestBody String day) {
        JSONObject json = new JSONObject(day);
        LocalDate reqDay = LocalDate.parse(json.getString("day"));
        Optional<User> user = getUser();
        if (user.isPresent()) {
            return ResponseEntity.ok( StreamSupport.stream(workingTimeRepository.findAll().spliterator(), false).filter(wr -> wr.getStart().toLocalDate().isEqual(reqDay)).collect(Collectors.toList()) );
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Secured({"ROLE_LEADER","ROLE_ADMIN"})
    @PostMapping("/{id}/validate")
    public ResponseEntity<WorkingTime> evaluate(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<WorkingTime> oRequest = workingTimeRepository.findById(id);
        if(user.isPresent() && oRequest.isPresent() && !oRequest.get().getValidated()) {
            WorkingTime request = oRequest.get();
            request.setValidated(true);
            return ResponseEntity.ok(workingTimeRepository.save(request));
        }
        return ResponseEntity.notFound().build();
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
    
    @PutMapping("/{id}")
    public ResponseEntity<WorkingTime> put(@PathVariable Integer id,@RequestBody WorkingTime reqWt) {
        Optional<User> user = getUser();
        Optional<WorkingTime> oWorkingTime = workingTimeRepository.findById(id);
        if (user.isPresent() && oWorkingTime.isPresent()) {
            WorkingTime wt = oWorkingTime.get();
            wt.setStart(reqWt.getStart());
            wt.setEnd(reqWt.getEnd());
            return ResponseEntity.ok(workingTimeRepository.save(reqWt));
        }
        return ResponseEntity.status(403).build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<WorkingTime> delete(@PathVariable Integer id) {
        Optional<User> user = getUser();
        Optional<WorkingTime> oWorkingTime = workingTimeRepository.findById(id);
        if(user.isPresent()){
            if (oWorkingTime.isPresent() && !oWorkingTime.get().getValidated() && oWorkingTime.get().getUser().getUsername().equals(user.get().getUsername())) {
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
