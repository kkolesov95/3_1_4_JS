package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Users;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
}
