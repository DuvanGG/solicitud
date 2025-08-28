package co.com.pragma.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("solicitud") 
public class SolicitudEntity {

    @Id
    @Column("id_solicitud")
    private Long idSolicitud;  

    private Double monto;

    private Integer plazo;

    private String email;

    @Column("id_tipo_prestamo")
    private Integer idTipoPrestamo;

    @Column("id_estado")
    private Integer idEstado;
}

