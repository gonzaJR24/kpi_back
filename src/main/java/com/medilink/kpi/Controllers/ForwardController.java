package com.medilink.kpi.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
public class ForwardController {
  @RequestMapping(value = "/{path:[^\\.]*}")
  public String redirect() {
    // Redirige a index.html
    return "forward:/index.html";
  }
}
