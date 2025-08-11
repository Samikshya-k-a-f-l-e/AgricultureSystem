package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.ForumTopics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumTopicsRepo extends JpaRepository<ForumTopics, Long> {
    Optional<ForumTopics> findByTitle(String title);
    ForumTopics findBySlug(String slug);

    @EntityGraph(attributePaths = {"author"})
    Page<ForumTopics> findAll(Pageable pageable);
}
