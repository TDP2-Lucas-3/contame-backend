package com.lucas3.contanos.controller;

import com.lucas3.contanos.entities.Report;
import com.lucas3.contanos.model.request.ReportRequest;
import com.lucas3.contanos.model.response.ReportResponse;
import com.lucas3.contanos.model.exception.ReportNotFoundException;
import com.lucas3.contanos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("")
    public ReportResponse createReport(@RequestBody ReportRequest request) {
        ReportResponse response = null;
        response = new ReportResponse(reportService.createReport(request));
        return response;
    }

    @GetMapping("")
    public List<ReportResponse> getReports(){
        List<ReportResponse> response = new ArrayList<ReportResponse>();
        for (Report report: reportService.getAllReports() ) {
            response.add(new ReportResponse(report));
        }
        return response;
    }
    @GetMapping("/{id}")
    public ReportResponse getReportById(@PathVariable Long id) throws ReportNotFoundException {
        return new ReportResponse(reportService.getReportById(id));
    }




}
