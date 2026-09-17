package com.curso.suporteos.api;

import com.curso.suporteos.domain.Fornecedor;
import com.curso.suporteos.domain.GrupoProduto;
import com.curso.suporteos.repository.FornecedorRepository;
import com.curso.suporteos.repository.GrupoProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProdutoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GrupoProdutoRepository grupoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    void deveCadastrarProdutoERetornar201() throws Exception {
        GrupoProduto grupo = grupoRepository.save(new GrupoProduto("Grupo API"));
        Fornecedor fornecedor = fornecedorRepository.save(new Fornecedor(
                "Fornecedor API",
                "22222222000192"));

        String json = """
                {
                  "codigoBarras": "API-001",
                  "descricao": "Produto criado pela API",
                  "saldoEstoque": 10.000,
                  "valorUnitario": 49.90,
                  "estoqueMinimo": 2.000,
                  "grupoId": %d,
                  "fornecedorId": %d
                }
                """.formatted(grupo.getId(), fornecedor.getId());

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.codigoBarras").value("API-001"))
                .andExpect(jsonPath("$.grupoNome").value("Grupo API"))
                .andExpect(jsonPath("$.fornecedorRazaoSocial").value("Fornecedor API"));
    }

    @Test
    void deveRetornar400ComErrosPorCampo() throws Exception {
        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Um ou mais campos são inválidos"))
                .andExpect(jsonPath("$.fields.codigoBarras").exists())
                .andExpect(jsonPath("$.fields.grupoId").exists());
    }

    @Test
    void deveRetornar409QuandoCodigoRepetir() throws Exception {
        GrupoProduto grupo = grupoRepository.save(new GrupoProduto("Grupo conflito"));
        String json = """
            {"codigoBarras":"CONFLITO-1","descricao":"Mouse","saldoEstoque":2,
             "valorUnitario":10,"estoqueMinimo":1,"grupoId":%d,"garantiaMeses":12}
            """.formatted(grupo.getId());
        mockMvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.garantiaMeses").value(12));
        mockMvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isConflict());
    }

    @Test
    void deveRejeitarGarantiaForaDoIntervalo() throws Exception {
        String json = """
            {"codigoBarras":"GARANTIA-INVALIDA","descricao":"Mouse","saldoEstoque":2,
             "valorUnitario":10,"estoqueMinimo":1,"grupoId":1,"garantiaMeses":61}
            """;
        mockMvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fields.garantiaMeses").exists());
    }

    @Test
    void deveConsultarProdutoDepoisDoCadastro() throws Exception {
        GrupoProduto grupo = grupoRepository.save(new GrupoProduto("Grupo consulta"));
        String json = """
            {"codigoBarras":"CONSULTA-1","descricao":"Teclado","saldoEstoque":3,
             "valorUnitario":20,"estoqueMinimo":1,"grupoId":%d}
            """.formatted(grupo.getId());
        String location = mockMvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
        mockMvc.perform(get(location)).andExpect(status().isOk())
            .andExpect(jsonPath("$.codigoBarras").value("CONSULTA-1"))
            .andExpect(jsonPath("$.valorEstoque").value(60));
    }

    @Test
    void deveRetornar404ParaProdutoInexistente() throws Exception {
        mockMvc.perform(get("/api/produtos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"))
                .andExpect(jsonPath("$.path").value("/api/produtos/" + Long.MAX_VALUE));
    }
}
