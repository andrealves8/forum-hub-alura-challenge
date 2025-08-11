package com.alura.forum_hub.domain.curso;

import com.alura.forum_hub.domain.curso.Curso;

public record DadosCurso(Long id, String nome, String categoriaCurso) {

    public DadosCurso(Curso curso){
        this(curso.getId(), curso.getNome(), curso.getCategoria());
    }
}

