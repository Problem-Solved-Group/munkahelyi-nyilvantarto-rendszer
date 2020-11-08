package problemsolved.filingsystem.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import problemsolved.filingsystem.entities.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Object>{
    
}
