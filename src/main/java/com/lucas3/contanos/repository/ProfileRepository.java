package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
