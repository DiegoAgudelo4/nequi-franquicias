package com.nequi.franquicias.service;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FranquiciaService {
    private final FranquiciaRepository franquiciaRepository;

    public FranquiciaService(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    public Flux<Franquicia> obtenerTodos() {
        return franquiciaRepository.findAll();
    }

    public Mono<Franquicia> obtenerPorId(Long id) {
        return franquiciaRepository.findById(id);
    }

    public Mono<Franquicia> guardar(Franquicia franquicia) {
        return franquiciaRepository.save(franquicia);
    }
}
