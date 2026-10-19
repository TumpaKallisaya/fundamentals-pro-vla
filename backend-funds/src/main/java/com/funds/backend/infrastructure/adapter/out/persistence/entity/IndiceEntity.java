package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidad JPA de la tabla {@code indice}. */
@Entity
@Table(name = "indice")
public class IndiceEntity {

    @Id
    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "sector")
    private String sector;

    protected IndiceEntity() {
    }

    public IndiceEntity(String codigo, String nombre, String sector) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.sector = sector;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSector() {
        return sector;
    }
}
