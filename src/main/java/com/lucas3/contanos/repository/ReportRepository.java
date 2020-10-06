package com.lucas3.contanos.repository;

import com.lucas3.contanos.entity.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {

    List<Report> findAll();
}
