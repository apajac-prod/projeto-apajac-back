package com.apajac.acolhimento.controllers.usuario;

import com.apajac.acolhimento.domain.dtos.LoginDTO;
import com.apajac.acolhimento.domain.dtos.UsuarioLogadoDTO;
import com.apajac.acolhimento.services.interfaces.LoginUsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/login")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class LoginUsuarioController {

    private final LoginUsuarioService loginUsuarioService;
    @PostMapping
    public ResponseEntity<UsuarioLogadoDTO> loginUsuario(@RequestBody LoginDTO loginDTO) {

        UsuarioLogadoDTO usuarioLogado = loginUsuarioService.login(loginDTO);

        return ResponseEntity.ok(usuarioLogado);
    }
}
