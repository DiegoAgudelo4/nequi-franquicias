package com.nequi.franquicias.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Data
@Table(name = "producto")
public class Producto {
    @Id
    @Column("idProducto")
    private long idProducto;

    private String nombre;
    private int stock;

    private boolean active;

    @Column("idSucursal")
    private int idSucursal;
}
