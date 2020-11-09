/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemsolved.filingsystem.repositories;

import org.springframework.data.repository.CrudRepository;
import problemsolved.filingsystem.entities.Message;
/**
 *
 * @author domin
 */
public interface MessageRepository extends CrudRepository<Message, Integer>{
    
}
