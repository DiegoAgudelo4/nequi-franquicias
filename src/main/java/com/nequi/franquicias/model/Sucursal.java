package com.nequi.franquicias.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "sucursal")
public class Sucursal {
    @Id
    @Column("idSucursal")
    private long idSucursal;

    private String nombre;

    private boolean active;

    @Column("idFranquicia")
    private long idFranquicia;

    @Transient
    private Franquicia franquicia;
}
