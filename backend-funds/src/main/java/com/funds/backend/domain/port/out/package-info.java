/**
 * Puertos de salida (driven ports) del dominio.
 *
 * <p>Aquí se declaran las interfaces que el dominio necesita del mundo exterior:
 * repositorios (persistencia), conectores a fuentes externas (APIs/scraping), etc.
 * Sus implementaciones viven en {@code infrastructure.adapter.out}.</p>
 *
 * <p>En la Fase 0 no existen puertos de salida; este paquete establece la convención
 * para las fases posteriores (Fase 2: repositorios; Fase 3: conectores).</p>
 */
package com.funds.backend.domain.port.out;
