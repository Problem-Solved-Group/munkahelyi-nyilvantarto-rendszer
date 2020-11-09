package problemsolved.filingsystem.repositories;


import org.springframework.data.repository.CrudRepository;
import problemsolved.filingsystem.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
