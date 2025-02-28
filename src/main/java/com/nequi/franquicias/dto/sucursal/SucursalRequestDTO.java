package com.nequi.franquicias.dto.sucursal;


import lombok.Data;

@Data
public class SucursalRequestDTO {
    private String nombre;
    private boolean active;
    private long idFranquicia;
}
