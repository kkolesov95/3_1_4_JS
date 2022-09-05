package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Roles;
import ru.kata.spring.boot_security.demo.entities.Users;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users myUser = userRepository.findByEmail(s);
        if (myUser!=null) {
            return new User(myUser.getEmail(), myUser.getPassword(), myUser.getRoles());
        }

        throw new UsernameNotFoundException("User Not Found");
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public Users findById(Long id){
        return userRepository.getById(id);
    }

    @Transactional
    @Override
    public List<Users> index() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void saveUser(Users users) {
        Users checkUser = userRepository.findByEmail(users.getEmail());
        if(checkUser==null) {
            Roles role = roleRepository.findByRole("ROLE_USER");
            if (role != null) {
                ArrayList<Roles> roles = new ArrayList<>();
                roles.add(role);
                users.setRoles(roles);
                users.setPassword(passwordEncoder.encode(users.getPassword()));
                userRepository.save(users);
            }
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    @Override
    public void update(Users users) {
        userRepository.save(users);
    }

    @Override
    public List<String> getRolesAsListString(Users users) {
        return users.getRoles().stream()
                .map(x -> x.getRole().replaceFirst("ROLE_", ""))
                .collect(Collectors.toList());
    }
}

