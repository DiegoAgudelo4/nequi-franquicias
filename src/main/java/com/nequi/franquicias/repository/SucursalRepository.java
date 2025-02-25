package com.nequi.franquicias.repository;

import com.nequi.franquicias.model.Sucursal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SucursalRepository extends ReactiveCrudRepository<Sucursal, Long> {
}
