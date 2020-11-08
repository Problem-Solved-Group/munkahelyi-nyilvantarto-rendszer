package problemsolved.filingsystem.repositories;

import org.springframework.data.repository.CrudRepository;
import problemsolved.filingsystem.entities.WorkingTime;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingTimeRepository extends CrudRepository<WorkingTime, Object>{
    
}
