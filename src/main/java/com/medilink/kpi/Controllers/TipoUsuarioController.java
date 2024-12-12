package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.TipoUsuarioService;
import com.medilink.kpi.entities.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipoUsuario")
public class TipoUsuarioController {
    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TipoUsuario tipoUsuario) {
        if (tipoUsuario.getTipoUsuario().isEmpty()) {
            return ResponseEntity.status(404).body("tipo usuario cannot be empty");
        }
        tipoUsuarioService.save(tipoUsuario);
        return ResponseEntity.status(201).body(tipoUsuario);
    }

    @GetMapping
    public List<TipoUsuario>list(){
        return tipoUsuarioService.list();
    }

}

