package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.AreaService;
import com.medilink.kpi.Services.SucursalService;
import com.medilink.kpi.Services.TipoUsuarioService;
import com.medilink.kpi.Services.UsuarioService;
import com.medilink.kpi.entities.Usuario;
import com.medilink.kpi.entities.dto.UsuarioDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @Autowired
    private AreaService areaService;

    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.list();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UsuarioDTO usuarioDTO){
      Usuario usuario=new Usuario();
      usuario.setNombres(usuarioDTO.nombres());
      usuario.setApellidos(usuarioDTO.apellidos());
      usuario.setSucursal(sucursalService.findById(usuarioDTO.sucursal()));
      usuario.setTipoUsuario(tipoUsuarioService.findById(usuarioDTO.tipoUsuario()));
      usuario.setNombreUsuario(usuarioDTO.nombreUsuario());
      usuario.setContrasena(usuarioDTO.contrasena());
      usuario.setFechaCreacion(LocalDate.now());
      usuario.setArea(areaService.findById(usuarioDTO.area()).getNombreArea());
      usuarioService.save(usuario);
      return ResponseEntity.status(201).body(usuarioDTO);
    }


    @PutMapping("{id}")
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody UsuarioDTO usuarioDTO){
      Usuario existing=usuarioService.findById(id);
      existing.setNombres(usuarioDTO.nombres());
      existing.setApellidos(usuarioDTO.apellidos());
      existing.setSucursal(sucursalService.findById(usuarioDTO.sucursal()));
      existing.setTipoUsuario(tipoUsuarioService.findById(usuarioDTO.tipoUsuario()));
      existing.setNombreUsuario(usuarioDTO.nombreUsuario());
      existing.setContrasena(usuarioDTO.contrasena());
      usuarioService.save(existing);
      return ResponseEntity.status(200).body(existing);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable int id){
      usuarioService.deleteById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            session.setAttribute("user", authentication.getPrincipal());
            Map<String, Object> response = new HashMap<>();
            response.put("username", usuarioService.findByNombreUsuario(loginRequest.getUsername()));
            response.put("authenticated", true);
            return ResponseEntity.status(200).body(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(400).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logout successful";
    }

    @GetMapping("/current")
    public Usuario getCurrentUser(HttpSession session) {
        return (Usuario) session.getAttribute("user");
    }
}

class LoginRequest {
    private String username;
    private String password;

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
