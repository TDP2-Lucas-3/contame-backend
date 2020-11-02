package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.entities.User;
import com.lucas3.contanos.entities.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote,Long> {

    Integer countByIncident(Incident incident);

    Optional<Vote> findByUserAndIncident(User user, Incident incident);
}
