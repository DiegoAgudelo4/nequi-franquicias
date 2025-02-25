package com.nequi.franquicias.service;

import com.nequi.franquicias.model.Sucursal;
import com.nequi.franquicias.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    public SucursalService(SucursalRepository SucursalRepository) {
        this.sucursalRepository = SucursalRepository;
    }

    public Flux<Sucursal> obtenerTodos() {
        return sucursalRepository.findAll();
    }

    public Mono<Sucursal> obtenerPorId(Long id) {
        return sucursalRepository.findById(id);
    }

    public Mono<Sucursal> guardar(Sucursal Sucursal) {
        return sucursalRepository.save(Sucursal);
    }
}
