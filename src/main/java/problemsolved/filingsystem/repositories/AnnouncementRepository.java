package problemsolved.filingsystem.repositories;

import org.springframework.data.repository.CrudRepository;
import problemsolved.filingsystem.entities.Announcement;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends CrudRepository<Announcement,Integer> {
    
}
