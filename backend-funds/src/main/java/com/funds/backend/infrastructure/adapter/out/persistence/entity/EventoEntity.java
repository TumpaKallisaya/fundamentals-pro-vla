package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidad JPA de la tabla {@code evento}. */
@Entity
@Table(name = "evento")
public class EventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "indicador_id")
    private String indicadorId;

    @Column(name = "fecha_hora_utc")
    private Instant fechaHoraUtc;

    @Column(name = "forecast")
    private BigDecimal forecast;

    @Column(name = "previous")
    private BigDecimal previous;

    protected EventoEntity() {
    }

    public EventoEntity(Long id, String indicadorId, Instant fechaHoraUtc, BigDecimal forecast, BigDecimal previous) {
        this.id = id;
        this.indicadorId = indicadorId;
        this.fechaHoraUtc = fechaHoraUtc;
        this.forecast = forecast;
        this.previous = previous;
    }

    public Long getId() {
        return id;
    }

    public String getIndicadorId() {
        return indicadorId;
    }

    public Instant getFechaHoraUtc() {
        return fechaHoraUtc;
    }

    public BigDecimal getForecast() {
        return forecast;
    }

    public BigDecimal getPrevious() {
        return previous;
    }
}
