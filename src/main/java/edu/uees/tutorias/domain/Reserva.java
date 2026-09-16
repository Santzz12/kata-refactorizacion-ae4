package edu.uees.tutorias.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Registra el encuentro solicitado por un estudiante en un horario de tutoría.
 * El objeto protege las transiciones válidas de estado.
 */
public final class Reserva {
    private final UUID id;
    private final Estudiante estudiante;
    private HorarioDisponible horario;
    private EstadoReserva estado;
    private final String tema;
    private final String observaciones;
    private final boolean enviarRecordatorio;

    public Reserva(Estudiante estudiante, HorarioDisponible horario) {
        this(UUID.randomUUID(), estudiante, horario, EstadoReserva.SOLICITADA,
                "Sin especificar", "Sin observaciones", false);
    }

    public Reserva(UUID id, Estudiante estudiante, HorarioDisponible horario, EstadoReserva estado) {
        this(id, estudiante, horario, estado, "Sin especificar", "Sin observaciones", false);
    }

    public Reserva(UUID id, Estudiante estudiante, HorarioDisponible horario, EstadoReserva estado,
                   String tema, String observaciones, boolean enviarRecordatorio) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.estudiante = Objects.requireNonNull(estudiante, "El estudiante es obligatorio");
        this.horario = Objects.requireNonNull(horario, "El horario es obligatorio");
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
        this.tema = normalizarTexto(tema, "Sin especificar");
        this.observaciones = normalizarTexto(observaciones, "Sin observaciones");
        this.enviarRecordatorio = enviarRecordatorio;
    }

    private static String normalizarTexto(String valor, String valorPorDefecto) {
        return valor == null || valor.isBlank() ? valorPorDefecto : valor.trim();
    }

    public void confirmar() {
        if (estado != EstadoReserva.SOLICITADA && estado != EstadoReserva.REPROGRAMADA) {
            throw new IllegalStateException("Solo una reserva solicitada o reprogramada puede confirmarse");
        }
        estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("La reserva no puede cancelarse en su estado actual");
        }
        estado = EstadoReserva.CANCELADA;
    }

    public void reprogramar(HorarioDisponible nuevoHorario) {
        Objects.requireNonNull(nuevoHorario, "El nuevo horario es obligatorio");
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("La reserva no puede reprogramarse en su estado actual");
        }
        if (!horario.getDocente().getId().equals(nuevoHorario.getDocente().getId())) {
            throw new IllegalArgumentException("Una reprogramación debe conservar al mismo docente");
        }
        horario = nuevoHorario;
        estado = EstadoReserva.REPROGRAMADA;
    }

    public void completar() {
        if (estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo una reserva confirmada puede marcarse como completada");
        }
        estado = EstadoReserva.COMPLETADA;
    }

    public UUID getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public HorarioDisponible getHorario() {
        return horario;
    }

    public Docente getDocente() {
        return horario.getDocente();
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public String getTema() {
        return tema;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public boolean isEnviarRecordatorio() {
        return enviarRecordatorio;
    }
}
