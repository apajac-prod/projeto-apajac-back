package com.apajac.acolhimento.controllers.usuario;

import com.apajac.acolhimento.domain.dtos.UsuarioDTO;
import com.apajac.acolhimento.services.interfaces.AtualizarDadosUsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/usuario")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class AtualizarDadosUsuarioController {

    private final AtualizarDadosUsuarioService atualizarDadosUsuarioService;
    @PutMapping("/{id}/atualizar_dados")
    public ResponseEntity<String> updateStatusUsuario(@PathVariable("id") Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        atualizarDadosUsuarioService.updateDadosUsuario(id, usuarioDTO);
        return new ResponseEntity<>("Status alterado com sucesso!", HttpStatus.NO_CONTENT);
    }
}
