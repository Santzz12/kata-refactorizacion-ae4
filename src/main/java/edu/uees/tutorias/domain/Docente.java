package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Representa al docente y concentra la administración de sus horarios.
 */
public final class Docente extends Usuario {
    private final String especialidad;
    private final List<HorarioDisponible> horarios = new ArrayList<>();

    public Docente(String nombre, String email, String especialidad) {
        super(nombre, email);
        if (especialidad == null || especialidad.isBlank()) {
            throw new IllegalArgumentException("La especialidad es obligatoria");
        }
        this.especialidad = especialidad.trim();
    }

    public Docente(UUID id, String nombre, String email, String especialidad) {
        super(id, nombre, email);
        if (especialidad == null || especialidad.isBlank()) {
            throw new IllegalArgumentException("La especialidad es obligatoria");
        }
        this.especialidad = especialidad.trim();
    }

    public HorarioDisponible publicarHorario(LocalDateTime inicio, LocalDateTime fin) {
        validarSolapamiento(inicio, fin);
        HorarioDisponible horario = new HorarioDisponible(this, inicio, fin);
        horarios.add(horario);
        return horario;
    }

    private void validarSolapamiento(LocalDateTime inicio, LocalDateTime fin) {
        for (HorarioDisponible horario : horarios) {
            if (horario.seSuperpone(inicio, fin)) {
                throw new IllegalArgumentException("El nuevo horario se superpone con otro horario del docente");
            }
        }
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public List<HorarioDisponible> getHorarios() {
        return Collections.unmodifiableList(horarios);
    }
}
