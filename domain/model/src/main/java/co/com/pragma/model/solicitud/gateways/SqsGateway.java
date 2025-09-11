package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface SqsGateway {
	
	 Mono<String> enviarMensaje(Solicitud solicitud);

}


