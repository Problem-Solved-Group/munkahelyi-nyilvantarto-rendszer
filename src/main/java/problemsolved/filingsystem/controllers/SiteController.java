package problemsolved.filingsystem.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import java.util.Optional;
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
import problemsolved.filingsystem.entities.Site;
import problemsolved.filingsystem.entities.User;
import problemsolved.filingsystem.repositories.SiteRepository;
import problemsolved.filingsystem.repositories.UserRepository;

@RestController
@RequestMapping("/site")
public class SiteController {
    @Autowired
    private SiteRepository siteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("")
    public ResponseEntity<Iterable<Site>> getAll() {
        Optional<User> user = getUser();
        if (user.isPresent()) {
            return ResponseEntity.ok(siteRepository.findAll());
        }
        return ResponseEntity.status(403).build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Site> getAll(@PathVariable Integer id) {
        Optional<User> user = getUser();
        if (user.isPresent()) {
            Optional<Site> oSite = siteRepository.findById(id);
            if(oSite.isPresent()){
                 return ResponseEntity.ok(oSite.get());
            }
            else{
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(403).build();
    }
    
    @Secured("ROLE_ADMIN")
    @PostMapping("")
    public ResponseEntity<Site> post(@RequestBody Site site) {
        Optional<User> user = getUser();
        if (user.isPresent()) {
            return ResponseEntity.ok(siteRepository.save(site));
        }
        return ResponseEntity.notFound().build();
    }
    
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Site> put(@PathVariable Integer id,@RequestBody Site site) {
        Optional<User> user = getUser();
        Optional<Site> oSite = siteRepository.findById(id);
        if (user.isPresent() && oSite.isPresent()) {
            oSite.get().setName(site.getName());
            oSite.get().setLocation(site.getLocation());
            return ResponseEntity.ok(siteRepository.save(oSite.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Site> delete(@PathVariable Integer id) {
        Optional<User> user = getUser();
        Optional<Site> oSite = siteRepository.findById(id);
        if (user.isPresent()&& oSite.isPresent()) {
            siteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @Secured("ROLE_ADMIN")
    @PostMapping("/{id}/add")
    public ResponseEntity<Site> post(@PathVariable Integer id , @RequestBody String json){
        System.out.println(json);
        JSONObject obj = new JSONObject(json);
        Optional<User> reqUser = userRepository.findByUsername(obj.getString("username"));
        Optional<Site> reqSite = siteRepository.findById(id);
        if(reqUser.isPresent() && reqSite.isPresent() && !reqUser.get().getSites().contains(reqSite.get())){
            System.out.println("User is: " + reqUser.get().getName() );
            reqUser.get().getSites().add(reqSite.get());
            userRepository.save(reqUser.get());
            return ResponseEntity.ok(reqSite.get());
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }
    
    
    private Optional<User> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }
}
