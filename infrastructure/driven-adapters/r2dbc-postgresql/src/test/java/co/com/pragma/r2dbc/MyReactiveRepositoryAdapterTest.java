package co.com.pragma.r2dbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;

import co.com.pragma.entity.SolicitudEntity;
import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(1L);
        entity.setEmail("test");

        // Mock: el repositorio devuelve un Mono<SolicitudEntity>
        when(repository.findById(1L)).thenReturn(Mono.just(entity));

        // Mock: el mapper convierte SolicitudEntity -> Solicitud (dominio)
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEmail("test");
        when(mapper.map(entity, Solicitud.class)).thenReturn(solicitud);

        // Act: repositoryAdapter devuelve Mono<Solicitud>
        Mono<Solicitud> result = repositoryAdapter.findById(1L);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        // Preparar entidades de ejemplo
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(1L);
        entity.setEmail("test");

        // Mock del repositorio: devuelve Flux<SolicitudEntity>
        when(repository.findAll()).thenReturn(Flux.just(entity));

        // Mock del mapper: convierte de SolicitudEntity -> Solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEmail("test");
        when(mapper.map(entity, Solicitud.class)).thenReturn(solicitud);

        // Act: repositoryAdapter devuelve Flux<Solicitud>
        Flux<Solicitud> result = repositoryAdapter.findAll();

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        // Crear entidad de ejemplo
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(1L);
        entity.setEmail("test");

        // Mock del repositorio: Flux<SolicitudEntity> al hacer findAll con Example
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(entity));

        // Mock del mapper: de entidad a dominio
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEmail("test");
        when(mapper.map(entity, Solicitud.class)).thenReturn(solicitud);

        // Act: repositoryAdapter.findByExample espera un ejemplo de dominio, retorna Flux<Solicitud>
        Flux<Solicitud> result = repositoryAdapter.findByExample(solicitud);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals("test"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        // Crear entidad de ejemplo
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(1L);
        entity.setEmail("test");

        // Crear objeto de dominio correspondiente
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setEmail("test");

        // Mock del repositorio: cuando se guarde la entidad, retorna Mono<SolicitudEntity>
        when(repository.save(any(SolicitudEntity.class))).thenReturn(Mono.just(entity));

        // Mock del mapper: de dominio a entidad y de entidad a dominio
        when(mapper.map(solicitud, SolicitudEntity.class)).thenReturn(entity);
        when(mapper.map(entity, Solicitud.class)).thenReturn(solicitud);

        // Act: repositoryAdapter.save espera un dominio, retorna Mono<Solicitud>
        Mono<Solicitud> result = repositoryAdapter.save(solicitud);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(value -> value.getEmail().equals("test"))
                .verifyComplete();
    }
}
