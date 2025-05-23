package com.apajac.acolhimento.controllers.enderecoController;

import com.apajac.acolhimento.domain.dtos.EnderecoDTO;
import com.apajac.acolhimento.gateway.EnderecoIntegration;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/endereco")
@Tag(name = "Endereço", description = "Endpoints para busca de endereços")
public class BuscarEnderecoPorCEPController {

    private final EnderecoIntegration integration;
    @GetMapping("/{cep}")
    public ResponseEntity<EnderecoDTO> buscarEndereco(@PathVariable("cep") String cep){
        try {
            EnderecoDTO enderecoDTO = integration.enderecoPorCep(cep);
            return ResponseEntity.status(HttpStatus.OK).body(enderecoDTO);
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode(), e.getMessage());
        }
    }

    @GetMapping("/{uf}/{cidade}/{logradouro}")
    public ResponseEntity<List<EnderecoDTO>> buscarEnderecoPorUfCidadeELogradouro(
            @PathVariable("uf") String uf,
            @PathVariable("cidade") String cidade,
            @PathVariable("logradouro") String logradouro
    ){
        try {
            List<EnderecoDTO> enderecos = integration.enderecoPorUfCidadeELogradouro(uf, cidade, logradouro);
            return ResponseEntity.status(HttpStatus.OK).body(enderecos);
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode(), e.getMessage());
        }
    }

}
