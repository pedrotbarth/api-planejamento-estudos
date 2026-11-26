package br.edu.unisatc.estudos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "disciplinas")
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudante_id", nullable = false)
    private Estudante estudante;

    protected Disciplina() {
    }

    public Disciplina(String nome, Estudante estudante) {
        atualizar(nome, estudante);
    }

    public void atualizar(String nome, Estudante estudante) {
        this.nome = nome;
        this.estudante = estudante;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Estudante getEstudante() {
        return estudante;
    }
}
