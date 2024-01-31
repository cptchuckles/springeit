package world.grendel.cringeit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import world.grendel.cringeit.annotation.AuthenticatedRoute;
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
}
