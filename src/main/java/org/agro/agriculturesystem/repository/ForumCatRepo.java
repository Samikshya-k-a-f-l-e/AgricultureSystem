package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.ForumCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumCatRepo extends JpaRepository<ForumCategory, Integer> {
    Optional<ForumCategory> findByName(String name);

}
