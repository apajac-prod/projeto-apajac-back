package com.apajac.acolhimento.gateway;

import com.apajac.acolhimento.domain.dtos.EnderecoDTO;
import com.apajac.acolhimento.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnderecoIntegration {

    private static final String EXTENSION = "/json";
    private static final String SLASH = "/";
    private static final String CIDADE = "Jacareí";
    private static final String UF = "SP";

    private final RestTemplate restTemplate;

    @Value("${viacep.api.url}")
    private String pathViaCep;

    public EnderecoDTO enderecoPorCep(String cep){
        return getEnderecoDTOPorCep(cep);
    }

    private EnderecoDTO getEnderecoDTOPorCep(String cep) {
        try {
            String url = pathViaCep + cep + EXTENSION;
            ResponseEntity<Map> entity = restTemplate.getForEntity(url, Map.class);
            Map body = entity.getBody();

            validatedCidadeAndUf(body);

            return EnderecoDTO.builder()
                    .endereco((body.get("logradouro") !=null ? body.get("logradouro").toString() : null))
                    .bairro((body.get("bairro") != null ? body.get("bairro").toString() : null))
                    .cidade((body.get("localidade") != null ? body.get("localidade").toString() : null))
                    .UF((body.get("uf") != null ? body.get("uf").toString() : null))
                    .cep((body.get("cep") != null ? body.get("cep").toString() : null))
                    .build();
        } catch (BusinessException ex){
            throw new BusinessException(ex.getMessage());
        } catch (RuntimeException ex){
            throw new IllegalArgumentException("Não foi possivel encontrar o Endereço para o CEP informado.");
        }
    }

    private void validatedCidadeAndUf(Map body) {
        if (!body.get("uf").equals(UF) || !body.get("localidade").equals(CIDADE)){
            throw new BusinessException("CEP informado não pertence ao Municipio de Jacareí");
        }
    }

    public List<EnderecoDTO> enderecoPorUfCidadeELogradouro(String uf, String cidade, String logradouro) {

        try {
            List<Map> objects = new ArrayList<>();
            List<EnderecoDTO> enderecoDTOS = new ArrayList<>();
            String url = pathViaCep + uf + SLASH + cidade + SLASH +logradouro + EXTENSION;
            objects.addAll(restTemplate.getForEntity(url, List.class).getBody());

            for (Map endereco: objects){
                enderecoDTOS.add(EnderecoDTO.builder()
                        .endereco((endereco.get("logradouro") !=null ? endereco.get("logradouro").toString() : null))
                        .bairro((endereco.get("bairro") != null ? endereco.get("bairro").toString() : null))
                        .cidade((endereco.get("localidade") != null ? endereco.get("localidade").toString() : null))
                        .UF((endereco.get("uf") != null ? endereco.get("uf").toString() : null))
                        .cep((endereco.get("cep") != null ? endereco.get("cep").toString() : null))
                        .build());
            }
            return enderecoDTOS;
        } catch (RuntimeException ex){
            throw new IllegalArgumentException("Não foi possivel encontrar o Endereço com os dados informados.");
        }
    }
}
