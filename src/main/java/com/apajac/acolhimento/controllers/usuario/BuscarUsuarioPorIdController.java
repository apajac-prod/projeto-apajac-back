package com.apajac.acolhimento.controllers.usuario;

import com.apajac.acolhimento.domain.dtos.UsuarioSemSenhaDTO;
import com.apajac.acolhimento.domain.entities.UsuarioEntity;
import com.apajac.acolhimento.mappers.UsuarioMapper;
import com.apajac.acolhimento.services.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/usuario")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class BuscarUsuarioPorIdController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    @GetMapping("/por_id/{id}")
    public ResponseEntity<UsuarioSemSenhaDTO> buscarUsuario(@PathVariable("id") Long id){
        try {
            UsuarioEntity usuario = usuarioService.buscarUsuarioPorId(id);

            UsuarioSemSenhaDTO usuarioSemSenhaDTO = usuarioMapper.convertEntityToDto(usuario);

            return ResponseEntity.status(HttpStatus.OK).body(usuarioSemSenhaDTO);
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode(), e.getMessage());
        }
    }
}
