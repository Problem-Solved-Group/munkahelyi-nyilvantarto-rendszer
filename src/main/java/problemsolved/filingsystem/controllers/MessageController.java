package problemsolved.filingsystem.controllers;

import java.util.List;
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
import problemsolved.filingsystem.entities.Message;
import problemsolved.filingsystem.entities.User;
import problemsolved.filingsystem.repositories.MessageRepository;
import problemsolved.filingsystem.repositories.UserRepository;

@RestController
@RequestMapping("/messages")
public class MessageController {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("")
    public ResponseEntity<Message> insert(@RequestBody Message request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> sender = userRepository.findByUsername(auth.getName());
        if (sender.isPresent()) {
            
            return ResponseEntity.ok(messageRepository.save(request));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/sent")
    public ResponseEntity<List<Message>> getAllSent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getSentMessages());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/received")
    public ResponseEntity<List<Message>> getAllReceived() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getReceivedMessages());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<Message> oRequest = messageRepository.findById(id);
        if (oRequest.isPresent() && user.isPresent() && user.get().getSentMessages().contains(oRequest.get())) {
            messageRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
