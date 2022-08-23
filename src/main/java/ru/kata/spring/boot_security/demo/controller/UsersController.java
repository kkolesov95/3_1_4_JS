package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Users;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.stream.Collectors;

@Controller
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }


    @GetMapping("admin")
    public String index(Model model) {
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("currentUserRoles",
                getUserData().getRoles()
                        .stream()
                        .map(x -> x.getRole().replaceFirst("ROLE_", ""))
                        .collect(Collectors.toList()));
        model.addAttribute("users", userService.index());
        return "index";
    }

    @GetMapping("user")
    public String indexUser(Model model) {
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("currentUserRoles",
                getUserData().getRoles()
                        .stream()
                        .map(x -> x.getRole().replaceFirst("ROLE_", ""))
                        .collect(Collectors.toList()));
        model.addAttribute("currentUser", getUserData());
        return "user";
    }

    @GetMapping("/users/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "show";
    }

    @GetMapping("/admin/users/new")
    public String newUser(Users users) {
        return "new";
    }

    @PostMapping("/users")
    public String create(Users users) {
        userService.saveUser(users);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("users", userService.findById(id));
        return "edit";
    }

    @PostMapping("/users/{id}")
    public String update(Users users) {
        userService.saveUser(users);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    private Users getUserData() {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User)authentication.getPrincipal();
            Users myUser = userService.findByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }
}
