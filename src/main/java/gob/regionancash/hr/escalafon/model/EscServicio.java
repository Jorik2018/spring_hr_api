package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "esc_servicios")
@Data
public class EscServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_servicio")
    private Integer idServicio;
    @Basic(optional = false)
    @Column(name = "servicio")
    private String servicio;
    @OneToMany(mappedBy = "escServicio")
    private List<EscContratoOtro> escContratosOtrosList;
    @OneToMany(mappedBy = "escServicio")
    private List<EscCarreraLaboral> escCarreraLaboralList;
}