package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Users;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.stream.Collectors;

@Controller
public class UsersController {

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsersController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }


    @GetMapping("admin")
    public String index(Model model) {
        Users users = new Users();
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("users", userService.index());
        model.addAttribute("currentUserRoles",
                userService.getRolesAsListString(getUserData()));
        model.addAttribute("allRoles", userService.getAllRoles());
        model.addAttribute("newUser", users);
        return "index";
    }

    @GetMapping("user")
    public String indexUser(Model model) {
        model.addAttribute("currentUser", getUserData());
        model.addAttribute("currentUserRoles",
                userService.getRolesAsListString(getUserData()));
        model.addAttribute("currentUser", getUserData());
        return "user";
    }

    @GetMapping("/admin/getuser")
    @ResponseBody
    public Users getUserById(Long id) {
        return userService.findById(id);
    }


    @PostMapping( "/admin/add_user")
    public String saveUser(@ModelAttribute Users users) {
        userService.saveUser(users);
        return "redirect:/admin";
    }

    @PostMapping("/admin/save_user")
    public String updateUser(@ModelAttribute Users users) {
        String pass = passwordEncoder.encode(users.getPassword());
        users.setPassword(pass);
        userService.update(users);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete_user")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    private Users getUserData() {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User)authentication.getPrincipal();
            return userService.findByEmail(secUser.getUsername());
        }
        return null;
    }
}
