package co.com.pragma.r2dbc;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.pragma.entity.SolicitudEntity;
import reactor.core.publisher.Flux;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository
		extends ReactiveCrudRepository<SolicitudEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

	@Query("SELECT * FROM solicitud WHERE id_estado IN (:estados) LIMIT :limit OFFSET :offset")
	Flux<SolicitudEntity> findByEstadosPaged(List<Integer> estados, int limit, int offset);

}
