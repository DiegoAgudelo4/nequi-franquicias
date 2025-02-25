package com.nequi.franquicias.controller;

import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.service.SucursalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sucursales")
public class SucursalController {
    private final SucursalService sucursalService;

    public SucursalController(SucursalService SucursalService) {
        this.sucursalService = SucursalService;
    }

    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> obtenersucursales() {
        try {
            return sucursalService.obtenerTodos()
                    .collectList()
                    .map(sucursales -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("mensaje", "sucursales obtenidas exitosamente");
                        response.put("resultado", true);
                        response.put("data", sucursales);
                        return ResponseEntity.ok(response);
                    });
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "mensaje", "Error al obtener sucursales",
                    "resultado", false,
                    "data", null
            )));
        }
    }

    @GetMapping("/activos")
    public Mono<ResponseEntity<Map<String, Object>>> obtenersucursalesActivas() {
        return sucursalService.obtenerTodos()
                .filter(sucursal -> sucursal.isActive())
                .collectList() // Convierte el Flux en una lista
                .map(listasucursales -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "Lista de sucursales activas obtenida correctamente");
                    response.put("resultado", true);
                    response.put("data", listasucursales);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "No hay sucursales activas");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al obtener sucursales activas: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> obtenersucursal(@PathVariable Long id) {
        return sucursalService.obtenerPorId(id)
                .map(sucursal -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "sucursal obtenida exitosamente");
                    response.put("resultado", true);
                    response.put("data", sucursal);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "sucursal no encontrada");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al obtener sucursal: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> crearsucursal(@RequestBody Sucursal sucursal) {
        try {
            return sucursalService.guardar(sucursal)
                    .map(suc -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                            "mensaje", "sucursal creada exitosamente",
                            "resultado", true,
                            "data", suc
                    )));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "mensaje", "Error al crear sucursal",
                    "resultado", false,
                    "data", null
            )));
        }
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> eliminarsucursal(@PathVariable Long id) {
        return sucursalService.obtenerPorId(id)
                .flatMap(sucursal -> {
                    sucursal.setActive(false);
                    return sucursalService.guardar(sucursal)
                            .map(updatedsucursal -> {
                                Map<String, Object> response = new HashMap<>();
                                response.put("mensaje", "sucursal eliminada correctamente");
                                response.put("resultado", true);
                                response.put("data", updatedsucursal);
                                return ResponseEntity.ok(response);
                            });
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "sucursal no encontrada");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al eliminar sucursal: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> actualizarSucursal(@PathVariable Long id, @RequestBody Sucursal sucursalActualizada) {
        return sucursalService.obtenerPorId(id)
                .flatMap(sucursal -> {
                    sucursal.setNombre(sucursalActualizada.getNombre());
                    sucursal.setActive(sucursalActualizada.isActive());
                    sucursal.setIdFranquicia(sucursalActualizada.getIdFranquicia());

                    return sucursalService.guardar(sucursal)
                            .map(updatedSucursal -> {
                                Map<String, Object> response = new HashMap<>();
                                response.put("mensaje", "Sucursal actualizada correctamente");
                                response.put("resultado", true);
                                response.put("data", updatedSucursal);
                                return ResponseEntity.ok(response);
                            });
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "Sucursal no encontrada");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al actualizar sucursal: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }

}
