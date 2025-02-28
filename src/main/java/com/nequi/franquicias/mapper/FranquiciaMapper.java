package com.nequi.franquicias.mapper;

import com.nequi.franquicias.dto.franquicia.FranquiciaRequestDTO;
import com.nequi.franquicias.model.Franquicia;
import org.springframework.stereotype.Component;

@Component
public class FranquiciaMapper {

    public Franquicia toEntity(FranquiciaRequestDTO dto){
        Franquicia franquicia = new Franquicia();

        franquicia.setNombre(dto.getNombre());
        franquicia.setActive(dto.isActive());
        return franquicia;
    }
}
