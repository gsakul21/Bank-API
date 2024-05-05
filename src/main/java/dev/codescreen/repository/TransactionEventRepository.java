package dev.codescreen.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.codescreen.domain.TransactionEvent;

public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {

    List<TransactionEvent> findByUserId(String userId);
}
