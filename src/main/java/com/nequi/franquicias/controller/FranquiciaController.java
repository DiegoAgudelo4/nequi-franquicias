package com.nequi.franquicias.controller;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.service.FranquiciaService;
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
@RequestMapping("/franquicias")
@Tag(name = "Franquicias", description = "API para gestionar Franquicias")
public class FranquiciaController {
    private final FranquiciaService franquiciaService;

    public FranquiciaController(FranquiciaService franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos las franquicias", description = "Recupera la lista de franquicias de la bd.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
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
    @Operation(summary = "Obtener franquicias activas", description = "Recupera unicamente la lista de franquicias activas.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
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
    @Operation(summary = "Obtener franquicia por id", description = "Recupera la franquicia especificada con el Id")
    @ApiResponse(responseCode = "200", description = "Franquicia obtenido correctamente")
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
    @Operation(summary = "Crear una franquicia", description = "Crea una franquicia con los datos brindados")
    @ApiResponse(responseCode = "201", description = "Franquicia creada correctamente")
    public Mono<ResponseEntity<Map<String, Object>>> crearfranquicia(@RequestBody Franquicia franquicia) {
        try {
            return franquiciaService.guardar(franquicia)
                    .map(franq -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                            "mensaje", "franquicia creado exitosamente",
                            "resultado", true,
                            "data", franq
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
    @Operation(summary = "Eliminar una franquicia", description = "Elimina una franquicia dado el id")
    @ApiResponse(responseCode = "200", description = "Franquicia eliminada correctamente")
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

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una franquicia", description = "Actualiza una franquicia dado el id y los nuevos datos de la franquicia, debe enviar incluso los antiguos")
    @ApiResponse(responseCode = "200", description = "Franquicia actualizada correctamente")
    public Mono<ResponseEntity<Map<String, Object>>> actualizarFranquicia(@PathVariable Long id, @RequestBody Franquicia franquiciaActualizada) {
        return franquiciaService.obtenerPorId(id)
                .flatMap(franquicia -> {
                    franquicia.setNombre(franquiciaActualizada.getNombre());
                    franquicia.setActive(franquiciaActualizada.isActive());

                    return franquiciaService.guardar(franquicia)
                            .map(updatedFranquicia -> {
                                Map<String, Object> response = new HashMap<>();
                                response.put("mensaje", "Franquicia actualizada correctamente");
                                response.put("resultado", true);
                                response.put("data", updatedFranquicia);
                                return ResponseEntity.ok(response);
                            });
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<>() {{
                    put("mensaje", "Franquicia no encontrada");
                    put("resultado", false);
                    put("data", null);
                }}))
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("mensaje", "Error al actualizar franquicia: " + e.getMessage());
                    errorResponse.put("resultado", false);
                    errorResponse.put("data", null);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });
    }

}
