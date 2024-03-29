package world.grendel.cringeit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import world.grendel.cringeit.annotation.AuthenticatedRoute;
import world.grendel.cringeit.dataobjects.UserLoginDTO;
import world.grendel.cringeit.dataobjects.UserRegisterDTO;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.services.UserService;

/**
 * UserController
 */
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginOrRegister(
        @ModelAttribute("userLogin") UserLoginDTO userLogin, 
        @ModelAttribute("userRegister") UserRegisterDTO userRegister
    ) {
        return "authenticate.jsp";
    }

    @GetMapping("/users/{id}")
    @AuthenticatedRoute
    public String show(
        HttpSession session, Model model, User currentUser,
        @PathVariable("id") Long id
    ) {
        User user = userService.getById(id);
        if (user == null) {
            return "redirect:/cringe";
        }
        if (currentUser.isAdmin()) {
            model.addAttribute("allUsers", userService.getAll());
        }
        model.addAttribute("user", user);
        return "user/view.jsp";
    }

    @PostMapping("/login")
    public String login(
        @Valid @ModelAttribute("userLogin") UserLoginDTO userLogin, BindingResult result,
        @ModelAttribute("userRegister") UserRegisterDTO userRegister,
        HttpSession session, Model model
    ) {
        User user = userService.login(userLogin, result);
        if (result.hasErrors()) {
            model.addAttribute("userLogin", userLogin);
            model.addAttribute("userRegister", new UserRegisterDTO());
            return "authenticate.jsp";
        }
        session.setAttribute(User.sessionKey, user.getId());
        return "redirect:/cringe";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "redirect:/logout";
    }

    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("userRegister") UserRegisterDTO userRegister, BindingResult result,
        @ModelAttribute("userLogin") UserLoginDTO userLogin,
        HttpSession session,
        Model model
    ) {
        User newUser = userService.register(userRegister, result);
        if (result.hasErrors()) {
            model.addAttribute("userRegister", userRegister);
            model.addAttribute("userLogin", new UserLoginDTO());
            return "authenticate.jsp";
        }
        session.setAttribute(User.sessionKey, newUser.getId());
        return "redirect:/cringe";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
