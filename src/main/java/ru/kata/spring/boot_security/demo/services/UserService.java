package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entities.Roles;
import ru.kata.spring.boot_security.demo.entities.Users;

import java.util.List;

public interface UserService extends UserDetailsService {
    Users findById(Long id);

    List<Users> index();

    List<Roles> getAllRoles();

    Users saveUser(Users users);

    void deleteById(Long id);

    Users findByEmail(String email);
}
