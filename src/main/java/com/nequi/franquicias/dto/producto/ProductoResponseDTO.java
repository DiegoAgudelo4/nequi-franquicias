package com.nequi.franquicias.dto.producto;

import com.nequi.franquicias.model.Sucursal;
import lombok.Data;

@Data
public class ProductoResponseDTO {
    private long idProducto;
    private String nombre;
    private int stock;
    private boolean active;
    private Sucursal sucursal;
}
