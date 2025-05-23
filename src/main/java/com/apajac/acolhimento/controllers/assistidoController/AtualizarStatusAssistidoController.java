package com.apajac.acolhimento.controllers.assistidoController;

import com.apajac.acolhimento.services.interfaces.AtualizarStatusAssistidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/assistido")
@Tag(name = "Assistidos", description = "Endpoints para gerenciamento de assistidos")
public class AtualizarStatusAssistidoController {

    private final AtualizarStatusAssistidoService statusAssistidoService;

    @PutMapping("/{id}/status/{id_responsavel}")
    public ResponseEntity<String> updateStatusAssistido(
            @PathVariable("id") Long id,
            @PathVariable("id_responsavel") Long id_responsavel) {
        statusAssistidoService.updateStatusAssistido(id, id_responsavel);
        return new ResponseEntity<>("Status alterado com sucesso!", HttpStatus.NO_CONTENT);
    }
}
