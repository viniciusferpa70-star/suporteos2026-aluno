package com.curso.petshop;

import com.curso.petshop.domain.*;
import com.curso.petshop.repository.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") @Transactional
class PetshopApiTest {
    @Autowired MockMvc mvc;
    @Autowired TutorRepository tutores;
    @Autowired PetRepository pets;
    private String body(String codigo) {
        var t=tutores.save(new Tutor("Tutor de teste","teste@example.com","11999990000"));
        var p=pets.save(new Pet("Luna",Pet.Especie.CACHORRO,"SRD",t));
        return """
            {"codigo":"%s","petId":%d,"servico":"BANHO_E_TOSA","dataHora":"%s","valor":95.50,"observacoes":"Shampoo neutro"}
            """.formatted(codigo,p.getId(),LocalDateTime.now().plusDays(1));
    }
    @Test void fluxoCompleto() throws Exception {
        String location=mvc.perform(post("/api/atendimentos").contentType(MediaType.APPLICATION_JSON).content(body("FLUXO")))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("AGENDADO"))
            .andExpect(jsonPath("$.petNome").value("Luna"))
            .andExpect(jsonPath("$.tutorNome").value("Tutor de teste"))
            .andReturn().getResponse().getHeader("Location");
        mvc.perform(get(location)).andExpect(status().isOk()).andExpect(jsonPath("$.valor").value(95.50));
        mvc.perform(patch(location+"/concluir")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONCLUIDO"));
        mvc.perform(patch(location+"/concluir")).andExpect(status().isConflict());
    }
    @Test void codigoRepetido() throws Exception {
        String json=body("REPETIDO");
        mvc.perform(post("/api/atendimentos").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
        mvc.perform(post("/api/atendimentos").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isConflict());
    }
    @Test void camposInvalidos() throws Exception {
        mvc.perform(post("/api/atendimentos").contentType(MediaType.APPLICATION_JSON).content("{\"valor\":-1}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fields.valor").exists()).andExpect(jsonPath("$.fields.petId").exists());
    }
    @Test void dataPassada() throws Exception {
        String json=body("PASSADO").replaceAll("\"dataHora\":\"[^\"]+\"","\"dataHora\":\"2000-01-01T10:00:00\"");
        mvc.perform(post("/api/atendimentos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fields.dataHora").exists());
    }
    @Test void recursoInexistente() throws Exception {
        mvc.perform(get("/api/atendimentos/9223372036854775807")).andExpect(status().isNotFound());
        mvc.perform(post("/api/pets").contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Luna\",\"especie\":\"GATO\",\"raca\":\"SRD\",\"tutorId\":9223372036854775807}"))
            .andExpect(status().isNotFound());
    }
    @Test void emailRepetidoIgnoraMaiusculas() throws Exception {
        tutores.save(new Tutor("Ana","ana@example.com","11999990000"));
        mvc.perform(post("/api/tutores").contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Ana\",\"email\":\"ANA@example.com\",\"telefone\":\"11999990000\"}"))
            .andExpect(status().isConflict());
    }
    @Test void jsonInvalido() throws Exception {
        mvc.perform(post("/api/atendimentos").contentType(MediaType.APPLICATION_JSON).content("{invalido"))
            .andExpect(status().isBadRequest());
    }
    @Test void especieInvalida() throws Exception {
        mvc.perform(post("/api/pets").contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"Luna\",\"especie\":\"INVALIDA\",\"raca\":\"SRD\",\"tutorId\":1}"))
            .andExpect(status().isBadRequest());
    }
}
