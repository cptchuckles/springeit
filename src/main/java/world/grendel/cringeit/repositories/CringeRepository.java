package world.grendel.cringeit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import world.grendel.cringeit.models.Cringe;

/**
 * CringeRepository
 */
@Repository
public interface CringeRepository extends CrudRepository<Cringe, Long> {
    public List<Cringe> findAll();
    public List<Cringe> findAllByUserId(Long userId);
    @Query(value = "SELECT * FROM springe_cringe ORDER BY created_at DESC", nativeQuery = true)
    public List<Cringe> findAllOrderByCreatedAtDesc();
}
