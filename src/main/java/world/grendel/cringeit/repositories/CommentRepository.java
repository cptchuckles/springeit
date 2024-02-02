package world.grendel.cringeit.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import world.grendel.cringeit.models.Comment;

/**
 * CommentRepository
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    public List<Comment> findAll();
    public List<Comment> findAllByCringeId(Long cringeId);
    public List<Comment> findAllByCringeIdOrderByCreatedAtDesc(Long cringeId);
    public List<Comment> findAllByCringeIdOrderByCreatedAtAsc(Long cringeId);
    public List<Comment> findAllByUserId(Long userId);
}
