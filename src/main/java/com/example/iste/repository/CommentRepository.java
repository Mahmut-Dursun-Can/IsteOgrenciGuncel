package com.example.iste.repository;

import com.example.iste.entity.Appointment;
import com.example.iste.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByUsername(String username);
}
