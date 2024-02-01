package world.grendel.cringeit.services;

import org.springframework.stereotype.Service;

import world.grendel.cringeit.models.CringeRating;
import world.grendel.cringeit.models.CringeRatingPK;
import world.grendel.cringeit.repositories.CringeRatingRepository;

/**
 * CringeRatingService
 */
@Service
public class CringeRatingService {
    private final CringeRatingRepository repo;

    public CringeRatingService(CringeRatingRepository repo) {
        this.repo = repo;
    }

    public CringeRating getById(CringeRatingPK id) {
        return repo.findById(id).orElse(null);
    }

    public CringeRating upsert(CringeRating rating) {
        return repo.save(rating);
    }

    public void delete(CringeRating rating) {
        System.out.println("deleting rating");
        repo.deleteFromUserForCringe(rating.getUser(), rating.getCringe());
    }
}
