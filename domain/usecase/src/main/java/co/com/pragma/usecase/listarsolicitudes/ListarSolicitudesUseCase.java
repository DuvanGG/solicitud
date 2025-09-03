package co.com.pragma.usecase.listarsolicitudes;

import java.util.List;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
@RequiredArgsConstructor
public class ListarSolicitudesUseCase {
    private final SolicitudRepository solicitudRepository;

    public Flux<Solicitud> listarSolicitudesPendientes(int limit, int offset) {
        List<Integer> estadosRevision = List.of(1 /*Pendiente*/, 2 /*Rechazada*/, 3 /*Revision Manual*/);
        return solicitudRepository.listSolicitudesPorEstadosPaginado(estadosRevision, limit, offset);
    }
}
