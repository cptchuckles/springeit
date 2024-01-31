package world.grendel.cringeit.repositories;

import java.util.List;

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
}
