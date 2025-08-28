package co.com.pragma.usecase.registrarsolicitud;

import java.util.Set;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {
	
	private final SolicitudRepository solicitudRepository;
	
	private final Set<String> tiposPrestamoValidos = Set.of("PERSONAL", "HIPOTECARIO", "VEHICULO");
	
	public Mono<Solicitud> registrarSolicitud(Solicitud solicitud) {
		// log.info("Iniciando registro de solicitud para cliente {}",
		// solicitud.getDocumentoCliente());

		// Validaciones de negocio
//	        if (solicitud.getDocumentoCliente() == null || solicitud.getDocumentoCliente().isBlank()) {
//	            return Mono.error(new IllegalArgumentException("El documento del cliente es obligatorio"));
//	        }
		if (solicitud.getMonto() == null || solicitud.getMonto() <= 0) {
			return Mono.error(new IllegalArgumentException("El monto debe ser mayor que 0"));
		}
		if (solicitud.getPlazo() == null || solicitud.getPlazo() <= 0) {
			return Mono.error(new IllegalArgumentException("El plazo debe ser mayor que 0"));
		}
//		if (!tiposPrestamoValidos.contains(solicitud.getIdTipoPrestamo())) {
//			return Mono.error(new IllegalArgumentException("El tipo de préstamo no es válido"));
//		}

		solicitud.setIdEstado(1);

//		return solicitudRepository.save(solicitud)
//				.doOnSuccess(s -> log.info("Solicitud registrada con id {}", s.getId()))
//				.doOnError(e -> log.error("Error registrando solicitud", e));
		return solicitudRepository.save(solicitud);
	}
	
}
