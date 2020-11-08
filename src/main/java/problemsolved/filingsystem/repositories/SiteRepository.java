package problemsolved.filingsystem.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import problemsolved.filingsystem.entities.Site;

@Repository
public interface SiteRepository extends CrudRepository<Site, Integer>{
    
}
