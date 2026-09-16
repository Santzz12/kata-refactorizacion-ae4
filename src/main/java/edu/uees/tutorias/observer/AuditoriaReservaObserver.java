package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.Reserva;

/** Observer sencillo para evidenciar auditoría desacoplada del servicio. */
public final class AuditoriaReservaObserver implements ReservaObserver {
    @Override
    public void actualizar(Reserva reserva, String evento) {
        System.out.printf("[AUDITORIA] Reserva %s | %s | estado=%s%n",
                reserva.getId(), evento, reserva.getEstado());
    }
}
