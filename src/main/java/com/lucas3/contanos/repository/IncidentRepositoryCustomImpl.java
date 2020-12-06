package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.EIncidentCategory;
import com.lucas3.contanos.entities.EIncidentStatePublic;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.filters.DataFilter;
import com.lucas3.contanos.model.filters.IncidentFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IncidentRepositoryCustomImpl implements  IncidentRepositoryCustom {

    @Autowired
    EntityManager em;

    @Override
    public List<Incident> findAll(IncidentFilter filter) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Incident> cq = cb.createQuery(Incident.class);

        Root<Incident> incident = cq.from(Incident.class);
        List<Predicate> predicates = new ArrayList<>();

        if (!filter.getHood().isEmpty()) {
            predicates.add(cb.equal(incident.get("hood"), filter.getHood()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();

    }

    @Override
    public Integer countByState(DataFilter filter, EIncidentStatePublic state) {
        return null;
    }
}
