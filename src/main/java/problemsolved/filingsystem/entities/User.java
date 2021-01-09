package problemsolved.filingsystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false)
    private String name;
    
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String email;
    
    public enum Role {
        ROLE_WORKER, ROLE_LEADER, ROLE_ADMIN
    }
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Site> sites;
    
    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Message> sentMessages;
    
    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Message> receivedMessages;
    
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<HolidayRequest> holidayRequests;
    
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Announcement> announcements;
    
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<WorkingTime> workingTimes;
}
