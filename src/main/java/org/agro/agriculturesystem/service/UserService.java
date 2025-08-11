package org.agro.agriculturesystem.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.agro.agriculturesystem.dto.UserDTO;
import org.agro.agriculturesystem.model.FarmGuide;
import org.agro.agriculturesystem.model.User;
import org.agro.agriculturesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.Optional;

//handle business logic.
@Service
public class UserService extends User {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserDTO userDTO) {
        try {

            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new RuntimeException("Email is already in use!");
            }

            String name = userDTO.getName();
            String email = userDTO.getEmail();
            String password = userDTO.getPassword();
//            String confirmPassword = userDTO.getConfirmPassword();

            if (!name.matches("^[A-Za-z]{2}[A-Za-z ]*$")) {
                throw new RuntimeException("Invalid name!");
            }

            if (!name.matches("^[A-Za-z]{2}[A-Za-z ]*$")) {
                throw new RuntimeException("Invalid name! Name must start with at least two letters, and contain only letters and spaces.");
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                throw new RuntimeException("Invalid email format!");
            }

            if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{6,}$") || password.contains(" ")) {
                throw new RuntimeException("Password must be at least 6 characters long, contain at least 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character, and no white spaces.");
            }

//            if (!password.equals(confirmPassword)) {
//                throw new RuntimeException("Password and Confirm Password do not match!");
//            }

            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hash password
            user.setRole(userDTO.getRole());
            user.setJoinedDate(LocalDate.now());

            User savedUser = userRepository.save(user);
            return savedUser;
        } catch (Exception e) {
            throw e;
        }
    }

    public User loginUser(String email, String password, HttpSession session) {
        try {

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                throw new RuntimeException("Invalid credentials!");
            }

            User user = userOptional.get();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid credentials!");
            }
            session.setAttribute("userId", user.getId());

            return user;
        } catch (Exception e) {
            throw e;
        }
    }

    public User loginAdmin(String email, String password, HttpSession session) throws AccessDeniedException {
        try {

            // 1. Find user by email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        return new UsernameNotFoundException("Invalid credentials!");
                    });

            //  FIRST CHECK ROLE BEFORE PASSWORD
            if (!"admin".equals(user.getRole())) {
                throw new AccessDeniedException("Administrator access only!");
            }

            // 2. Verify password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid credentials");
            }

            // 3. Set session attributes
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("role", user.getRole());
            session.setMaxInactiveInterval(30 * 60);

            return user;

        } catch (Exception e) {
            throw e;
        }
    }


    @Transactional
    public void updateUser(int id, UserDTO userDTO) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));

        String name = userDTO.getName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
//        String confirmPassword = userDTO.getConfirmPassword();

        if (!name.matches("^[A-Za-z]{2}[A-Za-z ]*$")) {
            throw new RuntimeException("Invalid name!");
        }

        if (!name.matches("^[A-Za-z]{2}[A-Za-z ]*$")) {
            throw new RuntimeException("Invalid name! Name must start with at least two letters, and contain only letters and spaces.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new RuntimeException("Invalid email format!");
        }

        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{6,}$") || password.contains(" ")) {
            throw new RuntimeException("Password must be at least 6 characters long, contain at least 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character, and no white spaces.");
        }

//        if (!password.equals(confirmPassword)) {
//            throw new RuntimeException("Password and Confirm Password do not match!");
//        }


        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setRole(userDTO.getRole());
        userRepository.save(user);
    }

//    @Transactional
//    public void deleteUserById(int id) throws Exception {
//        if (!userRepository.existsById(id)) {
//            throw new Exception("User not found");
//        }
//        userRepository.deleteById(id);
//    }

//    public void softDeleteUserById(int id) {
//        userRepository.softDeleteById(id);
//    }

    @Transactional
    public void forgotPassword(String email) {

    }

    @Transactional
    public void updatePassword(String oldPassword, String newPassword, int userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        } else if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{6,}$") || newPassword.contains(" ")) {
            throw new RuntimeException("Password must be at least 6 characters long, contain at least 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character, and no white spaces.");
        } else if (oldPassword.equals(newPassword)) {
            throw new RuntimeException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found with id: " + id));
    }

    public Page<User> getPaginatedUsersList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }

    public long getTotalRegularUsers() {
        return userRepository.countByRole("user");
    }

    public long getTotalAdmin() {
        return userRepository.countByRole("admin");
    }

}

    
