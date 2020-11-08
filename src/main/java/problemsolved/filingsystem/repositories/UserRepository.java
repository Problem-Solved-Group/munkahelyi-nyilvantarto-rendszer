package problemsolved.filingsystem.repositories;

import org.springframework.data.repository.CrudRepository;
import problemsolved.filingsystem.entities.User;

public interface UserRepository extends CrudRepository<User, Interger>{
    
}
