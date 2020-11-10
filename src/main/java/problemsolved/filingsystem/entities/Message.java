
package problemsolved.filingsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    
    @JsonIgnore
    @ManyToOne
    private User sender;
    
    @JsonIgnore
    @ManyToOne
    private User receiver;
    
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;
    
    @Column
    private LocalDateTime seen_at;
}
