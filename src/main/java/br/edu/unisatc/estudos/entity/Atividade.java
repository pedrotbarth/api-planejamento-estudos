package br.edu.unisatc.estudos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "atividades")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private TipoAtividade tipo;

    @Column(nullable = false)
    private LocalDate dataLimite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private StatusAtividade status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    protected Atividade() {
    }

    public Atividade(String titulo, TipoAtividade tipo, LocalDate dataLimite, Disciplina disciplina) {
        atualizar(titulo, tipo, dataLimite, StatusAtividade.PENDENTE, disciplina);
    }

    public void atualizar(String titulo, TipoAtividade tipo, LocalDate dataLimite, StatusAtividade status, Disciplina disciplina) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.dataLimite = dataLimite;
        this.status = status;
        this.disciplina = disciplina;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public TipoAtividade getTipo() {
        return tipo;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public StatusAtividade getStatus() {
        return status;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }
}
