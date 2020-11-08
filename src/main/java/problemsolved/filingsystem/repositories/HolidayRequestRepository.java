package problemsolved.filingsystem.repositories;

import org.springframework.data.repository.CrudRepository;
import problemsolved.filingsystem.entities.HolidayRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRequestRepository extends CrudRepository<HolidayRequest, Object>{
    
}
