package world.grendel.cringeit.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import world.grendel.cringeit.models.Comment;
import world.grendel.cringeit.models.CommentRating;
import world.grendel.cringeit.models.CommentRatingPK;
import world.grendel.cringeit.models.User;

/**
 * CommentRatingRepository
 */
@Repository
public interface CommentRatingRepository extends CrudRepository<CommentRating, CommentRatingPK> {
    @Modifying
    @Query("DELETE FROM CommentRating c WHERE c.user = :user AND c.comment = :comment")
    public void deleteFromUserForComment(@Param("user") User user, @Param("comment") Comment comment);
}
