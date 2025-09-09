package co.com.pragma.usecase.actualizarestadosolicitud;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class ActualizarEstadoSolicitudUseCase {
	
	private final SolicitudRepository solicitudRepository;
	
	public Mono<Solicitud> ejecutar(Long idSolicitud, Integer nuevoEstado) {
	    return solicitudRepository.findById(idSolicitud) // busco la entidad actual
	        .switchIfEmpty(Mono.error(new IllegalArgumentException("Solicitud no encontrada")))
	        .flatMap(solicitud -> {
	            solicitud.setIdEstado(nuevoEstado);   
	            return solicitudRepository.save(solicitud); 
	        });
	}
}
