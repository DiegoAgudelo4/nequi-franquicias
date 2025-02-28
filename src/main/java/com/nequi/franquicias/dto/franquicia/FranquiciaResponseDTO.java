package com.nequi.franquicias.dto.franquicia;

import lombok.Data;

@Data
public class FranquiciaResponseDTO {
    private long idFranquicia;
    private String nombre;
    private boolean active;
}
