package com.curso.suporteos.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class GarantiaTest {
    @Test
    void devePreservarGarantiaQuandoNovoPrazoForInvalido() {
        Produto p = new Produto("INF-1", "Mouse", BigDecimal.TEN, BigDecimal.TEN, LocalDate.of(2026,9,10));
        p.definirGarantiaMeses(12);
        assertThrows(IllegalArgumentException.class, () -> p.definirGarantiaMeses(-1));
        assertThrows(IllegalArgumentException.class, () -> p.definirGarantiaMeses(61));
        assertEquals(12, p.getGarantiaMeses());
    }
    @Test
    void deveAceitarOsLimitesDaGarantia() {
        Produto p = new Produto("INF-2", "Teclado", BigDecimal.TEN, BigDecimal.TEN, LocalDate.of(2026,9,10));
        assertEquals(0, p.getGarantiaMeses());
        p.definirGarantiaMeses(60);
        assertEquals(60, p.getGarantiaMeses());
    }
}
