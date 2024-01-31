package world.grendel.cringeit.models;

import java.io.Serializable;

/**
 * CommentRatingPK
 */
public class CommentRatingPK implements Serializable {
    private Long user;
    private Long comment;

    public CommentRatingPK() {
    }
    public CommentRatingPK(Long user, Long comment) {
        this.user = user;
        this.comment = comment;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }
}
