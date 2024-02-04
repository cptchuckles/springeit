package world.grendel.cringeit.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import world.grendel.cringeit.annotation.AuthenticatedRoute;
import world.grendel.cringeit.dataobjects.CommentDTO;
import world.grendel.cringeit.models.Comment;
import world.grendel.cringeit.models.CommentRating;
import world.grendel.cringeit.models.Cringe;
import world.grendel.cringeit.models.User;
import world.grendel.cringeit.services.CommentRatingService;
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
	private final CommentRatingService ratingService;

    public CommentApiController(
        CommentService commentService,
        CringeService cringeService,
        CommentRatingService ratingService
    ) {
        this.commentService = commentService;
		this.cringeService = cringeService;
		this.ratingService = ratingService;
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
        @RequestBody CommentDTO newCommentData
    ) {
        Cringe cringe = cringeService.getById(cringeId);
        if (cringe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Comment newComment = new Comment();
        newComment.setContent(newCommentData.getContent());
        Comment savedComment = commentService.create(newComment, currentUser, cringe);
        Long parentCommentId = newCommentData.getParentCommentId();
        if (parentCommentId != null) {
            Comment parentComment = commentService.getById(parentCommentId);
            if (parentComment != null) {
                commentService.addReply(parentComment, savedComment);
            }
        }
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping(path = "/comments/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @AuthenticatedRoute(isApi = true)
    public ResponseEntity<Comment> update(
        HttpSession session, Model model, User currentUser,
        @PathVariable("commentId") Long commentId,
        @RequestBody Comment comment
    ) {
        Comment targetComment = commentService.getById(commentId);
        if (targetComment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        targetComment.setContent(comment.getContent());
        Comment savedComment = commentService.update(targetComment, currentUser);
        if (savedComment == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(savedComment, HttpStatus.ACCEPTED);
    }

    @Transactional
    @PostMapping(path = "/comments/{id}/rate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @AuthenticatedRoute
    public ResponseEntity<Integer> rate(
        HttpSession session, Model model, User currentUser,
        @PathVariable("id") Long id,
        @RequestBody CommentRating rating
    ) {
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Integer delta = rating.getDelta();
        Integer total = comment.getTotalRating();
        if (delta != -1 && delta != 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CommentRating targetRating = ratingService.getFromUserForComment(currentUser, comment);
        if (targetRating != null) {
            if (delta == targetRating.getDelta()) {
                ratingService.delete(targetRating);
                delta = -delta;
            }
            else {
                rating = new CommentRating(currentUser, comment, delta);
                ratingService.upsert(rating);
                delta *= 2;
            }
        }
        else {
            rating = new CommentRating(currentUser, comment, delta);
            ratingService.upsert(rating);
        }
        return new ResponseEntity<>(total + delta, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/comments/{id}")
    @AuthenticatedRoute(isApi = true)
    public ResponseEntity<Boolean> delete(
        HttpSession session, Model model, User currentUser,
        @PathVariable("id") Long id
    ) {
        if (!commentService.removeById(id, currentUser)) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
    }
}
