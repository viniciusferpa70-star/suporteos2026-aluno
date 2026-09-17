-- No pgAdmin, abra o Query Tool do banco suporteos2026_dev.
SELECT p.id, p.codigo_barras, p.descricao, g.nome AS grupo,
       f.razao_social AS fornecedor, p.saldo_estoque, p.valor_unitario,
       p.saldo_estoque * p.valor_unitario AS valor_estoque,
       p.garantia_meses
FROM produto p
JOIN grupo_produto g ON g.id = p.grupo_produto_id
LEFT JOIN fornecedor f ON f.id = p.fornecedor_id
ORDER BY p.id DESC;

SELECT id, author, filename, dateexecuted FROM databasechangelog ORDER BY orderexecuted;
