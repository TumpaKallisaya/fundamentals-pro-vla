package com.funds.backend.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** Mapeo JPA del value object {@code Fuente}. */
@Embeddable
public class FuenteEmbeddable {

    @Column(name = "fuente_nombre")
    private String nombre;

    @Column(name = "fuente_tipo")
    private String tipo;

    @Column(name = "fuente_url")
    private String url;

    @Column(name = "fuente_notas")
    private String notas;

    protected FuenteEmbeddable() {
    }

    public FuenteEmbeddable(String nombre, String tipo, String url, String notas) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.url = url;
        this.notas = notas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUrl() {
        return url;
    }

    public String getNotas() {
        return notas;
    }
}
