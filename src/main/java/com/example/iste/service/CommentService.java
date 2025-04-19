package com.example.iste.service;

import com.example.iste.entity.Comment;
import com.example.iste.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment saveComment(String content, int stars,String username) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setStars(stars);
        comment.setUsername(username);
        return commentRepository.save(comment);
    }

    public Comment findByUsername(String username) {
        return commentRepository.findByUsername(username);  // username ile randevu sorgulama
    }
}
