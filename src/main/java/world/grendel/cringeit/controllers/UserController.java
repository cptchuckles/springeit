package world.grendel.cringeit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String login(
        @ModelAttribute("userLogin") UserLoginDTO userLogin, 
        @ModelAttribute("userRegister") UserRegisterDTO userRegister
    ) {
        return "loginOrRegister.jsp";
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
            return "loginOrRegister.jsp";
        }
        session.setAttribute("currentUserId", newUser.getId());
        return "redirect:/users";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/users")
    @AuthenticatedRoute
    public String index(HttpSession session, Model model) {
        model.addAttribute("allUsers", userService.getAll());
        return "userIndex.jsp";
    }
}
