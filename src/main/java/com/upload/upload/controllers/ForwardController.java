package com.upload.upload.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForwardController {

    @RequestMapping(value = "/**/{path:[^\\.]*}")
    public String redirect() {
        // Redirige todas las ruts desconocidas al index.html de Angular
        return "forward:/nomina.html";
    }
}
