package com.nequi.franquicias.service;

import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.repository.ProductoRepository;
import com.nequi.franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    public ProductoService(ProductoRepository productoRepository, SucursalRepository sucursalRepository) {
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    public Flux<Producto> obtenerTodos() {
        return productoRepository.findAll()
                .flatMap(producto ->
                        sucursalRepository.findByIdSucursal((long) producto.getIdSucursal())
                                .defaultIfEmpty(new Sucursal())
                                .map(sucursal -> {
                                    producto.setSucursal(sucursal);
                                    return producto;
                                })
                );
    }

    public Mono<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .flatMap(producto ->
                        sucursalRepository.findByIdSucursal((long) producto.getIdSucursal())
                                .defaultIfEmpty(new Sucursal())
                                .map(sucursal -> {
                                    producto.setSucursal(sucursal);
                                    return producto;
                                })
                );
    }

    public Mono<Producto> guardar(Producto producto) {
        return productoRepository.save(producto) // Guarda el producto
                .flatMap(savedProducto ->
                        sucursalRepository.findByIdSucursal((long) savedProducto.getIdSucursal())
                                .defaultIfEmpty(new Sucursal())
                                .map(sucursal -> {
                                    savedProducto.setSucursal(sucursal);
                                    return savedProducto;
                                })
                );
    }

}
