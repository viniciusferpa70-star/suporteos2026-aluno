package com.curso.petshop;
import com.curso.petshop.domain.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PetshopDomainTest {
    private Pet pet() { return new Pet("Luna", Pet.Especie.CACHORRO, "SRD", new Tutor("Ana", "ANA@example.com", "11999990000")); }
    @Test void normalizaEmail() { assertEquals("ana@example.com", pet().getTutor().getEmail()); }
    @Test void exigeTutor() { assertThrows(IllegalArgumentException.class, ()->new Pet("Luna",Pet.Especie.GATO,"SRD",null)); }
    @Test void rejeitaValorNegativo() {
        assertThrows(IllegalArgumentException.class, ()->new Atendimento("A1",pet(),Atendimento.Servico.BANHO,LocalDateTime.now(),new BigDecimal("-1"),null));
    }
    @Test void concluiApenasUmaVez() {
        var a=new Atendimento("A1",pet(),Atendimento.Servico.BANHO,LocalDateTime.now(),new BigDecimal("80.00"),null);
        assertEquals(Atendimento.Status.AGENDADO,a.getStatus());
        a.concluir(); assertEquals(Atendimento.Status.CONCLUIDO,a.getStatus());
        assertThrows(IllegalStateException.class,a::concluir);
    }
}
