package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;

import com.funds.backend.domain.model.EstadoEvento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Entidad JPA de la tabla {@code resultado}. */
@Entity
@Table(name = "resultado")
public class ResultadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "evento_id")
    private Long eventoId;

    @Column(name = "refresco_id")
    private Long refrescoId;

    @Column(name = "actual")
    private BigDecimal actual;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoEvento estado;

    @Column(name = "desviacion")
    private BigDecimal desviacion;

    protected ResultadoEntity() {
    }

    public ResultadoEntity(Long id, Long eventoId, Long refrescoId, BigDecimal actual,
                           EstadoEvento estado, BigDecimal desviacion) {
        this.id = id;
        this.eventoId = eventoId;
        this.refrescoId = refrescoId;
        this.actual = actual;
        this.estado = estado;
        this.desviacion = desviacion;
    }

    public Long getId() {
        return id;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public Long getRefrescoId() {
        return refrescoId;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public BigDecimal getDesviacion() {
        return desviacion;
    }
}
