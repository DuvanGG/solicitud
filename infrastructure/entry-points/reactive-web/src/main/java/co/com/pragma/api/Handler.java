package co.com.pragma.api;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
//private  final UseCase useCase;
//private  final UseCase2 useCase2;
	private final RegistrarSolicitudUseCase registrarSolicitudUseCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
    
    public Mono<ServerResponse> registrarSolicitud(ServerRequest request) {
        return request.bodyToMono(Solicitud.class)
                .flatMap(registrarSolicitudUseCase::registrarSolicitud)
                .flatMap(solicitudGuardada ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(solicitudGuardada))
                .onErrorResume(e ->
                        ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse("Error al registrar la solicitud: " + e.getMessage())));
    }
    
    record ErrorResponse(String mensaje) {}
}
