package com.curso.petshop;

import com.curso.petshop.domain.Tutor;
import com.curso.petshop.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest @ActiveProfiles("test")
class PersistenciaTest {
    @Autowired TutorRepository tutores;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;
    @Test void desfazTransacaoAposFalha() {
        String email="rollback-"+System.nanoTime()+"@example.com";
        assertThrows(IllegalStateException.class,()->new TransactionTemplate(transactionManager).execute(status->{
            tutores.saveAndFlush(new Tutor("Rollback",email,"11999990000"));
            throw new IllegalStateException("Simulação de falha");
        }));
        assertFalse(tutores.existsByEmail(email));
    }
    @Test void liquibaseAplicouEvolucao() {
        assertEquals(4,jdbc.queryForObject("SELECT count(*) FROM databasechangelog",Integer.class));
        assertEquals(1,jdbc.queryForObject("SELECT count(*) FROM information_schema.columns WHERE table_schema='public' AND table_name='atendimento' AND column_name='observacoes'",Integer.class));
    }
}
