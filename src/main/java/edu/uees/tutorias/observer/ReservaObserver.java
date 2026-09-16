package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.Reserva;

/** Receptor de eventos producidos por cambios relevantes en una reserva. */
public interface ReservaObserver {
    void actualizar(Reserva reserva, String evento);
}
