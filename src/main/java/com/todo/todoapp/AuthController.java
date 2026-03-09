package com.todo.todoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    record AuthRequest(String username, String password) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        if (req.username() == null || req.username().isBlank()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        }
        if (req.password() == null || req.password().length() < 4) {
            return ResponseEntity.badRequest().body("Password must be at least 4 characters");
        }
        if (userRepository.findByUsername(req.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken!");
        }
        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        userRepository.save(user);
        return ResponseEntity.ok("Account created! Please login 🎉");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        Optional<User> found = userRepository.findByUsername(req.username());
        if (found.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found!");
        }
        if (!passwordEncoder.matches(req.password(), found.get().getPassword())) {
            return ResponseEntity.badRequest().body("Wrong password!");
        }
        String token = jwtUtil.generateToken(req.username());
        return ResponseEntity.ok(Map.of("token", token));
    }
}