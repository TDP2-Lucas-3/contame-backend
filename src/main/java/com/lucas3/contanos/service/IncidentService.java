package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.exception.IncidentNotFoundException;
import com.lucas3.contanos.model.response.imgbb.UploadImageResponse;
import com.lucas3.contanos.repository.CategoryRepository;
import com.lucas3.contanos.repository.IncidentRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService implements IIncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${contame.app.imgbb.client}")
    private String clientIdImgbb;


    @Override
    public Incident createIncident(IncidentRequest request) throws FailedToLoadImageException {
        Category category = categoryRepository.findByName(request.getCategory().toUpperCase());
        Incident incident = new Incident(request.getTitle(),category,request.getDescription(), request.getLat(), request.getLon());

        List<String> imagesURLs = new ArrayList<>();
        if(request.getImages() != null){
            for (String imageBase64:request.getImages()) {
                imagesURLs.add(uploadImgToImgbb(imageBase64));
            }
            incident.setImages(imagesURLs);
        }

        incidentRepository.save(incident);
        return incident;
    }

    private String uploadImgToImgbb(String image) throws FailedToLoadImageException {
        RestTemplate restTemplate = new RestTemplate();
        String url= "https://api.imgbb.com/1/upload?key="+ clientIdImgbb;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("image", image);
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
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    public Incident getIncidentById(Long id) throws IncidentNotFoundException {
        Optional<Incident> incident = incidentRepository.findById(id);
        if(incident.isPresent()){
            return incident.get();
        }
        throw new IncidentNotFoundException("Reporte inexistente");
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        return categoryRepository.save(new Category(request));
    }


}
