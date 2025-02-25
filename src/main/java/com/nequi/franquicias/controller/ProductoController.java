package com.nequi.franquicias.controller;

import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> obtenerProductos() {
        try {
            return productoService.obtenerTodos()
                    .collectList()
                    .map(productos -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("mensaje", "Productos obtenidos exitosamente");
                        response.put("resultado", true);
                        response.put("data", productos);
                        return ResponseEntity.ok(response);
                    });
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "mensaje", "Error al obtener productos",
                    "resultado", false,
                    "data", null
            )));
        }
    }

    @GetMapping("/activos")
    public Mono<ResponseEntity<Map<String, Object>>> obtenerProductosActivos() {
        return productoService.obtenerTodos()
                .filter(producto -> producto.isActive())
                .collectList() // Convierte el Flux en una lista
                .map(listaProductos -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "Lista de productos activos obtenida correctamente");
                    response.put("resultado", true);
                    response.put("data", listaProductos);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "No hay productos activos");
                    put("resultado", false);
                    put("data", null);
                }}))
            .onErrorResume(e -> {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al obtener productos activos: " + e.getMessage());
            errorResponse.put("resultado", false);
            errorResponse.put("data", null);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
        });
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "Producto obtenido exitosamente");
                    response.put("resultado", true);
                    response.put("data", producto);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "Producto no encontrado");
                    put("resultado", false);
                    put("data", null);
                }}))
            .onErrorResume(e -> {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al obtener producto: " + e.getMessage());
            errorResponse.put("resultado", false);
            errorResponse.put("data", null);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
        });
    }


    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> crearProducto(@RequestBody Producto producto) {
        try {
            return productoService.guardar(producto)
                    .map(prod -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                            "mensaje", "Producto creado exitosamente",
                            "resultado", true,
                            "data", prod
                    )));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "mensaje", "Error al crear producto",
                    "resultado", false,
                    "data", null
            )));
        }
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> eliminarProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .flatMap(producto -> {
                    producto.setActive(false);
                    return productoService.guardar(producto)
                            .map(updatedProducto -> {
                                Map<String, Object> response = new HashMap<>();
                                response.put("mensaje", "Producto eliminado correctamente");
                                response.put("resultado", true);
                                response.put("data", updatedProducto);
                                return ResponseEntity.ok(response);
                            });
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "Producto no encontrado");
                    put("resultado", false);
                    put("data", null);
                }}))
            .onErrorResume(e -> {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al eliminar producto: " + e.getMessage());
            errorResponse.put("resultado", false);
            errorResponse.put("data", null);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
        });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> actualizarProducto(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        return productoService.obtenerPorId(id)
                .flatMap(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setStock(productoActualizado.getStock());
                    producto.setActive(productoActualizado.isActive());
                    producto.setIdSucursal(productoActualizado.getIdSucursal());

                    return productoService.guardar(producto)
                            .map(updatedProducto -> {
                                Map<String, Object> response = new HashMap<>();
                                response.put("mensaje", "Sucursal actualizada correctamente");
                                response.put("resultado", true);
                                response.put("data", updatedProducto);
                                return ResponseEntity.ok(response);
                            });
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "Producto no encontrado");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al actualizar producto: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }


}
