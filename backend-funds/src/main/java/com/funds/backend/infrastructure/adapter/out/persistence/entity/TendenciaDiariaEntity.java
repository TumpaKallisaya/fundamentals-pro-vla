package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.funds.backend.domain.model.ClasificacionTendencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidad JPA de la tabla {@code tendencia_diaria}. */
@Entity
@Table(name = "tendencia_diaria")
public class TendenciaDiariaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "indice_codigo")
    private String indiceCodigo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "puntaje")
    private BigDecimal puntaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "clasificacion")
    private ClasificacionTendencia clasificacion;

    protected TendenciaDiariaEntity() {
    }

    public TendenciaDiariaEntity(Long id, String indiceCodigo, LocalDate fecha, BigDecimal puntaje,
                                 ClasificacionTendencia clasificacion) {
        this.id = id;
        this.indiceCodigo = indiceCodigo;
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.clasificacion = clasificacion;
    }

    public Long getId() {
        return id;
    }

    public String getIndiceCodigo() {
        return indiceCodigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public BigDecimal getPuntaje() {
        return puntaje;
    }

    public ClasificacionTendencia getClasificacion() {
        return clasificacion;
    }
}
