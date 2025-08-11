package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {
    Profile findByUserId(Integer userId);
}
