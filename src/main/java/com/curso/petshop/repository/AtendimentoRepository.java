package com.curso.petshop.repository;
import com.curso.petshop.domain.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    boolean existsByCodigo(String codigo);
}
