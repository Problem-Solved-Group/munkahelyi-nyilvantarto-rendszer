package problemsolved.filingsystem.controllers;

import java.util.List;
import java.util.Optional;
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
import problemsolved.filingsystem.repositories.AnnouncementRepository;
import problemsolved.filingsystem.repositories.UserRepository;
import problemsolved.filingsystem.entities.Announcement;
import problemsolved.filingsystem.entities.HolidayRequest;
import problemsolved.filingsystem.entities.User;


@RestController
@RequestMapping("")
public class AnnouncementController {
    
    @Autowired
    private AnnouncementRepository announcementRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("")
    public ResponseEntity<Iterable<Announcement>> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        if (user.isPresent()) {
            return ResponseEntity.ok(announcementRepository.findAll());
            
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/announcements/{id}")
    public ResponseEntity<Announcement> get(@PathVariable Integer id) {
        Optional<Announcement> oAnn = announcementRepository.findById(id);
        if (oAnn.isPresent()) {
            return ResponseEntity.ok(oAnn.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Secured({"LEADER", "ADMIN"})
    @PostMapping("/announcements")
    public ResponseEntity<Announcement> insert(@RequestBody Announcement request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        request.setUser(user.get());
        return ResponseEntity.ok(announcementRepository.save(request));
    }
    @Secured({"LEADER","ADMIN"})
    @PutMapping("/announcements/{id}")
    public ResponseEntity<Announcement> update(@PathVariable Integer id, @RequestBody Announcement request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<Announcement> oRequest = announcementRepository.findById(id);
        if (oRequest.isPresent() && user.isPresent() && user.get().getAnnouncements().contains(oRequest.get())) {
            request.setId(id);
            return ResponseEntity.ok(announcementRepository.save(request));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Secured({"LEADER", "ADMIN"})
    @DeleteMapping("/announcemens/{id}")
    public ResponseEntity<HolidayRequest> delete(@PathVariable Integer id) {
        Optional<Announcement> oAnn = announcementRepository.findById(id);
        if (oAnn.isPresent()) {
            announcementRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}