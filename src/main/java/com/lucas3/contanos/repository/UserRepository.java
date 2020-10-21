package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.ERole;
import com.lucas3.contanos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findAllByRol(ERole role);

}
