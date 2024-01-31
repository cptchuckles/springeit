package world.grendel.cringeit.models;

import java.io.Serializable;

/**
 * CringeRatingPK
 */
public class CringeRatingPK implements Serializable {
    private Long user;
    private Long cringe;

    public CringeRatingPK() {
    }
    public CringeRatingPK(Long userId, Long cringeId) {
        this.user = userId;
        this.cringe = cringeId;
    }

    public Long getUser() {
        return user;
    }
    public void setUser(Long userId) {
        this.user = userId;
    }
    public Long getCringe() {
        return cringe;
    }
    public void setCringe(Long cringeId) {
        this.cringe = cringeId;
    }
}
