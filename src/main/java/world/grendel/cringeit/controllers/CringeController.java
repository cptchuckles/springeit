package world.grendel.cringeit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import world.grendel.cringeit.annotation.AuthenticatedRoute;
import world.grendel.cringeit.models.Cringe;
import world.grendel.cringeit.models.CringeRating;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.services.CringeRatingService;
import world.grendel.cringeit.services.CringeService;

/**
 * CringeController
 */
@Controller
@RequestMapping("/cringe")
public class CringeController {
    private final CringeService cringeService;
	private final CringeRatingService ratingService;

    public CringeController(CringeService cringeService, CringeRatingService ratingService) {
        this.cringeService = cringeService;
		this.ratingService = ratingService;
    }

    @GetMapping
    @AuthenticatedRoute
    public String index(HttpSession session, Model model) {
        model.addAttribute("allCringe", cringeService.getAll());
        return "cringe/index.jsp";
    }

    @GetMapping("/{id}")
    @AuthenticatedRoute
    public String show(@PathVariable("id") Long cringeId, HttpSession session, Model model, User currentUser) {
        Cringe cringe = cringeService.getById(cringeId);
        if (cringe == null) {
            return "redirect:/cringe";
        }
        model.addAttribute("cringe", cringe);
        CringeRating rating = ratingService.getFromUserForCringe(currentUser, cringe);
        if (rating != null) {
            model.addAttribute("rating", rating.getDelta() > 0 ? "up" : "down");
        }
        else {
            model.addAttribute("rating", "none");
        }
        return "cringe/view.jsp";
    }

    @GetMapping("/new")
    @AuthenticatedRoute
    public String createForm(HttpSession session, Model model) {
        model.addAttribute("newCringe", new Cringe());
        return "cringe/new.jsp";
    }

    @GetMapping("/whine-confirmation")
    @AuthenticatedRoute
    public String confirmWhine(HttpSession session) {
        if (session.getAttribute("whine") == null) {
            return "redirect:/cringe";
        }
        session.removeAttribute("whine");
        return "cringe/whineConfirmation.jsp";
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
    public String edit(@PathVariable("id") Long id, HttpSession session, Model model, User currentUser) {
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

    @DeleteMapping(path = "/{cringeId}")
    @AuthenticatedRoute
    public String delete(@PathVariable("cringeId") Long cringeId, HttpSession session, Model model, User currentUser) {
        cringeService.deleteById(cringeId, currentUser);
        return "redirect:/cringe";
    }

    @Transactional
    @PostMapping("/{id}/rate")
    @AuthenticatedRoute
    public String rate(
        HttpSession session, Model model, User currentUser,
        @PathVariable("id") Long id,
        @RequestParam(name = "delta") Integer delta
    ) {
        Cringe cringe = cringeService.getById(id);
        if (cringe == null) {
            return "redirect:/cringe";
        }
        if (delta < -1 || 1 < delta) {
            return "redirect:/cringe";
        }
        CringeRating rating = ratingService.getFromUserForCringe(currentUser, cringe);
        if (rating != null && delta == rating.getDelta()) {
            ratingService.delete(rating);
        }
        else {
            rating = new CringeRating(currentUser, cringe, delta);
            ratingService.upsert(rating);
        }
        return String.format("redirect:/cringe/%d", id);
    }

    @GetMapping("/{id}/whine")
    @AuthenticatedRoute
    public String whine(
        HttpSession session, Model model, User currentUser,
        @PathVariable("id") Long id
    ) {
        Cringe cringe = cringeService.getById(id);
        if (cringe == null) {
            return "redirect:/cringe";
        }
        model.addAttribute("cringe", cringe);
        return "cringe/whine.jsp";
    }

    @PostMapping("/{id}/whine")
    @AuthenticatedRoute
    public String addWhine(
        HttpSession session, Model model, User currentUser,
        @PathVariable("id") Long id,
        @RequestParam("iAmABaby") Boolean whinerIsABaby
    ) {
        if (!whinerIsABaby) {
            return "redirect:/cringe";
        }
        Cringe cringe = cringeService.getById(id);
        if (cringe == null) {
            return "redirect:/cringe";
        }
        cringeService.addWhineByUser(cringe, currentUser);
        session.setAttribute("whine", cringe.getId());
        return "redirect:/cringe/whine-confirmation";
    }
}
