package gob.regionancash.hr.escalafon.model;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "esc_nivel_remunerativo_uns")
@Data
public class EscNivelRemunerativoUns implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EscNivelRemunerativoUnsPK escNivelRemunerativoUnsPK;
    @Basic(optional = false)
    @Column(name = "nivel")
    private String nivel;
    @Column(name = "dedicacion")
    private String dedicacion;
    @Column(name = "abrev_cat")
    private String abrevCat;
    @Column(name = "abrev_ded")
    private String abrevDed;
    @JoinColumn(name = "id_cat", referencedColumnName = "id_cat")
    @ManyToOne
    private EscCategoriaDoc idCat;
    @JoinColumn(name = "id_go", referencedColumnName = "id_go")
    @ManyToOne(optional = false)
    private EscGrupoOcupacional idGo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escNivelRemunerativoUns")
    private List<EscCapUns> escCapUnsList;


}
