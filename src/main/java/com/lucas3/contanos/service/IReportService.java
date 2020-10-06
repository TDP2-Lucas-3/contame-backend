package com.lucas3.contanos.service;

import com.lucas3.contanos.entity.Report;
import com.lucas3.contanos.model.ReportRequest;
import com.lucas3.contanos.model.exception.ReportNotFoundException;

import java.util.List;

public interface IReportService {

    Report createReport(ReportRequest request);

    List<Report> getAllReports();

    Report getReportById(Long id) throws ReportNotFoundException;

}
