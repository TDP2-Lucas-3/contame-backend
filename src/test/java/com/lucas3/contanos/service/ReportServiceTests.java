package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.Report;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.ReportRequest;
import com.lucas3.contanos.model.exception.ReportNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

// Test del servicio de reportes
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ReportServiceTests {
    @Autowired
    private ReportService reportService;


    @Test
    public void createTest() throws IOException, FailedToLoadImageException {
        Category category = reportService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        ReportRequest request = new ReportRequest("Prueba", "ROBO");
        Report report = new Report("Prueba", category);
        report.setId(1L);
        Report report1 = reportService.createReport(request);
        Assert.assertEquals(report.getTitle(),report1.getTitle());
        Assert.assertEquals(report.getDescription(),report1.getDescription());
        Assert.assertEquals(report.getId(),report1.getId());
    }

    @Test
    public void findAllTest() throws IOException, FailedToLoadImageException {
        Category category = reportService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        ReportRequest request = new ReportRequest("Prueba", "ROBO");
        Report report1 = reportService.createReport(request);
        Report report2 = reportService.createReport(request);
        Report report3 = reportService.createReport(request);
        List<Report> reports = reportService.getAllReports();
        Assert.assertTrue(reports.size() == 3);
    }

    @Test
    public void findByIdTest() throws IOException, FailedToLoadImageException {
        Category category = reportService.createCategory(new CategoryRequest("ROBO", "Incidnecia de robo"));
        Report result = null;
        ReportRequest request = new ReportRequest("Prueba", "ROBO");
        ReportRequest request3 = new ReportRequest("Prueba3", "ROBO");
        Report report1 = reportService.createReport(request);
        Report report2 = reportService.createReport(request);
        Report report3 = reportService.createReport(request3);
        try{
            result = reportService.getReportById(3L);
        } catch (ReportNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(request3.getTitle(), result.getTitle());
        Assert.assertEquals(request3.getCategory() , result.getCategory().getName());
    }

    @Test
    public void createCategory(){
        Category category = reportService.createCategory(new CategoryRequest("Robo", "Incidencia de robo"));
        Assert.assertEquals(category.getName(),"ROBO");
        Assert.assertEquals(category.getDescription(),"Incidencia de robo");
    }

    @Test
    public void createCategoryRepeat(){
        try{
            Category category1 = reportService.createCategory(new CategoryRequest("Robo", "Incidencia de robo"));
            Category category2 = reportService.createCategory(new CategoryRequest("Robo", "Incidencia de robo"));
        }catch(Exception e){

        }
        List<Category> categories = reportService.getCategories();
        Assert.assertEquals(categories.size(), 1);
    }

}
