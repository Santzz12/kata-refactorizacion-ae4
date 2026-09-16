package edu.uees.tutorias.repository;

import edu.uees.tutorias.domain.Reserva;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de persistencia: la lógica de aplicación conoce esta abstracción y no una base de datos concreta.
 */
public interface ReservaRepository {
    Reserva guardar(Reserva reserva);
    Optional<Reserva> buscarPorId(UUID id);
    List<Reserva> listar();
}
