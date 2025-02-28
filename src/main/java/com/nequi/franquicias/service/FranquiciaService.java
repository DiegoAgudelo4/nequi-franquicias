package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.franquicia.FranquiciaRequestDTO;
import com.nequi.franquicias.exception.ValidacionException;
import com.nequi.franquicias.mapper.FranquiciaMapper;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FranquiciaService {
    private final FranquiciaRepository franquiciaRepository;
    private final FranquiciaMapper franquiciaMapper;

    public FranquiciaService(FranquiciaRepository franquiciaRepository, FranquiciaMapper franquiciaMapper) {
        this.franquiciaRepository = franquiciaRepository;
        this.franquiciaMapper = franquiciaMapper;
    }

    public Flux<Franquicia> obtenerTodos() {
        return franquiciaRepository.findAll();
    }

    public Mono<Franquicia> obtenerPorId(Long id) {
        return franquiciaRepository.findById(id);
    }

    public Mono<Franquicia> guardar(FranquiciaRequestDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isEmpty()) {
            return Mono.error(new ValidacionException("El nombre de la franquicia es obligatorio"));
        }
        if (!dto.isActive()) {
            dto.setActive(true);
        }
        Franquicia franquicia = franquiciaMapper.toEntity(dto);
        return franquiciaRepository.save(franquicia);
    }
    public Mono<Franquicia> actualizarfranquicia(Long id, FranquiciaRequestDTO franquiciaActualizado) {
        if (franquiciaRepository.findByIdFranquicia(id) == null) {
            return Mono.error(new ValidacionException("Franquicia a actualizar no existe"));
        }

        if (franquiciaActualizado.getNombre() == null || franquiciaActualizado.getNombre().isEmpty()) {
            return Mono.error(new ValidacionException("El nombre del franquicia es obligatorio"));
        }


        return franquiciaRepository.findById(id)
                .flatMap(franquicia -> {
                    franquicia.setNombre(franquiciaActualizado.getNombre());
                    franquicia.setActive(franquiciaActualizado.isActive());

                    return franquiciaRepository.save(franquicia);
                });
    }


    public Mono<Franquicia> desactivarFranquicia(long id) {
        return franquiciaRepository.findById(id)
                .flatMap(franquicia -> {
                    franquicia.setActive(false);
                    return franquiciaRepository.save(franquicia);
                })
                .switchIfEmpty(Mono.error(new ValidacionException("La franquicia a eliminar no existe")));
    }

    
}
