package com.curso.petshop.api;

import com.curso.petshop.domain.Atendimento;
import com.curso.petshop.domain.Pet;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class PetshopDto {
    private PetshopDto() {}
    public record TutorRequest(
            @NotBlank @Size(max=120) String nome,
            @NotBlank @Email @Size(max=160) String email,
            @NotBlank @Pattern(regexp="[0-9]{10,11}", message="Informe 10 ou 11 dígitos") String telefone) {}
    public record TutorResponse(Long id, String nome, String email, String telefone) {}
    public record PetRequest(
            @NotBlank @Size(max=80) String nome,
            @NotNull Pet.Especie especie,
            @NotBlank @Size(max=80) String raca,
            @NotNull @Positive Long tutorId) {}
    public record PetResponse(Long id, String nome, Pet.Especie especie, String raca, Long tutorId, String tutorNome) {}
    public record AtendimentoRequest(
            @NotBlank @Size(max=60) String codigo,
            @NotNull @Positive Long petId,
            @NotNull Atendimento.Servico servico,
            @NotNull @Future LocalDateTime dataHora,
            @NotNull @DecimalMin("0.01") @Digits(integer=8, fraction=2) BigDecimal valor,
            @Size(max=500) String observacoes) {}
    public record AtendimentoResponse(Long id, String codigo, Long petId, String petNome,
            Long tutorId, String tutorNome, Atendimento.Servico servico, LocalDateTime dataHora,
            BigDecimal valor, Atendimento.Status status, String observacoes) {}
}
