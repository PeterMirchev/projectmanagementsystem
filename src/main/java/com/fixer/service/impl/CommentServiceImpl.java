package com.fixer.service.impl;

import com.fixer.model.Comment;
import com.fixer.model.Issue;
import com.fixer.model.User;
import com.fixer.repository.CommentRepository;
import com.fixer.repository.IssueRepository;
import com.fixer.repository.UserRepository;
import com.fixer.service.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              IssueRepository issueRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Comment createComment(Long issueId, Long userId, String commentInput) throws Exception {

        Optional<Issue> issue = issueRepository.findById(issueId);
        Optional<User> user = userRepository.findById(userId);

        if (issue.isEmpty()) {
            throw new Exception("Invalid issue ID.");
        }
        if (user.isEmpty()) {
            throw new Exception("Invalid user ID.");
        }

        Comment comment = mapToComment(issue, user, commentInput);

        Comment persistedComment = commentRepository.save(comment);
        issue.get().getComments().add(persistedComment);

        return persistedComment;
    }

    @Override
    public void deleteComment(Long commentId, Long userId) throws Exception {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception("Invalid comment ID."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Invalid user ID."));

        if (!comment.getUser().equals(user)) {
            throw new Exception("User does not have permission to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> findCommentsByIssueId(Long issueId) {

        return commentRepository.findCommentByIssueId(issueId);
    }

    protected static Comment mapToComment(Optional<Issue> issue, Optional<User> user, String commentInput) {
        Comment comment = new Comment();
        comment.setIssue(issue.get());
        comment.setUser(user.get());
        comment.setContent(commentInput);
        comment.setCreatedDateTime(LocalDateTime.now());
        return comment;
    }
}
