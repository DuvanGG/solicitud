package co.com.pragma.model.solicitud.responses;

import java.util.List;

import co.com.pragma.model.solicitud.Solicitud;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolicitudPageResponse {
	private List<Solicitud> items;
    private Long nextCursor;


}
