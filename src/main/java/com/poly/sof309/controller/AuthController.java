package com.poly.sof309.controller;

import com.poly.sof309.exception.DuplicateUserException;
import com.poly.sof309.exception.ValidationException;
import com.poly.sof309.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String root() { return "redirect:/login"; }

    @GetMapping("/login")
    public String loginForm() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam(defaultValue = "") String username,
                        @RequestParam(defaultValue = "") String password,
                        HttpSession session, Model model) {
        if (password.isEmpty()) {
            model.addAttribute("error", "Mật khẩu không được để trống");
            return "login";
        }
        if (username.isBlank()) {
            model.addAttribute("error", "Username không được để trống");
            return "login";
        }
        if (!authService.login(username, password)) {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
            return "login";
        }
        session.setAttribute("user", username);
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String registerForm() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String password,
                           @RequestParam(defaultValue = "") String confirmPassword,
                           Model model) {
        try {
            authService.register(username, email, password, confirmPassword);
        } catch (ValidationException e) {
            model.addAttribute("errors", e.getErrors());
            return "register";
        } catch (DuplicateUserException e) {
            model.addAttribute("errors", java.util.List.of(e.getMessage()));
            return "register";
        }
        model.addAttribute("success", "Đăng ký thành công! Hãy đăng nhập.");
        return "login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
