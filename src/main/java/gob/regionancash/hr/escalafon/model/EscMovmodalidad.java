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
@Table(name = "esc_movmodalidad")
@Data
public class EscMovmodalidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_movmod")
    private Short idMovmod;
    @Basic(optional = false)
    @Column(name = "modalidad")
    private String modalidad;
    @Basic(optional = false)
    @Column(name = "abrev")
    private String abrev;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMovmod")
    private List<EscCarreraLaboral> escCarreraLaboralList;

}
