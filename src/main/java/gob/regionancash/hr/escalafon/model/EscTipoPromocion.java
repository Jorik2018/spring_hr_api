package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "esc_tipo_promocion")
@Data
public class EscTipoPromocion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tp")
    private Short idTp;
    @Basic(optional = false)
    @Column(name = "tipo_promocion")
    private String tipoPromocion;
    @OneToMany(mappedBy = "idTp")
    private List<EscCarreraLaboral> escCarreraLaboralList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTp")
    private List<EscPromocion> escPromocionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTp")
    private List<EscPromocionDoc> escPromocionesDocList;

}
