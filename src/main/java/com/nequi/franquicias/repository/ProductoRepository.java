package com.nequi.franquicias.repository;

import com.nequi.franquicias.model.Producto;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {
}
