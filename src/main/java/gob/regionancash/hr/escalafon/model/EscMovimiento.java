package gob.regionancash.hr.escalafon.model;

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
@Table(name = "esc_movimientos")
@Data
public class EscMovimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_mov")
    private Short idMov;
    @Basic(optional = false)
    @Column(name = "movimiento")
    private String movimiento;
    @Basic(optional = false)
    @Column(name = "abrev")
    private String abrev;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escMovimiento")
    private List<EscCarreraLaboral> escCarreraLaboralList;

}
