package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Report;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.ReportRequest;
import com.lucas3.contanos.model.exception.ReportNotFoundException;
import org.junit.Assert;
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
        ReportRequest request = new ReportRequest("Prueba", "Descripcion", "CABA", null);
        Report report = new Report("Prueba", "Descripcion", "CABA");
        report.setId(1L);
        Report report1 = reportService.createReport(request);
        Assert.assertEquals(report.getTitle(),report1.getTitle());
        Assert.assertEquals(report.getDescription(),report1.getDescription());
        Assert.assertEquals(report.getId(),report1.getId());
    }

    @Test
    public void findAllTest() throws IOException, FailedToLoadImageException {
        ReportRequest request = new ReportRequest("Prueba", "Descripcion", "CABA", null);
        Report report1 = reportService.createReport(request);
        Report report2 = reportService.createReport(request);
        Report report3 = reportService.createReport(request);
        List<Report> reports = reportService.getAllReports();
        Assert.assertTrue(reports.size() == 3);
    }

    @Test
    public void findByIdTest() throws IOException, FailedToLoadImageException {
        Report result = null;
        ReportRequest request = new ReportRequest("Prueba", "Descripcion", "CABA", null);
        ReportRequest request3 = new ReportRequest("Prueba3", "Descripcion3", "CABA", null);
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
