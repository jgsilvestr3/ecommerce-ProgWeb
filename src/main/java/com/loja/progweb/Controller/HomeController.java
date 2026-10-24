package com.loja.progweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller 
@RequestMapping("/")
public class HomeController {

    @GetMapping 
    public ModelAndView home(){
        
        return new ModelAndView("home");
    }
    
    @GetMapping("login")
    public String login(){
        return "login";
    } 
}
