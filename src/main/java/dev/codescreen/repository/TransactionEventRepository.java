package dev.codescreen.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.codescreen.domain.TransactionEvent;

/*
 * This is the interface created to allow for interaction with the SQLite 
 * database. The repository is built off an existing interface implementation
 * provided by Spring Boot, designed to work with JPA.
 * 
 * Stores the Event, an Entity object, into the database with its key being a
 * generated Long. For more details in regard to that, please view {@link TransactionEvent}.
 */

public interface TransactionEventRepository extends JpaRepository<TransactionEvent, String> {

    /**
     * @param userId
     * @return List<TransactionEvent>, events related to user with userId
     * 
     * The function takes in a specific userId, corresponding to a User within
     * the service. It returns back all stored load/authorization events that 
     * are tied to the specific user
     */
    List<TransactionEvent> findByUserId(String userId);
}
