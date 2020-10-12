package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Report;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.ReportRequest;
import com.lucas3.contanos.model.exception.ReportNotFoundException;
import com.lucas3.contanos.model.response.imgbb.UploadImageResponse;
import com.lucas3.contanos.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService implements IReportService{

    @Autowired
    private ReportRepository reportRepository;

    @Value("${contame.app.imgbb.client}")
    private String clientIdImgbb;


    @Override
    public Report createReport(ReportRequest request) throws IOException, FailedToLoadImageException {
        Report report = new Report(request.getTitle(), request.getDescription(), request.getLocation());
        if(request.getImage() != null){
            report.setImage(uploadImgToImgur(request.getImage()));
        }

        reportRepository.save(report);
        return report;
    }

    private String uploadImgToImgur(MultipartFile image) throws IOException, FailedToLoadImageException {
        RestTemplate restTemplate = new RestTemplate();
        String url= "https://api.imgbb.com/1/upload?key="+ clientIdImgbb;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("image", Base64.getEncoder().encodeToString(image.getBytes()));
        map.add("key", clientIdImgbb);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<UploadImageResponse> result = restTemplate.postForEntity(url, request, UploadImageResponse.class);
        if (result.getBody().isSuccess()) {
            return result.getBody().getData().getUrl();
        } else {
            throw new FailedToLoadImageException();

        }
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
