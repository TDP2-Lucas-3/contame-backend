package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.Category;
import com.lucas3.contanos.entities.EIncidentState;
import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.FailedToLoadImageException;
import com.lucas3.contanos.model.request.CategoryRequest;
import com.lucas3.contanos.model.request.IncidentRequest;
import com.lucas3.contanos.model.exception.IncidentNotFoundException;
import com.lucas3.contanos.repository.CategoryRepository;
import com.lucas3.contanos.repository.IncidentRepository;
import com.lucas3.contanos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class IncidentService implements IIncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImgbbService imgbbService;


    @Override
    public Incident createIncident(IncidentRequest request, String email) throws FailedToLoadImageException {
        Optional<Category> category = categoryRepository.findById(request.getCategory());

        Incident incident = new Incident(request.getTitle(),category.get(),request.getDescription(), request.getLat(), request.getLon());

        List<String> imagesURLs = new ArrayList<>();
        if(request.getImages() != null){
            for (String imageBase64:request.getImages()) {
                imagesURLs.add(imgbbService.uploadImgToImgbb(imageBase64));
            }
            incident.setImages(imagesURLs);
        }
        incident.setUser(userRepository.findByEmail(email).get());
        incident.setState(EIncidentState.REPORTADO);
        incidentRepository.save(incident);
        return incident;
    }

    @Override
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Override
    public List<Incident> getAllIncidentsByUser(String email) {
        return incidentRepository.findAllByUser(userRepository.findByEmail(email).get());
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
        List<Category> categories = categoryRepository.findAll() ;
        Collections.sort(categories, Comparator.comparing(Category::getName));
        return categories;
    }

    @Override
    public Category createCategory(CategoryRequest request) {
        return categoryRepository.save(new Category(request));
    }


}
