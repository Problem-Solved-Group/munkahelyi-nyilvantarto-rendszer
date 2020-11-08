
package problemsolved.filingsystem.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String message;
    
    @ManyToOne
    private User sender;
    
    @ManyToOne
    private User reciever;
    
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;
}
