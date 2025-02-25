package com.nequi.franquicias.repository;

import com.nequi.franquicias.model.Sucursal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SucursalRepository extends ReactiveCrudRepository<Sucursal, Long> {
    Mono<Sucursal> findByIdSucursal(Long idSucursal);
}
