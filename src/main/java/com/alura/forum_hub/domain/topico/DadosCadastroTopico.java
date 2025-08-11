package com.alura.forum_hub.domain.topico;

import com.alura.forum_hub.domain.autor.Autor;
import com.alura.forum_hub.domain.curso.Curso;
import com.alura.forum_hub.domain.resposta.Resposta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record DadosCadastroTopico(
        @NotBlank
        String titulo,
        @NotBlank
        String mensagem,

        LocalDateTime dataCriacao,
        @NotBlank
        String status,
        @NotNull
        @Valid
        Autor autor,
        @NotNull
        @Valid
        Curso curso
 ) {
}
