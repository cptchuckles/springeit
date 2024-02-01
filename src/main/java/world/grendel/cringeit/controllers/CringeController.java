package world.grendel.cringeit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import world.grendel.cringeit.annotation.AuthenticatedRoute;
import world.grendel.cringeit.models.Cringe;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.services.CringeService;

/**
 * CringeController
 */
@Controller
@RequestMapping("/cringe")
public class CringeController {
    private final CringeService cringeService;

    public CringeController(CringeService cringeService) {
        this.cringeService = cringeService;
    }

    @GetMapping
    @AuthenticatedRoute
    public String index(HttpSession session, Model model) {
        model.addAttribute("allCringe", cringeService.getAll());
        return "cringe/index.jsp";
    }

    @GetMapping("/{id}")
    @AuthenticatedRoute
    public String show(
        HttpSession session, Model model,
        @PathVariable("id") Long cringeId
    ) {
        Cringe cringe = cringeService.getById(cringeId);
        if (cringe == null) {
            return "redirect:/cringe";
        }
        model.addAttribute("cringe", cringe);
        return "cringe/show.jsp";
    }

    @GetMapping("/new")
    @AuthenticatedRoute
    public String createForm(HttpSession session, Model model) {
        model.addAttribute("newCringe", new Cringe());
        return "cringe/new.jsp";
    }

    @PostMapping
    @AuthenticatedRoute
    public String create(
        HttpSession session, Model model, User currentUser,
        @Valid @ModelAttribute("newCringe") Cringe newCringe, BindingResult result
    ) {
        if (result.hasErrors()) {
            model.addAttribute("newCringe", newCringe);
            return "cringe/new.jsp";
        }
        cringeService.create(newCringe, currentUser);
        return "redirect:/cringe";
    }

    @GetMapping("/{id}/edit")
    @AuthenticatedRoute
    public String edit(
        HttpSession session, Model model, User currentUser,
        @PathVariable("id") Long id
    ) {
        Cringe cringe = cringeService.getById(id);
        if (cringe == null) {
            return "redirect:/cringe";
        }
        if (!currentUser.isAdmin() && cringe.getUser().getId() != currentUser.getId()) {
            return String.format("redirect:/cringe/%d", cringe.getId());
        }
        model.addAttribute("cringe", cringe);
        return "cringe/edit.jsp";
    }

    @PutMapping("/{id}")
    @AuthenticatedRoute
    public String update(
        HttpSession session, Model model, User currentUser,
        @Valid @ModelAttribute("cringe") Cringe cringe, BindingResult result,
        @PathVariable("id") Long id
    ) {
        if (result.hasErrors()) {
            model.addAttribute("cringe", cringe);
            model.addAttribute(User.modelKey, currentUser);
            return "cringe/edit.jsp";
        }
        Cringe targetCringe = cringeService.getById(id);
        targetCringe.setHeadline(cringe.getHeadline());
        targetCringe.setUrl(cringe.getUrl());
        targetCringe.setDescription(cringe.getDescription());
        cringeService.update(targetCringe, currentUser);
        return String.format("redirect:/cringe/%d", cringe.getId());
    }
}
