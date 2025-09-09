package co.com.pragma.api;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.responses.SolicitudPageResponse;
import co.com.pragma.usecase.actualizarestadosolicitud.ActualizarEstadoSolicitudUseCase;
import co.com.pragma.usecase.listarsolicitudes.ListarSolicitudesUseCase;
import co.com.pragma.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
//private  final UseCase useCase;
//private  final UseCase2 useCase2;
	private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
	private final ListarSolicitudesUseCase listarSolicitudesUseCase;
	private final ActualizarEstadoSolicitudUseCase actualizarEstadoSolicitudUseCase;

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
    
    public Mono<ServerResponse> listarSolicitudes(ServerRequest request) {
        int limit = Integer.parseInt(request.queryParam("limit").orElse("10"));
        int offset = Integer.parseInt(request.queryParam("offset").orElse("0"));

        //log.info("Inicio listado solicitudes para revisión manual. limit={}, offset={}", limit, offset);

        return listarSolicitudesUseCase.listarSolicitudesPendientes(limit, offset)
            .collectList()
            .flatMap(solicitudes -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(solicitudes))
            .doOnError(e -> System.out.println("Error listado solicitudes" + e))
            .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(new ErrorResponse("Error procesando la solicitud: " + e.getMessage())));
    }
    
    public Mono<ServerResponse> listarSolicitudesPendiendientes(ServerRequest request) {
        int limit = request.queryParam("limit")
                           .map(Integer::parseInt)
                           .orElse(5);
        Long lastId = request.queryParam("lastId")
                             .map(Long::parseLong)
                             .orElse(null);

        List<Integer> estados = List.of(1);

        return listarSolicitudesUseCase.listarSolicitudesPendientesConCursor(estados, lastId, limit)
            .collectList()
            .flatMap(items -> {
                Long nextCursor = items.isEmpty() ? null : items.get(items.size() - 1).getId();
                SolicitudPageResponse response = new SolicitudPageResponse(items, nextCursor);
                return ServerResponse.ok().bodyValue(response);
            });
    }
    
    public Mono<ServerResponse> actualizarEstado(ServerRequest request) {
    	System.out.println("Entrando a actualizarEstado con id: " + request.pathVariable("id"));
        Long id = Long.valueOf(request.pathVariable("id"));
        Integer nuevoEstado = Integer.valueOf(request.queryParam("nuevoEstado")
                                   .orElseThrow(() -> new IllegalArgumentException("nuevoEstado es requerido")));

        return actualizarEstadoSolicitudUseCase.ejecutar(id, nuevoEstado)
            .flatMap(solicitudActualizada ->
                ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(solicitudActualizada))
            .onErrorResume(e ->
                ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ErrorResponse("Error actualizando estado: " + e.getMessage())));
    }
    
    
}
