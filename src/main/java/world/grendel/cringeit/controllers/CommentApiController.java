package world.grendel.cringeit.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import world.grendel.cringeit.annotation.AuthenticatedRoute;
import world.grendel.cringeit.models.Comment;
import world.grendel.cringeit.models.Cringe;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.services.CommentService;
import world.grendel.cringeit.services.CringeService;

/**
 * CommentApiController
 */
@RestController
@RequestMapping("/api")
public class CommentApiController {
    private final CommentService commentService;
	private final CringeService cringeService;

    public CommentApiController(CommentService commentService, CringeService cringeService) {
        this.commentService = commentService;
		this.cringeService = cringeService;
    }

    @GetMapping("/cringe/{cringeId}/comments")
    @AuthenticatedRoute(isApi = true)
    public List<Comment> getAllForCringe(@PathVariable("cringeId") Long cringeId, HttpSession session) {
        return commentService.getAllForCringe(cringeId);
    }

    @PostMapping(path = "/cringe/{cringeId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @AuthenticatedRoute(isApi = true)
    public ResponseEntity<Comment> create(
        HttpSession session, Model model, User currentUser,
        @PathVariable("cringeId") Long cringeId,
        @RequestBody Comment newComment
    ) {
        Cringe cringe = cringeService.getById(cringeId);
        if (cringe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Comment savedComment = commentService.create(newComment, currentUser, cringe);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }
}
