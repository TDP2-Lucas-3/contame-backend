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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Incident> cq = cb.createQuery(Incident.class);

        Root<Incident> incident = cq.from(Incident.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getHood() != null && !filter.getHood().isEmpty()) {
            predicates.add(cb.equal(incident.get("hood"), filter.getHood()));
        }
/*
        if(filter.getCreateDate() != null && !filter.getCreateDate().isEmpty()){
            SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
            String myDate = "17-04-2011";
            // Create date 17-04-2011 - 00h00
            Date minDate = formatter.parse(filter.getCreateDate());
            // Create date 18-04-2011 - 00h00
            // -> We take the 1st date and add it 1 day in millisecond thanks to a useful and not so known class
            Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
            Conjunction and = Restrictions.conjunction();
            // The order date must be >= 17-04-2011 - 00h00
            and.add( Restrictions.ge("orderDate", minDate) );
            // And the order date must be < 18-04-2011 - 00h00
            and.add( Restrictions.lt("orderDate", maxDate) );
            predicates.add(cb.ge(incident.get("createDate"),maxDate));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.where(an)

 */

        return em.createQuery(cq).getResultList().size();

    }

    @Override
    public Integer countByStateAndCategory(DataFilter filter, EIncidentStatePublic state, EIncidentCategory category) {
        return null;
    }


}
