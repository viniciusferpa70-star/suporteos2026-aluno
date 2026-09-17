package com.curso.suporteos.application;

import com.curso.suporteos.domain.GrupoProduto;
import com.curso.suporteos.domain.Produto;
import com.curso.suporteos.repository.GrupoProdutoRepository;
import com.curso.suporteos.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RollbackTest {
    @Autowired PlatformTransactionManager manager;
    @Autowired GrupoProdutoRepository grupos;
    @Autowired ProdutoRepository produtos;
    @Autowired ProdutoService service;

    @Test
    void deveDesfazerCadastroJaEnviadoAoBancoAposFalhaNoCasoDeUso() {
        String codigo = "ROLLBACK-" + UUID.randomUUID();
        Long[] ids = new Long[2];
        TransactionTemplate tx = new TransactionTemplate(manager);
        assertThrows(IllegalStateException.class, () -> tx.executeWithoutResult(status -> {
            GrupoProduto grupo = grupos.saveAndFlush(new GrupoProduto(codigo));
            Produto produto = service.cadastrar(new Produto(codigo, "Mouse", BigDecimal.TEN,
                BigDecimal.TEN, LocalDate.of(2026,9,10)), grupo.getId(), null);
            produtos.flush();
            ids[0] = grupo.getId(); ids[1] = produto.getId();
            assertTrue(produtos.existsById(ids[1]));
            throw new IllegalStateException("Falha simulada depois da gravacao");
        }));
        assertFalse(produtos.existsByCodigoBarras(codigo));
        assertFalse(grupos.existsById(ids[0]));
    }
}
