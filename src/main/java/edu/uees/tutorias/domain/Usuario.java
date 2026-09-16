package edu.uees.tutorias.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Abstracción común para los usuarios que participan en las tutorías.
 */
public abstract class Usuario {
    private final UUID id;
    private final String nombre;
    private final String email;

    protected Usuario(UUID id, String nombre, String email) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.nombre = validarTexto(nombre, "El nombre es obligatorio");
        this.email = validarTexto(email, "El email es obligatorio");
    }

    protected Usuario(String nombre, String email) {
        this(UUID.randomUUID(), nombre, email);
    }

    private static String validarTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}
