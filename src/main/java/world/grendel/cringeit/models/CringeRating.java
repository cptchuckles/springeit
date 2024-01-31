package world.grendel.cringeit.models;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * CringeRating
 */
@Entity
@Table(name = "springe_cringe_ratings")
@IdClass(CringeRatingPK.class)
public class CringeRating {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "cringe_id")
    private Cringe cringe;

    @Range(min = -1, max = 1)
    private Integer delta;

    public CringeRating() {
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Cringe getCringe() {
		return cringe;
	}

	public void setCringe(Cringe cringe) {
		this.cringe = cringe;
	}

	public Integer getDelta() {
		return delta;
	}

	public void setDelta(Integer delta) {
		this.delta = delta;
	}
}
