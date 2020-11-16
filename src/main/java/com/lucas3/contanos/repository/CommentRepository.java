package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Comment;
import com.lucas3.contanos.entities.ECommentCategory;
import com.lucas3.contanos.entities.Incident;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment,Long> {

    List<Comment> findAllByIncident(Incident incident);

    List<Comment> findAllByIncidentAndCategory(Incident incident, ECommentCategory category);
}
