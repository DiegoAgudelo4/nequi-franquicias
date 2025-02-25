package com.nequi.franquicias.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "franquicia")
public class Franquicia {
    @Id
    @Column("idFranquicia")
    private long idFranquicia;

    private String nombre;

    private boolean active;
}
