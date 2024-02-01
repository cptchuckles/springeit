package world.grendel.cringeit.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import world.grendel.cringeit.models.Cringe;
import world.grendel.cringeit.models.CringeRating;
import world.grendel.cringeit.models.CringeRatingPK;
import world.grendel.cringeit.models.User;

/**
 * CringeRatingRepository
 */
@Repository
public interface CringeRatingRepository extends CrudRepository<CringeRating, CringeRatingPK> {
    public List<CringeRating> findAll();
    public List<CringeRating> findAllByUserId(Long id);
    public List<CringeRating> findAllByCringeId(Long id);
    public Optional<CringeRating> findById(CringeRatingPK id);
    public List<CringeRating> findAllByCringeIdIn(List<Long> cringeIds);

    @Modifying
    @Query("DELETE FROM CringeRating c WHERE c.user = :user AND c.cringe = :cringe")
    public void deleteFromUserForCringe(@Param("user") User user, @Param("cringe") Cringe cringe);
}
