package world.grendel.cringeit.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;
import world.grendel.cringeit.dataobjects.UserLoginDTO;
import world.grendel.cringeit.dataobjects.UserRegisterDTO;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.repositories.UserRepository;

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
        Long userId = (Long) session.getAttribute(User.sessionKey);
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
        if (userRepository.findByUsername(userRegister.getUsername()).isPresent()) {
            result.rejectValue("username", "Username", "This username is already taken");
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

    public User login(UserLoginDTO userLogin, BindingResult result) {
        if (result.hasErrors()) {
            return null;
        }
        Optional<User> targetUser = userRepository.findByEmail(userLogin.getEmail());
        if (targetUser.isEmpty()) {
            result.rejectValue("email", "login", "User credentials are invalid");
            return null;
        }
        User user = targetUser.get();
        if (! BCrypt.checkpw(userLogin.getPassword(), user.getPasswordHash())) {
            result.rejectValue("email", "login", "User credentials are invalid");
            return null;
        }
        return user;
    }

    public User update(User whomstdve) {
        return userRepository.save(whomstdve);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
