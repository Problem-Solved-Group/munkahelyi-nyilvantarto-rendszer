/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolved.filingsystem.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import problemsolved.filingsystem.entities.HolidayRequest;
import problemsolved.filingsystem.entities.User;
/**
 *
 * @author domin
 */
public interface HolidayRequestRepository extends CrudRepository<HolidayRequest, Integer>{
    Optional<HolidayRequest> findBy(String username);
}
