package co.com.pragma.r2dbc;

import co.com.pragma.entity.SolicitudEntity;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

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

}
