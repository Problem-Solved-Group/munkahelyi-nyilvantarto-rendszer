package problemsolved.filingsystem.repositories;

import problemsolved.filingsystem.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    
}
