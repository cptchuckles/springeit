package world.grendel.userlogindemo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import world.grendel.userlogindemo.models.User;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findByEmail(String search);
    public List<User> findAll();
}
