package com.curso.suporteos.domain;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import javax.sql.DataSource;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MigracaoGarantiaTest {
    @Autowired DataSource dataSource;

    @Test
    void deveMigrarRegistroAntigoSemPerderSaldoOuDescricao() throws Exception {
        String schema = "teste_migracao_" + UUID.randomUUID().toString().replace("-", "");
        try (var connection = dataSource.getConnection()) {
            try (var stmt = connection.createStatement()) { stmt.execute("CREATE SCHEMA " + schema); }
            try {
                connection.setSchema(schema);
                var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                database.setDefaultSchemaName(schema);
                database.setLiquibaseSchemaName(schema);
                var resources = new ClassLoaderResourceAccessor();
                new Liquibase("db/changelog/baseline-aula-06.yaml", resources, database).update("");
                try (var stmt = connection.createStatement()) {
                    stmt.execute("INSERT INTO " + schema + ".grupo_produto(id,nome,status) VALUES(1,'Perifericos antigos','ATIVO')");
                    stmt.execute("INSERT INTO " + schema + ".produto(codigo_barras,descricao,saldo_estoque,valor_unitario,estoque_minimo,data_cadastro,status,grupo_produto_id) VALUES('LEGADO','Mouse antigo',5,10,1,DATE '2026-09-01','ATIVO',1)");
                }
                connection.commit();
                new Liquibase("db/changelog/db.changelog-master.yaml", resources, database).update("");
                try (var stmt = connection.createStatement(); var rs = stmt.executeQuery("SELECT descricao,saldo_estoque,garantia_meses FROM " + schema + ".produto WHERE codigo_barras='LEGADO'")) {
                    assertTrue(rs.next());
                    assertEquals("Mouse antigo", rs.getString(1));
                    assertEquals(5, rs.getBigDecimal(2).intValueExact());
                    assertEquals(0, rs.getInt(3));
                }
                try (var stmt = connection.createStatement()) {
                    assertThrows(java.sql.SQLException.class, () -> stmt.execute("UPDATE " + schema + ".produto SET garantia_meses=-1"));
                }
            } finally {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.setSchema("public");
                // Remove exclusivamente o schema aleatorio criado por este teste.
                try (var stmt = connection.createStatement()) { stmt.execute("DROP SCHEMA " + schema + " CASCADE"); }
            }
        }
    }
}
