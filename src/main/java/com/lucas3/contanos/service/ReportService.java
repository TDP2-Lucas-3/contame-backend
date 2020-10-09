package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Report;
import com.lucas3.contanos.model.request.ReportRequest;
import com.lucas3.contanos.model.exception.ReportNotFoundException;
import com.lucas3.contanos.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService implements IReportService{

    @Autowired
    private ReportRepository reportRepository;


    @Override
    public Report createReport(ReportRequest request) {
        Report report = new Report(request.getTitle(), request.getDescription());
        reportRepository.save(report);
        return report;
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public Report getReportById(Long id) throws ReportNotFoundException {
        Optional<Report> report = reportRepository.findById(id);
        if(report.isPresent()){
            return report.get();
        }
        throw new ReportNotFoundException("Reporte inexistente");
    }
}
