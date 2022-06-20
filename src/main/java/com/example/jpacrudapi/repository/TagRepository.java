package com.example.jpacrudapi.repository;

import com.example.jpacrudapi.model.Tag;
import com.example.jpacrudapi.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByTutorialsId(Long tutorialId);

}
