package edu.uees.tutorias.builder;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;

import java.util.UUID;

/** Construcción progresiva de Reserva con valores por defecto explícitos. */
public final class ReservaBuilder {
    private UUID id = UUID.randomUUID();
    private Estudiante estudiante;
    private HorarioDisponible horario;
    private String tema = "Sin especificar";
    private String observaciones = "Sin observaciones";
    private boolean enviarRecordatorio;

    public ReservaBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public ReservaBuilder estudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        return this;
    }

    public ReservaBuilder horario(HorarioDisponible horario) {
        this.horario = horario;
        return this;
    }

    public ReservaBuilder tema(String tema) {
        this.tema = tema;
        return this;
    }

    public ReservaBuilder observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public ReservaBuilder enviarRecordatorio(boolean enviarRecordatorio) {
        this.enviarRecordatorio = enviarRecordatorio;
        return this;
    }

    public Reserva build() {
        if (id == null) {
            throw new IllegalStateException("El id es obligatorio");
        }
        if (estudiante == null) {
            throw new IllegalStateException("El estudiante es obligatorio");
        }
        if (horario == null) {
            throw new IllegalStateException("El horario es obligatorio");
        }
        return new Reserva(id, estudiante, horario, EstadoReserva.SOLICITADA,
                tema, observaciones, enviarRecordatorio);
    }
}
