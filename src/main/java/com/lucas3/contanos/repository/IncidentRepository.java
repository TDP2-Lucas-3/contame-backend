package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends CrudRepository<Incident, Long>, IncidentRepositoryCustom{

    List<Incident> findAll();

    List<Incident> findAllByUser(User user);

    Integer countByUser(User user);

    List<Incident> findAllByFather(Incident father);
}
