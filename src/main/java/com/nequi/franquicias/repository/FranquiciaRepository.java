package com.nequi.franquicias.repository;

import com.nequi.franquicias.model.Franquicia;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranquiciaRepository extends ReactiveCrudRepository<Franquicia, Long> {
}
