package co.com.pragma.sqs.sender;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SqsGateway;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements SqsGateway {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    
    private final ObjectMapper objectMapper;

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

	@Override
	public Mono<String> enviarMensaje(Solicitud solicitud) {
		try {
            String mensaje = objectMapper.writeValueAsString(solicitud);
            log.info("Preparando mensaje para enviar a SQS: {}", mensaje);
            return send(mensaje)
                    .doOnSuccess(msgId -> log.info("Mensaje enviado a SQS con ID: {}", msgId))
                    .doOnError(e -> log.error("Error enviando mensaje a SQS", e));
        } catch (JsonProcessingException e) {
            log.error("Error serializando mensaje para SQS", e);
            return Mono.error(e);
        }
	}
}
