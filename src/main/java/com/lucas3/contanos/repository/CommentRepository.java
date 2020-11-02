package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment,Long> {
}
