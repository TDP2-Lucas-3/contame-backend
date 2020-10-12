package com.lucas3.contanos.controller;

import com.lucas3.contanos.entities.Report;
import com.lucas3.contanos.model.request.ReportRequest;
import com.lucas3.contanos.model.response.ReportResponse;
import com.lucas3.contanos.model.exception.ReportNotFoundException;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping(value= "")
    public ResponseEntity<?> createReport(@RequestBody ReportRequest request) {
        ReportResponse response = null;
        try{
            response = new ReportResponse(reportService.createReport(request));
        }catch(Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("Hubo un error creando tu reporte, Por favor intenta devuelta en unos minutos"));
        }
        return ResponseEntity.ok(response);
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
    public ResponseEntity<?>  getReportById(@PathVariable Long id)  {
        try{
            return ResponseEntity.ok(new ReportResponse(reportService.getReportById(id)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("El reporte solicitado no existe"));
        }
    }




}
