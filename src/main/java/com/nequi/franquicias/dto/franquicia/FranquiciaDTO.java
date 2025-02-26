package com.nequi.franquicias.dto.franquicia;

import lombok.Data;

@Data
public class FranquiciaDTO {
    private long idFranquicia;
    private String nombre;
    private boolean active;
}
