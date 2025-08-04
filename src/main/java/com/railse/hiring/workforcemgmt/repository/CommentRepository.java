package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findByTaskIdOrderByTimestamp(Long taskId);
    List<Comment> findAll();
}