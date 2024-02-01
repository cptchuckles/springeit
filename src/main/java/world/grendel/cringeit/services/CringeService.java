package world.grendel.cringeit.services;

import java.util.List;

import org.springframework.stereotype.Service;

import world.grendel.cringeit.models.Cringe;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.repositories.CringeRepository;

/**
 * CringeService
 */
@Service
public class CringeService {
    private final CringeRepository cringeRepository;

    public CringeService(CringeRepository cringeRepository) {
        this.cringeRepository = cringeRepository;
    }

    public List<Cringe> getAll() {
        return cringeRepository.findAll();
    }

    public Cringe getById(Long id) {
        return cringeRepository.findById(id).orElse(null);
    }

    public Cringe create(Cringe newCringe, User creator) {
        if (creator == null) {
            return null;
        }
        newCringe.setUser(creator);
        return cringeRepository.save(newCringe);
    }

    public Cringe update(Cringe cringe, User editor) {
        if (editor == null) {
            return null;
        }
        if (editor.getId() != cringe.getUser().getId() && !editor.isAdmin()) {
            return null;
        }
        return cringeRepository.save(cringe);
    }

    public boolean deleteById(Long id, User deleter) {
        Cringe cringe = cringeRepository.findById(id).orElse(null);
        if (cringe == null) {
            return false;
        }
        if (deleter.getId() != cringe.getUser().getId() && !deleter.isAdmin()) {
            return false;
        }
        cringeRepository.deleteById(id);
        return true;
    }
}
