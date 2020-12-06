package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.EIncidentCategory;
import com.lucas3.contanos.entities.EIncidentStatePublic;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.filters.DataFilter;
import com.lucas3.contanos.model.filters.IncidentFilter;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    public Integer countByCategory(DataFilter filter, EIncidentCategory category) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Incident> cq = cb.createQuery(Incident.class);

        Root<Incident> incident = cq.from(Incident.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getHood() != null && !filter.getHood().isEmpty()) {
            predicates.add(cb.equal(incident.get("hood"), filter.getHood()));
        }

        if(filter.getCreateDate() != null && !filter.getCreateDate().isEmpty()){
            Date minDate = formatter.parse(filter.getCreateDate());
            Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
            predicates.add(cb.greaterThanOrEqualTo(incident.get("creationDate"),minDate));
            predicates.add(cb.lessThanOrEqualTo(incident.get("creationDate"), maxDate));
        }

        if(filter.getCompleteDate() != null && !filter.getCompleteDate().isEmpty()){
            Date minDate = formatter.parse(filter.getCompleteDate());
            Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
            predicates.add(cb.greaterThanOrEqualTo(incident.get("completeDate"),minDate));
            predicates.add(cb.lessThanOrEqualTo(incident.get("completeDate"), maxDate));
        }

        predicates.add(cb.equal(incident.get("category"), category));

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList().size();

    }

    @Override
    public Integer countByStateAndCategory(DataFilter filter, EIncidentStatePublic state, EIncidentCategory category) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Incident> cq = cb.createQuery(Incident.class);

        Root<Incident> incident = cq.from(Incident.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getHood() != null && !filter.getHood().isEmpty()) {
            predicates.add(cb.equal(incident.get("hood"), filter.getHood()));
        }

        if(filter.getCreateDate() != null && !filter.getCreateDate().isEmpty()){
            Date minDate = formatter.parse(filter.getCreateDate());
            Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
            predicates.add(cb.greaterThanOrEqualTo(incident.get("creationDate"),minDate));
            predicates.add(cb.lessThanOrEqualTo(incident.get("creationDate"), maxDate));
        }

        if(filter.getCompleteDate() != null && !filter.getCompleteDate().isEmpty()){
            Date minDate = formatter.parse(filter.getCompleteDate());
            Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
            predicates.add(cb.greaterThanOrEqualTo(incident.get("completeDate"),minDate));
            predicates.add(cb.lessThanOrEqualTo(incident.get("completeDate"), maxDate));
        }

        predicates.add(cb.equal(incident.get("category"), category));
        predicates.add(cb.equal(incident.get("state"),state));

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList().size();

    }


}
