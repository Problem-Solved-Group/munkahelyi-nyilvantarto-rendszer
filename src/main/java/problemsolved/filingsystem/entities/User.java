package problemsolved.filingsystem.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String email;
    
    public enum Role {
        WORKER, LEADER, ADMIN
    }
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;
    
    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;
    
    @OneToMany(mappedBy = "user")
    private List<HolidayRequest> holidayRequests;
    
    @OneToMany(mappedBy = "user")
    private List<Announcement> announcements;
}
