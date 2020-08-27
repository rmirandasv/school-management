package com.rmiranda.schoolmanagement.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {
    
    @GetMapping(value = {"home", ""})
    public ModelAndView home(ModelAndView mv){
        mv.setViewName("home");
        return mv;
    }
}