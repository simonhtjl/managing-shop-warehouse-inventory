package org.simonhtjl.service;

import jakarta.annotation.PostConstruct;
import org.simonhtjl.dao.entity.User;
import org.simonhtjl.dao.repository.UserRepository;
import org.simonhtjl.dto.AuthResponse;
import org.simonhtjl.dto.LoginRequest;
import org.simonhtjl.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ADMIN");
            admin.setFullName("System Administrator");
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("=== ADMIN USER CREATED ===");
            System.out.println("Username: admin");
            System.out.println("Password: admin");
            System.out.println("==========================");
        }
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return new AuthResponse(token, user.getUsername(), user.getRole(), user.getFullName());
    }

    public AuthResponse validateToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                return new AuthResponse(token, user.getUsername(), user.getRole(), user.getFullName());
            }
        }
        throw new RuntimeException("Invalid token");
    }
}
