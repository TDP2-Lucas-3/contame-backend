package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category,Long> {

    List<Category> findAll();

    Optional<Category> findByName(String name);

    Optional<Category> findById(Long id);
}
