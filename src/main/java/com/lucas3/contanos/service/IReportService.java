package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Report;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.ReportRequest;
import com.lucas3.contanos.model.exception.ReportNotFoundException;

import java.io.IOException;
import java.util.List;

public interface IReportService {

    Report createReport(ReportRequest request) throws FailedToLoadImageException;

    List<Report> getAllReports();

    Report getReportById(Long id) throws ReportNotFoundException;

}
