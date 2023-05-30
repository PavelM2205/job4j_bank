package ru.job4j.job4j.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.job4j.job4j.bank.model.User;
import ru.job4j.job4j.bank.service.BankService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final BankService bankService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public User save(@RequestBody Map<String, String> body) {
        String password = body.get("password");
        String username = body.get("username");
        if (username.isEmpty() || password.isEmpty()) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. Password length must be more than 5 characters");
        }
        User user = new User(password, username);
        bankService.addUser(user);
        return user;
    }

    @GetMapping
    public User findByPassport(@RequestParam String password) {
        return bankService.findByPassport(password).orElse(null);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception exc, HttpServletRequest req,
                                 HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {{
            put("message", exc.getMessage());
            put("type", exc.getClass());
        }}));
        LOG.error(exc.getLocalizedMessage());
    }
}
