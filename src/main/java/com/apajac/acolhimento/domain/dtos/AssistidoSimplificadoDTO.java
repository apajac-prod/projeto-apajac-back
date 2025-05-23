package com.apajac.acolhimento.domain.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AssistidoSimplificadoDTO {
    private Long id;
    private String nome;
    private Integer idade;
    private Character sexo;
    private String responsavel;
    private boolean statusAssistido;
}
