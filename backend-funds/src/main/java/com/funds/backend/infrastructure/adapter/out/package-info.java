/**
 * Adaptadores de salida (driven adapters).
 *
 * <p>Implementan los puertos de salida del dominio: persistencia (Spring Data JPA),
 * conectores a fuentes externas (APIs/scraping), etc. Cada adaptador se ubica en un
 * subpaquete propio (p. ej. {@code persistence}, {@code web}).
 *
 * <p>En la Fase 0 no hay adaptadores de salida; este paquete establece la convención.</p>
 */
package com.funds.backend.infrastructure.adapter.out;
