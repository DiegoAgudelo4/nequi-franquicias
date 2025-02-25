package com.nequi.franquicias.repository;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository extends ReactiveCrudRepository<Franquicia, Long> {
    Mono<Franquicia> findByIdFranquicia(Long idFranquicia);
}
