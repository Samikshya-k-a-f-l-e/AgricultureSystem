package org.agro.agriculturesystem.service;

import org.agro.agriculturesystem.dto.ForumReplyDTO;
import org.agro.agriculturesystem.model.ForumReply;
import org.agro.agriculturesystem.model.ForumTopics;
import org.agro.agriculturesystem.model.User;
import org.agro.agriculturesystem.repository.ForumReplyRepo;
import org.agro.agriculturesystem.repository.ForumTopicsRepo;
import org.agro.agriculturesystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ForumReplyService {
    private final ForumReplyRepo forumReplyRepository;
    private final ForumTopicsRepo forumTopicRepository;
    private final UserRepository userRepository;

    public ForumReplyService(ForumReplyRepo forumReplyRepository, ForumTopicsRepo forumTopicRepository, UserRepository userRepository) {
        this.forumReplyRepository = forumReplyRepository;
        this.forumTopicRepository = forumTopicRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ForumReplyDTO createReply(ForumReplyDTO replyDTO) {
        if (replyDTO.getContent() == null || replyDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Reply content cannot be empty");
        }

        ForumTopics topic = forumTopicRepository.findById(replyDTO.getTopicId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Topic ID"));

        User user = userRepository.findById( replyDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ForumReply reply = ForumReply.builder()
                .content(replyDTO.getContent())
                .notifyReplies(replyDTO.getNotifyReplies() != null ? replyDTO.getNotifyReplies() : true)
                .topic(topic)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        forumTopicRepository.save(topic);

        ForumReply savedReply = forumReplyRepository.save(reply);

        return convertToDTO(savedReply);
    }

    private ForumReplyDTO convertToDTO(ForumReply reply) {
        ForumReplyDTO dto = new ForumReplyDTO();
        dto.setId(reply.getId());
        dto.setContent(reply.getContent());
        dto.setNotifyReplies(reply.getNotifyReplies());
        dto.setTopicId(reply.getTopic().getId());
        dto.setUserId(reply.getUser().getId());
        dto.setCreatedAt(reply.getCreatedAt());
        dto.setUpdatedAt(reply.getUpdatedAt());
        return dto;
    }

    public Page<ForumReply> getRepliesByTopicId(Long topicId, Pageable pageable) {
        return forumReplyRepository.findByTopicIdOrderByCreatedAtAsc(topicId, pageable);
    }

    @Transactional
    public void deleteReply(Long replyId) {
        ForumReply reply = forumReplyRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("Reply not found"));

        forumReplyRepository.delete(reply);
    }

}