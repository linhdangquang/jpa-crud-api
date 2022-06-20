package com.example.jpacrudapi.controller;

import com.example.jpacrudapi.model.Tag;
import com.example.jpacrudapi.repository.TagRepository;
import com.example.jpacrudapi.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.jpacrudapi.model.Tutorial;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TutorialRepository tutorialRepository;

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return ResponseEntity.ok().body(tags);
    }

    @GetMapping("/tutorials/{tutorialId}/tags")
    public ResponseEntity<List<Tag>> getAllTagsByTutorialId(@PathVariable(value = "tutorialId") Long tutorialId) {
        if (!tutorialRepository.existsById(tutorialId)) {
            return ResponseEntity.notFound().build();
        }
        List<Tag> tags = tagRepository.findTagsByTutorialsId(tutorialId);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable(value = "id") Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        return ResponseEntity.ok().body(tag);
    }

    @GetMapping("/tags/{tagId}/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorialsByTagId(@PathVariable(value = "tagId") Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            return ResponseEntity.notFound().build();
        }
        List<Tutorial> tutorials = tutorialRepository.findTutorialsByTagsId(tagId);
        return new ResponseEntity<>(tutorials, HttpStatus.OK);
    }

    @PostMapping("/tutorials/{tutorialId}/tags")
    public ResponseEntity<Tag> addTag(@PathVariable(value = "tutorialId") Long tutorialId, @RequestBody Tag tagRequest) {
        Tag tag = tutorialRepository.findById(tutorialId).map(tutorial -> {
            Long tagId = tagRequest.getId();
            if (tagId != null) {
                Tag tagToUpdate = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + tagId));
                tutorial.addTag(tagToUpdate);
                tutorialRepository.save(tutorial);
                return tagToUpdate;
            }
            tutorial.addTag(tagRequest);
            return tagRepository.save(tagRequest);
        }).orElseThrow(() -> new IllegalArgumentException("Invalid tutorial Id:" + tutorialId));
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<Tag> updateTag(@PathVariable(value = "tagId") Long tagId, @RequestBody Tag tagRequest) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + tagId));
        tag.setName(tagRequest.getName());
        return new ResponseEntity<>(tagRepository.save(tag), HttpStatus.OK);
    }


    @DeleteMapping("tutorials/{tutorialId}/tags/{tagId}")
    public ResponseEntity<HttpStatus> deleteTagFromTutorial(@PathVariable(value = "tutorialId") Long tutorialId, @PathVariable(value = "tagId") Long tagId) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId).orElseThrow(() -> new IllegalArgumentException("Invalid tutorial Id:" + tutorialId));
        tutorial.removeTag(tagId);
        tutorialRepository.save(tutorial);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<HttpStatus> deleteTag(@PathVariable(value = "id") Long id) {
        tagRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
