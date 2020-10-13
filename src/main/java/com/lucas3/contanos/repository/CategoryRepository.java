package com.lucas3.contanos.repository;

import com.lucas3.contanos.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Long> {

    List<Category> findAll();

    Category findByName(String name);
}
