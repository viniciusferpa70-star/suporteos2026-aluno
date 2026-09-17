--liquibase formatted sql

--changeset vinicius:004-observacoes
-- Evolução do modelo: orientações do tutor para banho e tosa, sem apagar cadastros.
ALTER TABLE atendimento ADD COLUMN observacoes VARCHAR(500);
