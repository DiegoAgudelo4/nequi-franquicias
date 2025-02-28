package com.nequi.franquicias.dto.sucursal;

import com.nequi.franquicias.dto.franquicia.FranquiciaRequestDTO;
import lombok.Data;

@Data
public class SucursalResponseDTO {
    private long idSucursal;
    private String nombre;
    private boolean active;
    private long idFranquicia;
    private FranquiciaRequestDTO franquicia;
}
