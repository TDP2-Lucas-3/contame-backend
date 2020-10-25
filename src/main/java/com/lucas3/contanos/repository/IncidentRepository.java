package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends CrudRepository<Incident, Long> {

    List<Incident> findAll();

    Integer countByUser(User user);
}