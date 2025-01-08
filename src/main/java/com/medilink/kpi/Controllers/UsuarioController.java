package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.SessionService;
import com.medilink.kpi.Services.SucursalService;
import com.medilink.kpi.Services.TipoUsuarioService;
import com.medilink.kpi.Services.UsuarioService;
import com.medilink.kpi.entities.Usuario;
import com.medilink.kpi.entities.dto.UsuarioDTO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();

        if (usuarioDTO.nombres().isEmpty() || usuarioDTO.apellidos().isEmpty() || usuarioDTO.nombreUsuario().isEmpty()
                || usuarioDTO.contrasena().isEmpty() || usuarioDTO.tipoUsuario() == 0 || usuarioDTO.sucursal() == 0) {

            return ResponseEntity.status(400).body("Invalid or empty value");
        }

        usuario.setNombres(usuarioDTO.nombres());
        usuario.setApellidos(usuarioDTO.apellidos());
        usuario.setNombreUsuario(usuarioDTO.nombreUsuario());
        usuario.setContrasena(usuarioDTO.contrasena());
        usuario.setTipoUsuario(tipoUsuarioService.findById(usuarioDTO.tipoUsuario()));
        usuario.setSucursal(sucursalService.findById(usuarioDTO.sucursal()));
        usuario.setFechaCreacion(LocalDate.now());

        usuarioService.save(usuario);
        return ResponseEntity.status(201).body("created successfully");
    }

    @GetMapping
    public List<Usuario> list(){
        return usuarioService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id){
        return ResponseEntity.status(200).body(usuarioService.findById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteById(@PathVariable int id){
        if((id == 0)){
            return ResponseEntity.status(203).body("Include a valid id");
        }

        usuarioService.deleteById(id);
        return ResponseEntity.status(200).body("deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?>edit(@RequestBody UsuarioDTO usuarioDTO, @PathVariable int id){
        Usuario usuario=usuarioService.findById(id);
        if(usuario==null){
            return ResponseEntity.status(404).body("User not found");
        }
        usuario.setNombres(usuarioDTO.nombres());
        usuario.setApellidos(usuarioDTO.apellidos());
        usuario.setNombreUsuario(usuarioDTO.nombreUsuario());
        usuario.setContrasena(usuarioDTO.contrasena());
        usuario.setTipoUsuario(tipoUsuarioService.findById(usuarioDTO.tipoUsuario()));
        usuario.setSucursal(sucursalService.findById(usuarioDTO.sucursal()));
        usuarioService.save(usuario);

        return ResponseEntity.status(200).body(usuario);
    }

    private static final ConcurrentHashMap<String, Usuario> sessionStore = new ConcurrentHashMap<>();

    // Modify the login method to include user details in the response
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO usuarioDTO) {
        if (usuarioDTO.nombreUsuario() == null || usuarioDTO.contrasena() == null) {
            return ResponseEntity.status(400).body("Username and password must not be empty");
        }

        Usuario usuario = usuarioService.findByNombreUsuario(usuarioDTO.nombreUsuario());
        if (usuario == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (!usuario.getContrasena().equals(usuarioDTO.contrasena())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        // Generate a unique token for the user
        String token = UUID.randomUUID().toString();
        sessionService.storeSession(token, usuario);

        return ResponseEntity.ok(Map.of("token", token, "userName", usuario.getNombreUsuario(), "tipoUsuario", usuario.getTipoUsuario().getTipoUsuario()));
    }



    @GetMapping("/secure-endpoint")
    public ResponseEntity<?> secureEndpoint(@RequestHeader("Session-Token") String sessionToken) {
        Usuario usuario = sessionStore.get(sessionToken);
        if (usuario == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Handle the secure request
        return ResponseEntity.ok("Access granted for user: " + usuario.getNombreUsuario());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        String sessionToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        if (sessionService.removeSession(sessionToken)) {
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(401).body("Invalid session token");
        }
    }



}

