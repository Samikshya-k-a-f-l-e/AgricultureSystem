package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.GuideCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuideCatRepo extends JpaRepository<GuideCategory, Integer> {
    Optional<GuideCategory> findByName(String name);
}