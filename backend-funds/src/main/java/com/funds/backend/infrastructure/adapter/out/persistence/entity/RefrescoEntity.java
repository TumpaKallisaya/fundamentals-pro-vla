package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import java.time.Instant;

import com.funds.backend.domain.model.EstadoRefresco;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidad JPA de la tabla {@code refresco}. */
@Entity
@Table(name = "refresco")
public class RefrescoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "iniciado_en_utc")
    private Instant iniciadoEnUtc;

    @Column(name = "finalizado_en_utc")
    private Instant finalizadoEnUtc;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoRefresco estado;

    @Column(name = "detalle")
    private String detalle;

    protected RefrescoEntity() {
    }

    public RefrescoEntity(Long id, Instant iniciadoEnUtc, Instant finalizadoEnUtc,
                          EstadoRefresco estado, String detalle) {
        this.id = id;
        this.iniciadoEnUtc = iniciadoEnUtc;
        this.finalizadoEnUtc = finalizadoEnUtc;
        this.estado = estado;
        this.detalle = detalle;
    }

    public Long getId() {
        return id;
    }

    public Instant getIniciadoEnUtc() {
        return iniciadoEnUtc;
    }

    public Instant getFinalizadoEnUtc() {
        return finalizadoEnUtc;
    }

    public EstadoRefresco getEstado() {
        return estado;
    }

    public String getDetalle() {
        return detalle;
    }
}
