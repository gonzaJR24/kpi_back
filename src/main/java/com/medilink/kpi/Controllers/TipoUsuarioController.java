package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.TipoUsuarioService;
import com.medilink.kpi.entities.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tipo_usuario")
@CrossOrigin
public class TipoUsuarioController {
    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @PostMapping("/save")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@PathVariable int id){
        tipoUsuarioService.deleteById(id);
        Map<String, String>response=new HashMap<>();
        response.put("response","deleted successfully");
        return ResponseEntity.status(200).body(response);
    }

}

