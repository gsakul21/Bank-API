package dev.codescreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.codescreen.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}