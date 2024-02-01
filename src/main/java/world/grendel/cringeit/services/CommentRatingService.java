package world.grendel.cringeit.services;

import org.springframework.stereotype.Service;

import world.grendel.cringeit.models.Comment;
import world.grendel.cringeit.models.CommentRating;
import world.grendel.cringeit.models.CommentRatingPK;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.repositories.CommentRatingRepository;

/**
 * CommentRatingService
 */
@Service
public class CommentRatingService {
    private CommentRatingRepository repo;

    public CommentRatingService(CommentRatingRepository repo) {
        this.repo = repo;
    }

    public CommentRating getFromUserForComment(User user, Comment comment) {
        return repo.findById(new CommentRatingPK(user.getId(), comment.getId())).orElse(null);
    }

    public CommentRating upsert(CommentRating rating) {
        return repo.save(rating);
    }

    public void delete(CommentRating rating) {
        repo.deleteFromUserForComment(rating.getUser(), rating.getComment());
    }
}
