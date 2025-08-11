package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.ForumReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumReplyRepo extends JpaRepository<ForumReply, Long> {
    Page<ForumReply> findByTopicIdOrderByCreatedAtAsc(Long topicId, Pageable pageable);
}
