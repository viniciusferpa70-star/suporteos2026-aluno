# Tema atual: Petshop — Banho e Tosa

O projeto individual implementado usa Tutor, Pet e Atendimento. Um tutor tem vários pets; cada pet tem vários atendimentos. O atendimento tem código único, serviço (BANHO, TOSA ou BANHO_E_TOSA), data/hora, valor positivo, observações e status AGENDADO/CONCLUIDO. A API valida entradas, impede duplicidade e conclusão repetida.

Demonstração: tutora fictícia Ana → pet Luna → banho e tosa por R$ 95,50 → consulta → conclusão.

## Referência original do professor (não é o tema implementado)

O conteúdo abaixo foi preservado como material de referência das aulas. O exemplo anterior de produtos também está preservado na branch de backup.

## Identificação

- Nome: `suporteos2026`
- Tema: controle simplificado de itens de estoque
- Objetivo: cadastrar produtos e organizá-los por grupos de produtos

## Entidade de classificação

- Nome no singular: `GrupoProduto`
- Nome no plural: grupos de produtos
- Descrição: classificação utilizada para organizar produtos
- Exemplos: Limpeza, Alimentos e Papelaria
- Status: ativo ou inativo

## Entidade principal

- Nome no singular: `Produto`
- Nome no plural: produtos
- Código único: código de barras
- Descrição: nome ou descrição comercial do produto
- Medida quantitativa: saldo em estoque
- Valor monetário: valor unitário
- Valor calculado: saldo em estoque multiplicado pelo valor unitário
- Data relevante: data de cadastro
- Status: ativo ou inativo

## Relacionamento

- Um grupo de produtos pode classificar vários produtos.
- Cada produto pertence a um grupo de produtos.

## Exemplos

| Grupo | Código | Produto | Saldo | Valor unitário |
|---|---|---|---:|---:|
| Limpeza | `7890000000001` | Detergente | 20 | 3,50 |
| Alimentos | `7890000000002` | Arroz | 15 | 24,90 |
| Papelaria | `7890000000003` | Caderno | 10 | 18,00 |

## Correspondência para os projetos dos estudantes

Cada estudante escolherá outro tema, mas deverá manter uma estrutura conceitualmente equivalente:

```text
GrupoProduto -> entidade de classificação do tema
Produto      -> entidade principal do tema
```

O tema individual deverá permitir código único, descrição, medida quantitativa, valor monetário, data, status e relacionamento com a classificação.
