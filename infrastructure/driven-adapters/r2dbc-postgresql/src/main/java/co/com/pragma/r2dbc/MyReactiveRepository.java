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

	@Query("SELECT * FROM solicitud WHERE id_solicitud > :lastId AND id_estado IN (:estados) ORDER BY id_solicitud ASC LIMIT :limit")
	Flux<SolicitudEntity> findByIdGreaterThanAndIdEstadoInOrderByIdAsc(Long lastId, List<Integer> estados, int limit);
	
}
