package com.lucas3.contanos.controller;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.Report;
import com.lucas3.contanos.model.exception.InvalidCategoryException;
import com.lucas3.contanos.model.exception.InvalidReportException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.ReportRequest;
import com.lucas3.contanos.model.response.StandResponse;
import com.lucas3.contanos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
        Report response = null;
        try{
            validateReport(request);
            response = reportService.createReport(request);
        }catch(Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new StandResponse("Hubo un error creando tu reporte, Por favor intenta devuelta en unos minutos"));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public List<Report> getReports(){
       return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  getReportById(@PathVariable Long id)  {
        try{
            return ResponseEntity.ok(reportService.getReportById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("El reporte solicitado no existe"));
        }
    }
    @GetMapping("/categories")
    public List<Category> getCategories(){
        return reportService.getCategories();
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        Category category = null;
        try{
            validateCategory(categoryRequest);
            category = reportService.createCategory(categoryRequest);
            return ResponseEntity.ok(category);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(new StandResponse("No se pudo crear la categoria"));
        }
    }

    private void validateCategory(CategoryRequest request) throws InvalidCategoryException {
        if(request.getName() == null || StringUtils.isEmpty(request.getName())) throw new InvalidCategoryException();
        if(request.getDescription() == null || StringUtils.isEmpty(request.getDescription())) throw new InvalidCategoryException();
    }

    private void validateReport(ReportRequest request) throws InvalidReportException {
        if(request.getTitle() == null || StringUtils.isEmpty(request.getTitle())) throw new InvalidReportException();
        if(request.getCategory() == null || StringUtils.isEmpty(request.getCategory())) throw new InvalidReportException();
    }




}
