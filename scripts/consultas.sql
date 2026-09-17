-- Conecte o Query Tool ao banco petshop2026_dev.
SELECT a.id, a.codigo, p.nome AS pet, p.especie,
       t.nome AS tutor, a.servico, a.data_hora, a.valor, a.status, a.observacoes
FROM atendimento a
JOIN pet p ON p.id = a.pet_id
JOIN tutor t ON t.id = p.tutor_id
ORDER BY a.id DESC;

SELECT * FROM tutor ORDER BY id DESC;
SELECT * FROM pet ORDER BY id DESC;
SELECT id, author, filename, dateexecuted, orderexecuted
FROM databasechangelog ORDER BY orderexecuted;

SELECT datname FROM pg_database WHERE datname IN
('petshop2026_dev','petshop2026_test','petshop2026_diff','petshop2026_reference');
