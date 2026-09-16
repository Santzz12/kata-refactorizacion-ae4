package edu.uees.tutorias.repository;

import edu.uees.tutorias.domain.Reserva;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Implementación simple en memoria para demostrar desacoplamiento de persistencia. */
public final class MemoriaReservaRepository implements ReservaRepository {
    private final Map<UUID, Reserva> reservas = new LinkedHashMap<>();

    @Override
    public Reserva guardar(Reserva reserva) {
        reservas.put(reserva.getId(), reserva);
        return reserva;
    }

    @Override
    public Optional<Reserva> buscarPorId(UUID id) {
        return Optional.ofNullable(reservas.get(id));
    }

    @Override
    public List<Reserva> listar() {
        return new ArrayList<>(reservas.values());
    }
}
