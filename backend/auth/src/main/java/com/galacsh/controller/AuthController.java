package com.galacsh.controller;

import com.galacsh.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    private final UserService userService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView signup(String username, String email, String password) {
        userService.createUser(username, email, password);

        ModelAndView modelAndView = new ModelAndView("alert-redirect");
        modelAndView.addObject("title", "Sign Up");
        modelAndView.addObject("message", "You have successfully signed up!");
        modelAndView.addObject("url", "/login");
        return modelAndView;
    }

    @GetMapping("/delete-user")
    public String deleteUser() {
        return "delete-user";
    }

    @PostMapping("/delete-user")
    public ModelAndView deleteUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(authentication.getName());
        logoutHandler.logout(request, response, authentication);

        ModelAndView modelAndView = new ModelAndView("alert-redirect");
        modelAndView.addObject("title", "Delete User");
        modelAndView.addObject("message", "You have successfully deleted your account!");
        modelAndView.addObject("url", "/login");
        return modelAndView;
    }
}
