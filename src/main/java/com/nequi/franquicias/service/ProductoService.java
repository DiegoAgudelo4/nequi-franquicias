package com.nequi.franquicias.service;

import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Flux<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Mono<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Mono<Producto> guardar(Producto producto) {
        return productoRepository.save(producto);
    }
}
