package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import com.funds.backend.domain.model.Impacto;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidad JPA de la tabla {@code indicador}. */
@Entity
@Table(name = "indicador")
public class IndicadorEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "alias_ff")
    private String aliasFf;

    @Column(name = "sector")
    private String sector;

    @Enumerated(EnumType.STRING)
    @Column(name = "impacto")
    private Impacto impacto;

    @Column(name = "frecuencia")
    private String frecuencia;

    @Column(name = "hora_et")
    private String horaEt;

    @Embedded
    private FuenteEmbeddable fuente;

    @Embedded
    private ReglaEmbeddable regla;

    protected IndicadorEntity() {
    }

    public IndicadorEntity(String id, String nombre, String aliasFf, String sector, Impacto impacto,
                           String frecuencia, String horaEt, FuenteEmbeddable fuente, ReglaEmbeddable regla) {
        this.id = id;
        this.nombre = nombre;
        this.aliasFf = aliasFf;
        this.sector = sector;
        this.impacto = impacto;
        this.frecuencia = frecuencia;
        this.horaEt = horaEt;
        this.fuente = fuente;
        this.regla = regla;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAliasFf() {
        return aliasFf;
    }

    public String getSector() {
        return sector;
    }

    public Impacto getImpacto() {
        return impacto;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public String getHoraEt() {
        return horaEt;
    }

    public FuenteEmbeddable getFuente() {
        return fuente;
    }

    public ReglaEmbeddable getRegla() {
        return regla;
    }
}
