package com.nequi.franquicias.dto.producto;

import lombok.Data;

@Data
public class ProductoRequestDTO {
    private String nombre;
    private int stock;
    private boolean active;
    private long idSucursal;
}
