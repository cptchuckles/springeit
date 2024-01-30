package world.grendel.userlogindemo.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;
import world.grendel.userlogindemo.dataobjects.UserRegisterDTO;
import world.grendel.userlogindemo.models.User;
import world.grendel.userlogindemo.repositories.UserRepository;

/**
 * UserService
 */
@Service
public class UserService {
    private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
    }

    public User identifyCurrentUser(HttpSession session) throws Exception {
        Long userId = (Long) session.getAttribute("currentUserId");
        if (userId == null) {
            throw new Exception("User not found");
        }
        User currentUser = getById(userId);
        if (currentUser == null) {
            throw new Exception("User ID is not valid");
        }
        return currentUser;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User register(UserRegisterDTO userRegister, BindingResult result) {
        if (! userRegister.getPassword().equals(userRegister.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "Matches", "Password confirmation does not match");
        }
        if (userRepository.findByEmail(userRegister.getEmail()).isPresent()) {
            result.rejectValue("email", "Email", "This email address has already been registered");
        }
        if (result.hasErrors()) {
            return null;
        }
        User newUser = new User();
        newUser.setUsername(userRegister.getUsername());
        newUser.setEmail(userRegister.getEmail());
        newUser.setPasswordHash(BCrypt.hashpw(userRegister.getPassword(), BCrypt.gensalt()));
        return userRepository.save(newUser);
    }

    public User update(User whomstdve) {
        return userRepository.save(whomstdve);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
