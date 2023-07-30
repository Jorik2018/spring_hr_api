package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "esc_nivel_remunerativo")
@Data
public class EscNivelRemunerativo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_nivel")
    private Short idNivel;
    @Basic(optional = false)
    @Column(name = "nivel")
    private String nivel;
    @OneToMany(mappedBy = "nivelRem")
    private List<EscCap> escCapList;
    @JoinColumn(name = "id_go", referencedColumnName = "id_go")
    @ManyToOne(optional = false)
    private EscGrupoOcupacional idGo;
    
}
