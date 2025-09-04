package co.com.pragma.model.solicitud;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
	
	 private Long id;
	 
	 private Double monto;

	 private Integer plazo;
	
	 private String email;

	 private Integer idTipoPrestamo;

	 private Integer idEstado;            
	
}
