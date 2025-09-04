package co.com.pragma.r2dbc;

import co.com.pragma.entity.SolicitudEntity;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Solicitud/* change for domain model */,
    SolicitudEntity/* change for adapter model */,
    Long,
    MyReactiveRepository
> implements SolicitudRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
      
        super(repository, mapper, d -> mapper.map(d, Solicitud.class/* change for domain model */));
    }
    
    @Override
    public Mono<Solicitud> save(Solicitud solicitud) {
        return repository.save(mapper.map(solicitud, SolicitudEntity.class))
                .map(entity -> mapper.map(entity, Solicitud.class));
    }

	@Override
    public Flux<Solicitud> listSolicitudesPorEstadosPaginado(List<Integer> estados, int limit, int offset) {
        return repository.findByEstadosPaged(estados, limit, offset)
            .map(entity -> mapper.map(entity, Solicitud.class));
    }

	@Override
	public Flux<Solicitud> listSolicitudesPorEstadosPorCursor(Long lastId, List<Integer> estados, int limit) {
		return repository.findByIdGreaterThanAndIdEstadoInOrderByIdAsc(lastId, estados, limit)
				.map(MyReactiveRepositoryAdapter::toSolicitud);
	}
	
	
	public static Solicitud toSolicitud(SolicitudEntity entity) {
        if (entity == null) return null;
        Solicitud solicitud = new Solicitud();
        solicitud.setId(entity.getIdSolicitud());
        solicitud.setMonto(entity.getMonto());
        solicitud.setPlazo(entity.getPlazo());
        solicitud.setEmail(entity.getEmail());
        solicitud.setIdTipoPrestamo(entity.getIdTipoPrestamo());
        solicitud.setIdEstado(entity.getIdEstado());
        return solicitud;
    }

    public static SolicitudEntity toEntity(Solicitud domain) {
        if (domain == null) return null;
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(domain.getId());
        entity.setMonto(domain.getMonto());
        entity.setPlazo(domain.getPlazo());
        entity.setEmail(domain.getEmail());
        entity.setIdTipoPrestamo(domain.getIdTipoPrestamo());
        entity.setIdEstado(domain.getIdEstado());
        return entity;
    }

}
