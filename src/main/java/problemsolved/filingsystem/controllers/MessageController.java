package problemsolved.filingsystem.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import problemsolved.filingsystem.entities.Message;
import problemsolved.filingsystem.entities.MessageRequest;
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
    public ResponseEntity<Message> insert(@RequestBody MessageRequest m) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<User>oReceiver = userRepository.findByUsername(m.getReceiver());
        if(user.isPresent() && oReceiver.isPresent()) {
            Message msg = new Message();
            msg.setTitle(m.getTitle());
            msg.setMessage(m.getMessage());
            msg.setSender(user.get());
            msg.setReceiver(oReceiver.get());
            return ResponseEntity.ok(messageRepository.save(msg));
        }
        return ResponseEntity.notFound().build();
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
    
    @PostMapping("/{id}/seen")
    public ResponseEntity<Void> setSeen(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<Message> message = messageRepository.findById(id);
        if (user.isPresent() && message.isPresent() && Objects.equals(user.get(), message.get().getReceiver()) ) {
            message.get().setSeen_at(LocalDateTime.now());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
}
