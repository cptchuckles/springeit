package world.grendel.cringeit.services;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import world.grendel.cringeit.models.Comment;
import world.grendel.cringeit.models.Cringe;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.repositories.CommentRepository;

/**
 * CommentService
 */
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentRatingService ratingService;

    public CommentService(CommentRepository commentRepository, CommentRatingService ratingService) {
        this.commentRepository = commentRepository;
        this.ratingService = ratingService;
    }

    public List<Comment> getAllForCringe(Long cringeId) {
        return commentRepository.findAllByCringeIdOrderByCreatedAtAsc(cringeId).stream()
            .filter(c -> c.getParentComment() == null)
            .toList();
    }

    public List<Comment> getAllForUser(Long userId) {
        return commentRepository.findAllByUserId(userId);
    }

    public Comment getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment create(Comment newComment, User author, Cringe cringe) {
        return create(newComment, author, cringe, null);
    }
    public Comment create(Comment newComment, User author, Cringe cringe, Comment parentComment) {
        newComment.setUser(author);
        newComment.setCringe(cringe);
        newComment.setParentComment(parentComment);
        return commentRepository.save(newComment);
    }

    public Comment addReply(Comment parentComment, Comment reply) {
        parentComment.getReplies().add(reply);
        commentRepository.save(parentComment);
        reply.setParentComment(parentComment);
        return commentRepository.save(reply);
    }

    public Comment update(Comment comment, User editor) {
        if (!editor.isAdmin() && comment.getUser().getId() != editor.getId()) {
            return null;
        }
        return commentRepository.save(comment);
    }

    public HttpStatus removeById(Long commentId, User remover) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return HttpStatus.NOT_FOUND;
        }
        if (!remover.isAdmin() && comment.getUser() != null && comment.getUser().getId() != remover.getId()) {
            return HttpStatus.FORBIDDEN;
        }

        if (comment.getReplies().size() > 0) {
            if (!erase(comment, remover)) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        else {
            if (!delete(comment, remover)) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return HttpStatus.ACCEPTED;
    }

    private boolean erase(Comment comment, User eraser) {
        comment.getUser().getComments().remove(comment);
        comment.setUser(null);
        comment.getRatings().stream().forEach(rating -> ratingService.delete(rating));
        comment.setRatings(Collections.emptySet());
        comment.setContent("message erased");
        commentRepository.save(comment);
        return true;
    }

    private boolean delete(Comment comment, User deleter) {
        if (comment == null) {
            return false;
        }
        if (!deleter.isAdmin() && comment.getUser() != null && comment.getUser().getId() != deleter.getId()) {
            return false;
        }
        Comment parentComment = comment.getParentComment();
        if (parentComment != null && parentComment.getUser() == null) {
            var parentReplies = parentComment.getReplies();
            parentReplies.remove(comment);
            commentRepository.save(parentComment);
            if (parentReplies.size() == 0) {
                delete(parentComment, deleter);
            }
        }
        commentRepository.delete(comment);
        return true;
    }
}
