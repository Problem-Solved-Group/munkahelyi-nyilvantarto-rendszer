package problemsolved.filingsystem.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    public enum Role {
        WORKER,LEADER,ADMIN
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    @Colum(nullable = false)
    @Enumerated(EnumType.String)
    private Role role;
    
    
}
