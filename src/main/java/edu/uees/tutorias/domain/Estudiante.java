package edu.uees.tutorias.domain;

import java.util.UUID;

/** Representa al estudiante que solicita una tutoría. */
public final class Estudiante extends Usuario {
    private final String carrera;

    public Estudiante(String nombre, String email, String carrera) {
        super(nombre, email);
        if (carrera == null || carrera.isBlank()) {
            throw new IllegalArgumentException("La carrera es obligatoria");
        }
        this.carrera = carrera.trim();
    }

    public Estudiante(UUID id, String nombre, String email, String carrera) {
        super(id, nombre, email);
        if (carrera == null || carrera.isBlank()) {
            throw new IllegalArgumentException("La carrera es obligatoria");
        }
        this.carrera = carrera.trim();
    }

    public String getCarrera() {
        return carrera;
    }
}
