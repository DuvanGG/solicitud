package co.com.pragma.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.pragma.api.dto.SolicitudDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/solicitud",
            method = RequestMethod.POST,
            beanClass = Handler.class,
            beanMethod = "registrarSolicitud",
            operation = @Operation(
                operationId = "registrarSolicitud",
                summary = "Registrar una solicitud",
                description = "Crea una nueva solicitud en el sistema",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos de la solicitud a registrar",
                    content = @Content(schema = @Schema(implementation = SolicitudDTO.class))
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Solicitud registrada correctamente",
                        content = @Content(schema = @Schema(implementation = SolicitudDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Datos inválidos"
                    )
                }
            )
        )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/usecase/path"), handler::listenGETUseCase)
                .andRoute(POST("/api/usecase/otherpath"), handler::listenPOSTUseCase)
                .and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase))
                .and(route(POST("/api/v1/solicitud"), handler::registrarSolicitud))
                .andRoute(GET("/api/v1/solicitud"), handler::listarSolicitudes)
                .andRoute(GET("/api/v2/solicitud"), handler::listarSolicitudesPendiendientes)
                .andRoute(PUT("/api/v1/solicitud/{id}"), handler::actualizarEstado);
    }
}
