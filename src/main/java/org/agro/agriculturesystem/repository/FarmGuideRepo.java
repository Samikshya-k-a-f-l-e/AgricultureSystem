package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.FarmGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FarmGuideRepo extends JpaRepository<FarmGuide, Integer> {
    List<FarmGuide> findByFeaturedTrue();
    Optional<FarmGuide> findBySlug(String slug);
    List<FarmGuide> findByFeaturedTrueOrderByFeaturedPositionAsc();
}
