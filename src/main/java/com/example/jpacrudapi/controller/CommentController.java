package com.example.jpacrudapi.controller;

import com.example.jpacrudapi.model.Comment;
import com.example.jpacrudapi.model.Tutorial;
import com.example.jpacrudapi.repository.CommentRepository;
import com.example.jpacrudapi.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("tutorials/{tutorialId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByTutorialId(@PathVariable(value = "tutorialId") Long tutorialId) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId).orElseThrow(
                () -> new IllegalArgumentException("Invalid tutorial Id:" + tutorialId));

        List<Comment> comments = new ArrayList<>(tutorial.getComments());
        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable(value = "id") Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid comment Id:" + id));
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable(value = "tutorialId") Long tutorialId,
                                                 @RequestBody Comment commentRequest) {
        Comment comment = tutorialRepository.findById(tutorialId).map(tutorial -> {
            tutorial.getComments().add(commentRequest);
            return commentRepository.save(commentRequest);
        }).orElseThrow(() -> new IllegalArgumentException("Invalid tutorial Id:" + tutorialId));
        return ResponseEntity.ok().body(comment);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable(value = "id") Long id, @RequestBody Comment commentRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
        comment.setContent(commentRequest.getContent());
        return ResponseEntity.ok().body(commentRepository.save(comment));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable(value = "id") Long id) {
       commentRepository.deleteById(id);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<HttpStatus> deleteAllCommentsByTutorialId(@PathVariable(value = "tutorialId") Long tutorialId) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId).orElseThrow(
                () -> new IllegalArgumentException("Invalid tutorial Id:" + tutorialId));
        tutorial.getComments().clear();
        tutorialRepository.save(tutorial);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
