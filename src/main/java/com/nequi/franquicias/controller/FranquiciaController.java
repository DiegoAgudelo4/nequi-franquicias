package com.nequi.franquicias.controller;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.service.FranquiciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/franquicias")
public class FranquiciaController {
    private final FranquiciaService franquiciaService;

    public FranquiciaController(FranquiciaService franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> obtenerfranquicias() {
        try {
            return franquiciaService.obtenerTodos()
                    .collectList()
                    .map(franquicias -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("mensaje", "franquicias obtenidos exitosamente");
                        response.put("resultado", true);
                        response.put("data", franquicias);
                        return ResponseEntity.ok(response);
                    });
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "mensaje", "Error al obtener franquicias",
                    "resultado", false,
                    "data", null
            )));
        }
    }

    @GetMapping("/activos")
    public Mono<ResponseEntity<Map<String, Object>>> obtenerfranquiciasActivos() {
        return franquiciaService.obtenerTodos()
                .filter(franquicia -> franquicia.isActive())
                .collectList() // Convierte el Flux en una lista
                .map(listafranquicias -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "Lista de franquicias activos obtenida correctamente");
                    response.put("resultado", true);
                    response.put("data", listafranquicias);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "No hay franquicias activos");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al obtener franquicias activos: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> obtenerfranquicia(@PathVariable Long id) {
        return franquiciaService.obtenerPorId(id)
                .map(franquicia -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", "franquicia obtenido exitosamente");
                    response.put("resultado", true);
                    response.put("data", franquicia);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "franquicia no encontrado");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al obtener franquicia: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }


    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> crearfranquicia(@RequestBody Franquicia franquicia) {
        try {
            return franquiciaService.guardar(franquicia)
                    .map(prod -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                            "mensaje", "franquicia creado exitosamente",
                            "resultado", true,
                            "data", prod
                    )));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "mensaje", "Error al crear franquicia",
                    "resultado", false,
                    "data", null
            )));
        }
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> eliminarfranquicia(@PathVariable Long id) {
        return franquiciaService.obtenerPorId(id)
                .flatMap(franquicia -> {
                    franquicia.setActive(false);
                    return franquiciaService.guardar(franquicia)
                            .map(updatedfranquicia -> {
                                Map<String, Object> response = new HashMap<>();
                                response.put("mensaje", "franquicia eliminado correctamente");
                                response.put("resultado", true);
                                response.put("data", updatedfranquicia);
                                return ResponseEntity.ok(response);
                            });
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "franquicia no encontrado");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al eliminar franquicia: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }
}
