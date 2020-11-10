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
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JsonIgnore
    private User user;
    
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;
    
    @Column
    @UpdateTimestamp
    private LocalDateTime updated_at;
    
    @Column(nullable = false)
    private LocalDateTime start;
    
    @Column(nullable = false)
    private LocalDateTime end;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean validated;
}
