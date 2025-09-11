package co.com.pragma.usecase.actualizarestadosolicitud;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.solicitud.gateways.SqsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class ActualizarEstadoSolicitudUseCase {
	
	private final SolicitudRepository solicitudRepository;
	private final SqsGateway sqsGateway;
	
	public Mono<Solicitud> ejecutar(Long id, Integer nuevoEstado) {
        return solicitudRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Solicitud no encontrada")))
            .flatMap(solicitud -> {
                solicitud.setIdEstado(nuevoEstado);
                return solicitudRepository.save(solicitud)
                    .flatMap(solicitudActualizada -> {
                        if (nuevoEstado == 1 || nuevoEstado == 2) {
                            return sqsGateway.enviarMensaje(solicitudActualizada)
                                      .thenReturn(solicitudActualizada);
                        }
                        return Mono.just(solicitudActualizada);
                    });
            });
    }
}
