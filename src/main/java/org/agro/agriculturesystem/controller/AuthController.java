package org.agro.agriculturesystem.controller;

import jakarta.servlet.http.HttpSession;
import org.agro.agriculturesystem.dto.UserDTO;
import org.agro.agriculturesystem.model.FarmGuide;
import org.agro.agriculturesystem.model.User;
import org.agro.agriculturesystem.repository.UserRepository;
import org.agro.agriculturesystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {
//    to expose a registration API.

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return "login";
        } catch (Exception e) {
            return "redirect:/register?error=" + e.getMessage();
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                return "redirect:/login?error=Email and password are required!";
            }

            userService.loginUser(email, password, session);
            return "index";
        } catch (Exception e) {
            return "redirect:/login?error=" + e.getMessage();
        }
    }

    @PostMapping("/adminLogin")
    public String loginAdmin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {
            userService.loginAdmin(email, password, session);
            return "redirect:/adminDashboard";
        } catch (Exception e) {
            return "redirect:/adminLogin?error=" + e.getMessage();
        }
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, HttpSession session,
    @RequestParam(defaultValue ="1") int page, @RequestParam(defaultValue = "7") int size) {
        if (session.getAttribute("userId") == null || session.getAttribute("role") == null) {
            return "admin/login";
        }

        long totalUsers = userService.getTotalRegularUsers();

        long totalAdmins = userService.getTotalAdmin();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalAdmins", totalAdmins);


        Page<User> usr = userService.getPaginatedUsersList(page, size);
        model.addAttribute("userList", usr.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", usr.getTotalPages());
        model.addAttribute("totalItems", usr.getTotalElements());

        return "admin/users";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@ModelAttribute("user") UserDTO userDTO, HttpSession session, Model model) {
        try {
            // Check if admin is logged in
            if (session.getAttribute("userId") == null || !"admin".equals(session.getAttribute("role"))) {
                return "redirect:/adminLogin?error=Unauthorized access";
            }

            if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty() ||
                    userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty() ||
                    userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
                model.addAttribute("error-message", "All fields are required");
                return "redirect:/users";
            }

            // Register the new user
            userService.registerUser(userDTO);
            return "redirect:/users";
        } catch (Exception e) {
            return "redirect:/users?error=" + e.getMessage();
        }
    }

    @GetMapping("/getUser/{id}")
    @ResponseBody
    public User editUser(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable int id, @ModelAttribute("user") UserDTO userDTO, HttpSession session, Model model) {
        try {
            if (session.getAttribute("userId") == null || !"admin".equals(session.getAttribute("role"))) {
                return "redirect:/adminLogin?error=Unauthorized access";
            }

            if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty() || userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
                model.addAttribute("error-message", "All fields are required");
                return "redirect:/users";
            }

            userService.updateUser(id, userDTO);
            return "redirect:/users";
        } catch (Exception e) {
            return "redirect:/users?error=" + e.getMessage();
        }
    }

//    @DeleteMapping("/deleteUser/{id}")
//    public ResponseEntity<String> softDeleteUser(@PathVariable int id, HttpSession session) {
//        try {
//            if (session.getAttribute("userId") == null || !"admin".equals(session.getAttribute("role"))) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
//            }
//
//            userService.softDeleteUserById(id);
//            return ResponseEntity.ok("User deactivated (soft deleted) successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//        }
//    }

    @PostMapping("/updatePassword")
    public String updatePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        try {
            userService.updatePassword(currentPassword, newPassword, userId);
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/settings?error=" + e.getMessage();
        }
    }

    @GetMapping("/adminLogout")
    public String logoutAdmin(HttpSession session) {
        session.invalidate();
        return "redirect:/adminLogin";
    }
}