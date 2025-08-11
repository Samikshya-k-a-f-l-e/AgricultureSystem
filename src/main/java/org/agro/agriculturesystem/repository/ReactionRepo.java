package org.agro.agriculturesystem.repository;

import org.agro.agriculturesystem.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepo extends JpaRepository<Reaction, Long> {
    @Query("SELECT r.reactionType, COUNT(r) FROM Reaction r WHERE r.topic.id = :topicId GROUP BY r.reactionType")
    List<Object[]> countReactionsByTypeForTopic(@Param("topicId") Long topicId);

    @Query("SELECT r.reactionType, COUNT(r) FROM Reaction r WHERE r.reply.id = :replyId GROUP BY r.reactionType")
    List<Object[]> countReactionsByTypeForReply(@Param("replyId") Long replyId);

    @Query("SELECT r FROM Reaction r WHERE r.user.id = :userId AND r.topic.id = :topicId")
    Optional<Reaction> findByUserAndTopic(@Param("userId") Long userId, @Param("topicId") Long topicId);

    @Query("SELECT r FROM Reaction r WHERE r.user.id = :userId AND r.reply.id = :replyId")
    Optional<Reaction> findByUserAndReply(@Param("userId") Long userId, @Param("replyId") Long replyId);

    @Query("SELECT COUNT(r) > 0 FROM Reaction r WHERE r.user.id = :userId AND r.topic.id = :topicId AND r.reactionType = :reactionType")
    boolean existsByUserAndTopicAndReactionType(@Param("userId") Long userId,
                                                @Param("topicId") Long topicId,
                                                @Param("reactionType") String reactionType);

    @Query("SELECT COUNT(r) > 0 FROM Reaction r WHERE r.user.id = :userId AND r.reply.id = :replyId AND r.reactionType = :reactionType")
    boolean existsByUserAndReplyAndReactionType(@Param("userId") Long userId,
                                                @Param("replyId") Long replyId,
                                                @Param("reactionType") String reactionType);



}

