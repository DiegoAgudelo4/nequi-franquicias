package com.nequi.franquicias.controller;

import com.nequi.franquicias.dto.producto.ProductoRequestDTO;
import com.nequi.franquicias.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "API para gestionar productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Recupera la lista de productos de la bd.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
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
    @Operation(summary = "Obtener productos activos", description = "Recupera unicamente la lista de productos activos.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
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
    @Operation(summary = "Obtener producto por id", description = "Recupera el producto especificado con el Id")
    @ApiResponse(responseCode = "200", description = "Producto obtenido correctamente")
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
    @Operation(summary = "Crear un producto", description = "Crea un producto con los datos brindados")
    @ApiResponse(responseCode = "201", description = "Producto creado correctamente")
    public Mono<ResponseEntity<Map<String, Object>>> crearProducto(@RequestBody ProductoRequestDTO producto) {
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
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto dado el id")
    @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente")
    public Mono<ResponseEntity<Map<String, Object>>> eliminarProducto(@PathVariable Long id) {
        return productoService.desactivarProducto(id)
                .map(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "Producto eliminado exitosamente");
                    response.put("resultado", true);
                    response.put("data", producto);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "Producto no eliminada");
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
    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto dado el id y los nuevos datos del producto, debe enviar incluso los antiguos")
    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente")
    public Mono<ResponseEntity<Map<String, Object>>> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequestDTO productoActualizado) {
        return productoService.actualizarProducto(id, productoActualizado)
                .map(producto -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "Producto actualizado exitosamente");
                    response.put("resultado", true);
                    response.put("data", producto);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "Producto no actualizado");
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
