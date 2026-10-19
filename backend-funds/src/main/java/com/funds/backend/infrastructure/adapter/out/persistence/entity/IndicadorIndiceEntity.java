package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import com.funds.backend.domain.model.Relevancia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidad JPA de la tabla {@code indicador_indice}. */
@Entity
@Table(name = "indicador_indice")
public class IndicadorIndiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "indicador_id")
    private String indicadorId;

    @Column(name = "indice_codigo")
    private String indiceCodigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "relevancia")
    private Relevancia relevancia;

    protected IndicadorIndiceEntity() {
    }

    public IndicadorIndiceEntity(Long id, String indicadorId, String indiceCodigo, Relevancia relevancia) {
        this.id = id;
        this.indicadorId = indicadorId;
        this.indiceCodigo = indiceCodigo;
        this.relevancia = relevancia;
    }

    public Long getId() {
        return id;
    }

    public String getIndicadorId() {
        return indicadorId;
    }

    public String getIndiceCodigo() {
        return indiceCodigo;
    }

    public Relevancia getRelevancia() {
        return relevancia;
    }
}
