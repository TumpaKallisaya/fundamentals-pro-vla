package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;

import com.funds.backend.domain.model.Direccion;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/** Mapeo JPA del value object {@code Regla}. */
@Embeddable
public class ReglaEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "regla_direccion")
    private Direccion direccion;

    @Column(name = "regla_peso")
    private int peso;

    @Column(name = "regla_tolerancia_pct")
    private BigDecimal toleranciaPct;

    protected ReglaEmbeddable() {
    }

    public ReglaEmbeddable(Direccion direccion, int peso, BigDecimal toleranciaPct) {
        this.direccion = direccion;
        this.peso = peso;
        this.toleranciaPct = toleranciaPct;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public int getPeso() {
        return peso;
    }

    public BigDecimal getToleranciaPct() {
        return toleranciaPct;
    }
}
