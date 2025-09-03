package co.com.pragma.model.solicitud.gateways;

import java.util.List;

import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {
	Mono<Solicitud> save(Solicitud solicitud);
	
	Flux<Solicitud> listSolicitudesPorEstadosPaginado(List<Integer> estados, int limit, int offset);
}