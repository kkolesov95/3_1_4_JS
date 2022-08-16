package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.entities.Users;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String index(Model model) {
        model.addAttribute("users", userService.index());
        return "index";
    }

    @GetMapping("/user/indexUser")
    public String indexUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User) authentication.getPrincipal();
            users = userService.findByEmail(secUser.getUsername());
        }
        model.addAttribute("user", users);
        return "indexUser";
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
}
