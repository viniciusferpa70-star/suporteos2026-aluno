package com.curso.petshop.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "pet")
public class Pet {
    public enum Especie { CACHORRO, GATO }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80)
    private String nome;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private Especie especie;
    @Column(nullable = false, length = 80)
    private String raca;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    protected Pet() {}
    public Pet(String nome, Especie especie, String raca, Tutor tutor) {
        if (nome == null || nome.isBlank() || especie == null || raca == null || raca.isBlank() || tutor == null)
            throw new IllegalArgumentException("Nome, espécie, raça e tutor são obrigatórios");
        this.nome = nome.trim(); this.especie = especie; this.raca = raca.trim(); this.tutor = tutor;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Especie getEspecie() { return especie; }
    public String getRaca() { return raca; }
    public Tutor getTutor() { return tutor; }
}
