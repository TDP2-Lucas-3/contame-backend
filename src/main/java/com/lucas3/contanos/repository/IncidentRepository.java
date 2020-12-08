package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.EIncidentCategory;
import com.lucas3.contanos.entities.EIncidentStatePublic;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends CrudRepository<Incident, Long>, IncidentRepositoryCustom{

    List<Incident> findAll();

    List<Incident> findAllByUser(User user);

    List<Incident> findAllByCategory(EIncidentCategory category);

    Integer countByUser(User user);

    List<Incident> findAllByParent(Incident parent);

    Integer countByCategory(EIncidentCategory category);

    Integer countByCategoryAndHood(EIncidentCategory category, String hood);

    Integer countByHood(String hood);

    Integer countByStateAndCategory(EIncidentStatePublic state, EIncidentCategory category);

    @Query("SELECT DISTINCT i.hood FROM Incident i")
    List<String> findDistinctHood();
}
