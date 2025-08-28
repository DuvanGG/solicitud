package co.com.pragma.model.solicitud;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
	
	 private Long id;
	 @Schema(description = "Monto total solicitado para el préstamo", example = "2000000")
	 private Double monto;

	 @Schema(description = "Plazo del préstamo en meses", example = "24")
	 private Integer plazo;

	 @Schema(description = "Correo electrónico del solicitante", example = "usuario@correo.com")
	 private String email;

	 @Schema(description = "Identificador del tipo de préstamo", example = "1")
	 private Integer idTipoPrestamo;

	 @Schema(description = "Identificador del estado de la solicitud", example = "0")
	 private Integer idEstado;            
	
}
