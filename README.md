# Petshop 2026 — Banho e Tosa

Projeto didático Java 21 / Spring Boot 4, PostgreSQL e Liquibase. Novo domínio independente da demonstração de produtos, preservada como backup.

## Modelo e regras

- Tutor: nome, e-mail único (normalizado em minúsculas), telefone.
- Pet: nome, espécie (CACHORRO ou GATO), raça e tutor obrigatório. Um tutor pode ter vários pets.
- Atendimento: código único, pet, serviço (BANHO, TOSA ou BANHO_E_TOSA), data/hora futura no cadastro, valor positivo, observações e estado AGENDADO/CONCLUIDO.
- Cada atendimento pertence a um pet. Concluir duas vezes retorna conflito.
- Dados da coleção são fictícios. Datas são geradas para amanhã; cada execução gera identificadores novos.

## Arquitetura

Controller HTTP → DTO com Bean Validation → Service transacional → Repository JPA → PostgreSQL.
O mapper transforma entidades em respostas dentro da transação, sem expor relacionamentos JPA diretamente no JSON.
Liquibase cria as tabelas; Hibernate apenas valida o esquema. A migração 004 adiciona observações preservando registros.

## Executar

1. Configure Java 21 e PostgreSQL 17.
2. Crie os bancos com `scripts/criar-bancos.sql`, um comando por vez no pgAdmin, conectado como administrador.
3. Copie `.env.example` para `.env` e configure as credenciais locais. Nunca versione `.env`.
4. `./mvnw.cmd test` executa testes de domínio, API, migrações e rollback no banco petshop2026_test.
5. `./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev` inicia na porta 8080.
6. Importe `postman/Petshop2026.postman_collection.json` e execute a coleção na ordem.

Os bancos locais são petshop2026_dev/test/diff/reference. O usuário local suporteos_app foi reaproveitado; isso não muda o tema nem acessa as tabelas do banco antigo. Em produção, configure DB_URL, DB_USERNAME e DB_PASSWORD e use o perfil prod.

## API

| Recurso | Operações |
|---|---|
| /api/health | GET |
| /api/tutores | POST, GET |
| /api/tutores/{id} | GET |
| /api/pets | POST, GET |
| /api/pets/{id} | GET |
| /api/atendimentos | POST, GET |
| /api/atendimentos/{id} | GET |
| /api/atendimentos/{id}/concluir | PATCH |

Respostas: 201 criação; 200 consulta/conclusão; 400 validação/JSON; 404 referência ou recurso ausente; 409 duplicidade/conclusão repetida.

## Conteúdo didático

Há modelagem, persistência, perfis, changelogs, repositories, transações, DTOs, mapper e testes HTTP. A mudança de tema não fabrica commits ou tags retroativos de aulas. Revisões teóricas e eventuais exigências específicas do professor devem ser conferidas à parte.

Os materiais em `docs/00aula` a `docs/07aula` são referências do curso e podem apresentar o exemplo de produtos do professor. O tema implementado nesta branch é o petshop. Veja o [roteiro da apresentação](docs/APRESENTAR-PETSHOP.md).

Validação em 17/09/2026: 14 testes Java aprovados e 14 requisições com 24 verificações aprovadas no Newman e na interface do Postman.

## Backup

A versão anterior está preservada na branch [backup/produtos-2026-09-17](https://github.com/viniciusferpa70-star/suporteos2026-aluno/tree/backup/produtos-2026-09-17). O histórico anterior permanece acessível. Os bancos do petshop são separados dos bancos antigos.

Backups locais do banco, arquivos `.env` e credenciais não são publicados neste repositório.
