package com.lucas3.contanos.service;

import com.lucas3.contanos.entity.Report;
import com.lucas3.contanos.model.ReportRequest;
import com.lucas3.contanos.model.exception.ReportNotFoundException;
import com.lucas3.contanos.repository.ReportRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ReportServiceTests {
    @Autowired
    private ReportService reportService;

    @Test
    public void createTest(){
        ReportRequest request = new ReportRequest("Prueba", "Descripcion");
        Report report = new Report("Prueba", "Descripcion");
        report.setId(1L);
        Report report1 = reportService.createReport(request);
        Assert.assertEquals(report.getTitle(),report1.getTitle());
        Assert.assertEquals(report.getDescription(),report1.getDescription());
        Assert.assertEquals(report.getId(),report1.getId());
    }

    @Test
    public void findAllTest(){
        ReportRequest request = new ReportRequest("Prueba", "Descripcion");
        Report report1 = reportService.createReport(request);
        Report report2 = reportService.createReport(request);
        Report report3 = reportService.createReport(request);
        List<Report> reports = reportService.getAllReports();
        Assert.assertTrue(reports.size() == 3);
    }

    @Test
    public void findByIdTest(){
        Report result = null;
        ReportRequest request = new ReportRequest("Prueba", "Descripcion");
        ReportRequest request3 = new ReportRequest("Prueba3", "Descripcion3");
        Report report1 = reportService.createReport(request);
        Report report2 = reportService.createReport(request);
        Report report3 = reportService.createReport(request3);
        try{
            result = reportService.getReportById(3L);
        } catch (ReportNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(request3.getTitle(), result.getTitle());
        Assert.assertEquals(request3.getDescription() , request3.getDescription());

    }

}
