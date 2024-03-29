package world.grendel.cringeit.models;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * CommentRating
 */
@Entity
@Table(name = "springe_comment_ratings")
@IdClass(CommentRatingPK.class)
public class CommentRating {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Range(min = -1, max = 1)
    @Column(columnDefinition = "TINYINT")
    private Integer delta = 0;

    public CommentRating() {
    }

    public CommentRating(User user, Comment comment, Integer delta) {
        this.user = user;
        this.comment = comment;
        this.delta = delta;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Integer getDelta() {
        return delta;
    }

    public void setDelta(Integer delta) {
        this.delta = delta;
    }
}
