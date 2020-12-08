package com.lucas3.contanos.controller;

import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.model.exception.IncidentNotFoundException;
import com.lucas3.contanos.model.exception.UserNotFoundException;
import com.lucas3.contanos.service.IIncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mobile")
public class MobileController {

    @Autowired
    private IIncidentService incidentService;

    private static String IMAGE_URL_SHARING = "https://i.ibb.co/BrWnB16/Logo-BA.png";

    @GetMapping(value= "/report")
    public String redirect(@RequestParam String reportId, Model model){
        Long id = Long.parseLong(reportId);
        try {
            Incident inc = incidentService.getIncidentById(id);
            model.addAttribute("title",inc.getTitle());
            model.addAttribute("description", inc.getDescription());
            if(inc.getImages().isEmpty()){
                model.addAttribute("image", IMAGE_URL_SHARING);
            }else{
                model.addAttribute("image", inc.getImages().get(0));
            }

        } catch (IncidentNotFoundException e) {
            model.addAttribute("title","Incidente no encontrado");
            model.addAttribute("description", "No pudimos encontrar el incidente indicado");
            model.addAttribute("image", IMAGE_URL_SHARING);
        }
        return "redirect";
    }



}
