package com.alura.forum_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.forum_hub.domain.curso.Curso;
import com.alura.forum_hub.domain.curso.DadosCurso;
import com.alura.forum_hub.repository.CursoRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/{id}")
    public ResponseEntity buscarAutor(@PathVariable Long id){
        var curso = cursoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosCurso(curso));
    }

    @GetMapping
    public ResponseEntity<Page<DadosCurso>> listarCursos(@PageableDefault(sort = {"nome"}) Pageable pageable) {
        var pagina = cursoRepository.findAll(pageable).map(DadosCurso::new);
        return ResponseEntity.ok(pagina);
    }


    @PostMapping
    public Curso cadastrar(@RequestBody @Valid Curso a){
        return cursoRepository.save(a);
    }

    @PutMapping()
    public Curso editar(@RequestBody @Valid Curso a){
        return cursoRepository.save(a);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void remover(@PathVariable Long id){
        cursoRepository.deleteById(id);
    }
}

