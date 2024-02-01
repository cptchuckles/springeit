package world.grendel.cringeit.services;

import java.util.List;

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

	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
    }

    public List<Comment> getAllForCringe(Long cringeId) {
        return commentRepository.findAllByCringeId(cringeId);
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

    public Comment update(Comment comment, User editor) {
        if (comment == null) {
            return null;
        }
        if (!editor.isAdmin() && comment.getUser().getId() != editor.getId()) {
            return null;
        }
        return commentRepository.save(comment);
    }

    public boolean delete(Comment comment, User deleter) {
        if (comment == null) {
            return false;
        }
        if (!deleter.isAdmin() && comment.getUser().getId() != deleter.getId()) {
            return false;
        }
        commentRepository.delete(comment);
        return true;
    }
}
