package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.producto.ProductoRequestDTO;
import com.nequi.franquicias.dto.producto.ProductoResponseDTO;
import com.nequi.franquicias.exception.RecursoNoEncontradoException;
import com.nequi.franquicias.exception.ValidacionException;
import com.nequi.franquicias.mapper.ProductoMapper;
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
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoRepository productoRepository, SucursalRepository sucursalRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
        this.productoMapper = productoMapper;
    }

    public Flux<ProductoResponseDTO> obtenerTodos() {
        return productoRepository.findAll()
                .flatMap(producto -> sucursalRepository.findById(producto.getIdSucursal())
                        .defaultIfEmpty(new Sucursal())
                        .map(sucursal -> productoMapper.toResponseDTO(producto, sucursal))
                );
    }

    public Mono<ProductoResponseDTO> obtenerPorId(Long id) {
        if (id == null) {
            return Mono.error(new ValidacionException("Debe enviar un id"));
        }
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new RecursoNoEncontradoException("Producto no encontrado")))
                .flatMap(producto -> sucursalRepository.findById(producto.getIdSucursal())
                        .defaultIfEmpty(new Sucursal())
                        .map(sucursal -> productoMapper.toResponseDTO(producto, sucursal))
                );
    }

    public Mono<ProductoResponseDTO> guardar(ProductoRequestDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isEmpty()) {
            return Mono.error(new ValidacionException("El nombre del producto es obligatorio"));
        }
        if (dto.getStock() < 0) {
            return Mono.error(new ValidacionException("El stock no puede ser negativo"));
        }

        if (sucursalRepository.findByIdSucursal(dto.getIdSucursal()) == null) {
            return Mono.error(new ValidacionException("La sucursal no existe"));
        }

        Producto producto = productoMapper.toEntity(dto);
        return productoRepository.save(producto)
                .flatMap(savedProducto -> sucursalRepository.findById(savedProducto.getIdSucursal())
                        .defaultIfEmpty(new Sucursal())
                        .map(sucursal -> productoMapper.toResponseDTO(savedProducto, sucursal))
                );
    }

    public Mono<ProductoResponseDTO> desactivarProducto(long id) {
        return productoRepository.findById(id)
                .flatMap(producto -> {
                    producto.setActive(false);
                    return productoRepository.save(producto)
                            .flatMap(savedProducto -> sucursalRepository.findById(savedProducto.getIdSucursal())
                                    .defaultIfEmpty(new Sucursal())
                                    .map(sucursal -> productoMapper.toResponseDTO(savedProducto, sucursal))
                            );
                });
    }


    public Mono<ProductoResponseDTO> actualizarProducto(Long id, ProductoRequestDTO productoActualizado) {
        if (productoRepository.findById(id) == null) {
            return Mono.error(new ValidacionException("El producto a actualizar no existe"));
        }

        if (productoActualizado.getNombre() == null || productoActualizado.getNombre().isEmpty()) {
            return Mono.error(new ValidacionException("El nombre del producto es obligatorio"));
        }
        if (productoActualizado.getStock() < 0) {
            return Mono.error(new ValidacionException("El stock no puede ser negativo"));
        }

        if (sucursalRepository.findByIdSucursal(productoActualizado.getIdSucursal()) == null) {
            return Mono.error(new ValidacionException("La sucursal no existe"));
        }
        return productoRepository.findById(id)
                .flatMap(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setStock(productoActualizado.getStock());
                    producto.setActive(productoActualizado.isActive());
                    producto.setIdSucursal(productoActualizado.getIdSucursal());

                    return productoRepository.save(producto)
                            .flatMap(savedProducto -> sucursalRepository.findById(savedProducto.getIdSucursal())
                                    .defaultIfEmpty(new Sucursal())
                                    .map(sucursal -> productoMapper.toResponseDTO(savedProducto, sucursal))
                            );
                });
    }

}
