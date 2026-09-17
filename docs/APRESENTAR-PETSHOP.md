# Apresentação pelo Postman

1. Inicie PostgreSQL e a aplicação com Java 21 e perfil dev, conforme o README.
2. Importe `postman/Petshop2026.postman_collection.json`.
3. Selecione a coleção **Petshop 2026 - Banho e Tosa - Apresentação** e clique Run.
4. Mantenha as 14 requisições selecionadas, uma iteração, e clique Start run.
5. Resultado esperado: 24 verificações aprovadas, nenhuma falha.

Para demonstrar passo a passo, envie as requisições 00 a 13 na ordem. O 00 gera identificadores únicos e a data de amanhã. Os IDs são encadeados automaticamente.

| Etapas | Demonstração |
|---|---|
| 00 | API disponível |
| 01–02 | Cadastrar tutora Ana e pet Luna |
| 03 | Agendar banho e tosa por R$ 95,50 |
| 04–05 | Consultar e listar atendimentos |
| 06–09 | Dados inválidos 400, inexistente 404, código repetido 409 e JSON inválido 400 |
| 10–11 | Concluir e verificar persistência do status CONCLUIDO |
| 12–13 | Impedir conclusão repetida e consultar pet/tutor |

Os erros 400, 404 e 409 dessa sequência são esperados. Para repetir toda a apresentação, recomece pelo 00.

No pgAdmin, conecte o Query Tool a `petshop2026_dev` e execute `scripts/consultas.sql`. A primeira consulta junta atendimento, pet e tutor. Não recrie bancos durante a apresentação.

“O controller recebe JSON, o DTO valida, o serviço aplica as regras dentro de transações e o repository persiste no PostgreSQL. Liquibase versiona o esquema e Hibernate valida o mapeamento.”
