package com.nequi.franquicias.service;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.repository.FranquiciaRepository;
import com.nequi.franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    public SucursalService(SucursalRepository sucursalRepository, FranquiciaRepository franquiciaRepository) {
        this.sucursalRepository = sucursalRepository;
        this.franquiciaRepository = franquiciaRepository;
    }

    public Flux<Sucursal> obtenerTodos() {
        return sucursalRepository.findAll()
                .flatMap(sucursal ->
                        franquiciaRepository.findByIdFranquicia((long) sucursal.getIdFranquicia())
                                .defaultIfEmpty(new Franquicia())
                                .map(franquicia -> {
                                    sucursal.setFranquicia(franquicia);
                                    return sucursal;
                                })
                );
    }

    public Mono<Sucursal> obtenerPorId(Long id) {
        return sucursalRepository.findById(id)
                .flatMap(sucursal -> franquiciaRepository.findByIdFranquicia((long) sucursal.getIdFranquicia())
                        .defaultIfEmpty(new Franquicia())
                        .map(franquicia -> {
                            sucursal.setFranquicia((franquicia));
                            return sucursal;
                        })
                );
    }

    public Mono<Sucursal> guardar(Sucursal Sucursal) {
        return sucursalRepository.save(Sucursal)
                .flatMap(sucursal -> franquiciaRepository.findByIdFranquicia((long) sucursal.getIdFranquicia())
                        .defaultIfEmpty(new Franquicia())
                        .map(franquicia -> {
                            sucursal.setFranquicia((franquicia));
                            return sucursal;
                        })
                );
    }
}
