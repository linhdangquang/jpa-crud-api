package com.example.jpacrudapi.repository;

import com.example.jpacrudapi.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
