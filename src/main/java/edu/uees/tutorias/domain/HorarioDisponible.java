package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/** Franja de tiempo publicada por un docente para recibir tutorías. */
public final class HorarioDisponible {
    private final UUID id;
    private final Docente docente;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;
    private boolean reservado;

    public HorarioDisponible(Docente docente, LocalDateTime inicio, LocalDateTime fin) {
        this(UUID.randomUUID(), docente, inicio, fin, false);
    }

    public HorarioDisponible(UUID id, Docente docente, LocalDateTime inicio, LocalDateTime fin, boolean reservado) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.docente = Objects.requireNonNull(docente, "El docente es obligatorio");
        this.inicio = Objects.requireNonNull(inicio, "La fecha de inicio es obligatoria");
        this.fin = Objects.requireNonNull(fin, "La fecha de fin es obligatoria");
        if (!fin.isAfter(inicio)) {
            throw new IllegalArgumentException("El fin del horario debe ser posterior al inicio");
        }
        this.reservado = reservado;
    }

    public boolean estaDisponible() {
        return !reservado;
    }

    public void reservar() {
        if (reservado) {
            throw new IllegalStateException("El horario ya se encuentra reservado");
        }
        reservado = true;
    }

    public void liberar() {
        reservado = false;
    }

    public boolean seSuperpone(LocalDateTime otroInicio, LocalDateTime otroFin) {
        Objects.requireNonNull(otroInicio, "El inicio es obligatorio");
        Objects.requireNonNull(otroFin, "El fin es obligatorio");
        return inicio.isBefore(otroFin) && otroInicio.isBefore(fin);
    }

    public UUID getId() {
        return id;
    }

    public Docente getDocente() {
        return docente;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }
}
