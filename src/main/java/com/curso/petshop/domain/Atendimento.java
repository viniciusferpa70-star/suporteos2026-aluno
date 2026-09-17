package com.curso.petshop.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "atendimento")
public class Atendimento {
    public enum Servico { BANHO, TOSA, BANHO_E_TOSA }
    public enum Status { AGENDADO, CONCLUIDO }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 60)
    private String codigo;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private Servico servico;
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private Status status;
    @Column(length = 500)
    private String observacoes;

    protected Atendimento() {}
    public Atendimento(String codigo, Pet pet, Servico servico, LocalDateTime dataHora,
                       BigDecimal valor, String observacoes) {
        if (codigo == null || codigo.isBlank() || pet == null || servico == null || dataHora == null)
            throw new IllegalArgumentException("Código, pet, serviço e data são obrigatórios");
        if (valor == null || valor.signum() <= 0 || valor.scale() > 2)
            throw new IllegalArgumentException("Valor deve ser positivo, com até duas casas decimais");
        this.codigo = codigo.trim(); this.pet = pet; this.servico = servico;
        this.dataHora = dataHora; this.valor = valor; this.observacoes = observacoes;
        this.status = Status.AGENDADO;
    }
    public void concluir() {
        if (status != Status.AGENDADO) throw new IllegalStateException("Atendimento já concluído");
        status = Status.CONCLUIDO;
    }
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public Pet getPet() { return pet; }
    public Servico getServico() { return servico; }
    public LocalDateTime getDataHora() { return dataHora; }
    public BigDecimal getValor() { return valor; }
    public Status getStatus() { return status; }
    public String getObservacoes() { return observacoes; }
}
