package org.agro.agriculturesystem.service;

import jakarta.persistence.EntityNotFoundException;
import org.agro.agriculturesystem.dto.ReactionDTO;
import org.agro.agriculturesystem.model.*;
import org.agro.agriculturesystem.repository.ForumReplyRepo;
import org.agro.agriculturesystem.repository.ForumTopicsRepo;
import org.agro.agriculturesystem.repository.ReactionRepo;
import org.agro.agriculturesystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReactionService {

    private final ReactionRepo reactionRepo;
    private final ForumTopicsRepo forumTopicsRepo;
    private final UserRepository userRepo;
    private final ForumReplyRepo forumReplyRepo;

    public ReactionService(ReactionRepo reactionRepo, ForumTopicsRepo forumTopicsRepo,
                           UserRepository userRepo, ForumReplyRepo forumReplyRepo) {
        this.reactionRepo = reactionRepo;
        this.forumTopicsRepo = forumTopicsRepo;
        this.userRepo = userRepo;
        this.forumReplyRepo = forumReplyRepo;
    }

    private ReactionDTO convertToDTO(Reaction reaction) {
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(reaction.getId());
        reactionDTO.setReactionType(reaction.getReactionType());
        reactionDTO.setUserId(reaction.getUser().getId());
        reactionDTO.setCreatedAt(reaction.getCreatedAt());

        if (reaction.getTopic() != null) {
            reactionDTO.setTopicId(reaction.getTopic().getId());
        }

        if (reaction.getReply() != null) {
            reactionDTO.setReplyId(reaction.getReply().getId());
        }

        return reactionDTO;
    }

    public Map<String, Long> getReactionTypeCountsForTopic(Long topicId) {
        List<Object[]> results = reactionRepo.countReactionsByTypeForTopic(topicId);
        Map<String, Long> reactionMap = new HashMap<>();
        for (Object[] row : results) {
            reactionMap.put((String) row[0], (Long) row[1]);
        }
        return reactionMap;
    }

    public Map<String, Long> getReactionTypeCountsForReply(Long replyId) {
        List<Object[]> results = reactionRepo.countReactionsByTypeForReply(replyId);
        Map<String, Long> reactionMap = new HashMap<>();
        for (Object[] row : results) {
            reactionMap.put((String) row[0], (Long) row[1]);
        }
        return reactionMap;
    }

    @Transactional
    public void deleteReaction(Long reactionId) {
        if (reactionRepo.existsById(reactionId)) {
            reactionRepo.deleteById(reactionId);
        } else {
            throw new EntityNotFoundException("Reaction not found with id: " + reactionId);
        }
    }

    @Transactional
    public void addReaction(ReactionDTO reactionDTO) {
        User user = userRepo.findById(reactionDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check for existing reaction of the same type
        boolean reactionExists;
        if (reactionDTO.getTopicId() != null) {
            reactionExists = reactionRepo.existsByUserAndTopicAndReactionType(
                    (long) user.getId(),
                    reactionDTO.getTopicId(),
                    reactionDTO.getReactionType()
            );
        } else if (reactionDTO.getReplyId() != null) {
            reactionExists = reactionRepo.existsByUserAndReplyAndReactionType(
                    (long) user.getId(),
                    reactionDTO.getReplyId(),
                    reactionDTO.getReactionType()
            );
        } else {
            throw new IllegalArgumentException("Either topicId or replyId must be provided");
        }

        if (reactionExists) {
            throw new IllegalStateException("You have already reacted with this reaction type");
        }

        // Check if user has a different reaction type
        Optional<Reaction> existingDifferentReaction;
        if (reactionDTO.getTopicId() != null) {
            existingDifferentReaction = reactionRepo.findByUserAndTopic((long) user.getId(), reactionDTO.getTopicId());
        } else {
            existingDifferentReaction = reactionRepo.findByUserAndReply((long) user.getId(), reactionDTO.getReplyId());
        }

        // If user has a different reaction, remove it first
        if (existingDifferentReaction.isPresent()) {
            Reaction oldReaction = existingDifferentReaction.get();
            if (oldReaction.getReactionType().equals(reactionDTO.getReactionType())) {
                // Remove the reaction if it's the same type
                reactionRepo.delete(oldReaction);
                return;
            } else {
                // Update the reaction if it's a different type
                oldReaction.setReactionType(reactionDTO.getReactionType());
                reactionRepo.save(oldReaction);
                return;
            }
        }

        // Create new reaction if none exists
        ForumTopics topic = reactionDTO.getTopicId() != null ?
                forumTopicsRepo.findById(reactionDTO.getTopicId()).orElse(null) : null;
        ForumReply reply = reactionDTO.getReplyId() != null ?
                forumReplyRepo.findById(reactionDTO.getReplyId()).orElse(null) : null;

        try {
            Reaction newReaction = Reaction.builder()
                    .reactionType(reactionDTO.getReactionType())
                    .topic(topic)
                    .user(user)
                    .reply(reply)
                    .createdAt(LocalDateTime.now())
                    .build();

            reactionRepo.save(newReaction);
        } catch (Exception e) {
            throw new IllegalStateException("Could not add reaction: " + e.getMessage());
        }
    }
}
