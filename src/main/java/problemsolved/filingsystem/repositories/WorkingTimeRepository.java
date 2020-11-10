package problemsolved.filingsystem.repositories;

import org.springframework.data.repository.CrudRepository;
import problemsolved.filingsystem.entities.WorkingTime;

public interface WorkingTimeRepository extends CrudRepository<WorkingTime, Integer>{
    
}
