package edu.uees.tutorias;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.factory.EmailFactory;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.observer.AuditoriaReservaObserver;
import edu.uees.tutorias.observer.NotificacionReservaObserver;
import edu.uees.tutorias.repository.MemoriaReservaRepository;
import edu.uees.tutorias.service.ServicioReservas;
import edu.uees.tutorias.strategy.CancelacionEstandar;

import java.time.LocalDateTime;

/** Demostración ejecutable del incremento Ae3. */
public final class App {
    private App() {
    }

    public static void main(String[] args) {
        Estudiante estudiante = new Estudiante(
                "Ana Torres", "ana.torres@uees.edu.ec", "Ingeniería en Computación");
        Docente docente = new Docente(
                "Carlos Ruiz", "carlos.ruiz@uees.edu.ec", "Programación");

        HorarioDisponible horario = docente.publicarHorario(
                LocalDateTime.of(2026, 9, 16, 15, 0),
                LocalDateTime.of(2026, 9, 16, 16, 0));

        Notificador notificador = new EmailFactory().crearNotificador();

        ServicioReservas servicio = new ServicioReservas(
                new MemoriaReservaRepository(),
                new CancelacionEstandar());

        servicio.registrarObserver(new NotificacionReservaObserver(notificador));
        servicio.registrarObserver(new AuditoriaReservaObserver());

        Reserva reserva = servicio.solicitarTutoria(estudiante, horario);
        servicio.confirmarReserva(reserva.getId());
        servicio.cancelarReserva(reserva.getId());

        System.out.println("Reserva final: " + reserva.getId() + " - " + reserva.getEstado());
    }
}
