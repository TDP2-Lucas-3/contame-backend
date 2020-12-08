package com.lucas3.contanos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mobile")
public class MobileController {

    @GetMapping(value= "/report")
    public String redirect(@RequestParam String id){
        return "redirect";
    }



}
