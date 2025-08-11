package org.agro.agriculturesystem.dto;
// for request handling.

import java.time.LocalDate;

public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String role = "user";
    private LocalDate joinedDate;

    // Constructors
    public UserDTO() {}

    public UserDTO(String name, String email, String password, String confirmPassword, String role, LocalDate joinedDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.joinedDate = joinedDate;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return java.util.Objects.equals(name, userDTO.name) &&
               java.util.Objects.equals(email, userDTO.email) &&
               java.util.Objects.equals(password, userDTO.password) &&
               java.util.Objects.equals(confirmPassword, userDTO.confirmPassword) &&
               java.util.Objects.equals(role, userDTO.role) &&
               java.util.Objects.equals(joinedDate, userDTO.joinedDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, email, password, confirmPassword, role, joinedDate);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", role='" + role + '\'' +
                ", joinedDate=" + joinedDate +
                '}';
    }
}
