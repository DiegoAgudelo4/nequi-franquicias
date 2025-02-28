package com.nequi.franquicias.mapper;

import com.nequi.franquicias.dto.franquicia.FranquiciaRequestDTO;
import com.nequi.franquicias.dto.sucursal.SucursalRequestDTO;
import com.nequi.franquicias.dto.sucursal.SucursalResponseDTO;
import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

    public SucursalResponseDTO toResponseDTO(Sucursal sucursal, Franquicia franquicia) {
        SucursalResponseDTO dto = new SucursalResponseDTO();
        dto.setIdSucursal(sucursal.getIdSucursal());
        dto.setNombre(sucursal.getNombre());
        dto.setActive(sucursal.isActive());

        FranquiciaRequestDTO franquiciaDTO = new FranquiciaRequestDTO();
        franquiciaDTO.setNombre(franquicia.getNombre());
        franquiciaDTO.setActive(franquicia.isActive());
        return dto;
    }
    public Sucursal toEntity(SucursalRequestDTO dto){
        Sucursal sucursal= new Sucursal();
        sucursal.setNombre(dto.getNombre());
        sucursal.setIdFranquicia(dto.getIdFranquicia());
        sucursal.setActive(dto.isActive());
        return sucursal;
    }
}
