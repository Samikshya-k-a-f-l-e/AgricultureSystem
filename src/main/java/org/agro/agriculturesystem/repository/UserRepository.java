package org.agro.agriculturesystem.repository;

import jakarta.transaction.Transactional;
import org.agro.agriculturesystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//for database operations
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    long countByRole(String role);

    @Query(value = "SELECT TO_CHAR(u.joined_date, 'YYYY-MM') as month, COUNT(u) as count " +
            "FROM users u " +
            "WHERE u.joined_date BETWEEN :start AND :end " +
            "GROUP BY TO_CHAR(u.joined_date, 'YYYY-MM') " +
            "ORDER BY TO_CHAR(u.joined_date, 'YYYY-MM')", nativeQuery = true)
    List<Object[]> countRegistrationsByMonthWithYear(@Param("start") LocalDate start,
                                                     @Param("end") LocalDate end);
}