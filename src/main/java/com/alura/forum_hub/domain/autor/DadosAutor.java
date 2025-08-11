package com.alura.forum_hub.domain.autor;

import com.alura.forum_hub.domain.autor.Autor;

public record DadosAutor(Long id, String nome, String email) {

public DadosAutor(Autor autor){
    this(autor.getId(), autor.getNome(), autor.getEmail());
}
}
