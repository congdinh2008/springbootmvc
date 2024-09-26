package com.congdinh.springbootmvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

// Mark this class as a controller
@Controller
@RequestMapping("/") // http://localhost:8080/
public class HomeController {
    // http://localhost:8080/ or http://localhost:8080/index or
    // http://localhost:8080/index.html
    @GetMapping
    public String index(
            @RequestParam(name = "name", required = false, defaultValue = "User") String name,
            @RequestParam(name = "age", required = false, defaultValue = "0") int age,
            Model model) {
        // Model - transfer data from controller to view
        model.addAttribute("message",
                "Hello " + name + ", this is the home page of Spring Boot MVC.");
        model.addAttribute("age", age);
        return "home/index";
    }

    // http://localhost:8080/about
    @GetMapping("/about")
    public ModelAndView about(ModelAndView modelAndView) {
        // ModelAndView - transfer data from controller to view
        modelAndView.setViewName("home/about");
        modelAndView.addObject("message",
                "This is the about page of Spring Boot MVC.");
        return modelAndView;
    }

    // http://localhost:8080/contact
    @GetMapping("/contact")
    public String contact(ModelMap modelMap) {
        // ModelMap - transfer data from controller to view
        modelMap.addAttribute("message",
                "This is the contact page of Spring Boot MVC.");
        return "home/contact";
    }
}
