package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.sucursal.SucursalRequestDTO;
import com.nequi.franquicias.dto.sucursal.SucursalResponseDTO;
import com.nequi.franquicias.exception.ValidacionException;
import com.nequi.franquicias.mapper.SucursalMapper;
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
    private final SucursalMapper sucursalMapper;

    public SucursalService(SucursalRepository sucursalRepository, FranquiciaRepository franquiciaRepository, SucursalMapper sucursalMapper) {
        this.sucursalRepository = sucursalRepository;
        this.franquiciaRepository = franquiciaRepository;
        this.sucursalMapper = sucursalMapper;
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

    public Mono<Sucursal> guardar(SucursalRequestDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isEmpty()) {
            return Mono.error(new ValidacionException("El nombre de la sucursal es obligatorio"));
        }
        if (!dto.isActive()) {
            dto.setActive(true);
        }
        return franquiciaRepository.findByIdFranquicia(dto.getIdFranquicia())
                .switchIfEmpty(Mono.error(new ValidacionException("La franquicia [" + dto.getIdFranquicia() + "] no existe")))
                .flatMap(franquicia -> {
                    Sucursal sucursal = sucursalMapper.toEntity(dto);
                    return sucursalRepository.save(sucursal);
                });
    }

    public Mono<SucursalResponseDTO> desactivarSucursal(long id) {
        return sucursalRepository.findById(id)
                .switchIfEmpty(Mono.error(new ValidacionException("Sucursal [" + id + "] no existe.")))
                .flatMap(sucursal -> {
                    sucursal.setActive(false);
                    return sucursalRepository.save(sucursal)
                            .flatMap(savedSucursal -> franquiciaRepository.findById(savedSucursal.getIdSucursal())
                                    .defaultIfEmpty(new Franquicia())
                                    .map(franquicia -> sucursalMapper.toResponseDTO(savedSucursal, franquicia))
                            );
                });
    }

    public Mono<SucursalResponseDTO> actualizarSucursal(Long id, SucursalRequestDTO sucursalActualizado) {

        if (sucursalActualizado.getNombre() == null || sucursalActualizado.getNombre().isEmpty()) {
            return Mono.error(new ValidacionException("El nombre de la sucursal es obligatorio"));
        }
        System.out.println(sucursalActualizado.getIdFranquicia());
        return sucursalRepository.findById(id)
                .switchIfEmpty(Mono.error(new ValidacionException("Sucursal [" + id + "] no existe.")))
                .flatMap(sucursal ->
                        franquiciaRepository.findById(sucursalActualizado.getIdFranquicia())
                                .switchIfEmpty(Mono.error(new ValidacionException("Franquicia [" + sucursalActualizado.getIdFranquicia() + "] no existe.")))
                                .flatMap(franquicia -> {
                                    sucursal.setNombre(sucursalActualizado.getNombre());
                                    sucursal.setActive(sucursalActualizado.isActive());
                                    sucursal.setIdFranquicia(sucursalActualizado.getIdFranquicia());

                                    return sucursalRepository.save(sucursal)
                                            .map(savedSucursal -> sucursalMapper.toResponseDTO(savedSucursal, franquicia));
                                })
                );
    }


}
