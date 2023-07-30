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

@Entity
@Table(name = "esc_gocupacional")
public class EscGrupoOcupacional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_go")
    private Short idGo;
    @Basic(optional = false)
    @Column(name = "grupo")
    private String grupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGo")
    private List<EscNivelRemunerativoUns> escNivelRemunerativoUnsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGo")
    private List<EscNivelRemunerativo> escNivelRemunerativoList;

    public EscGrupoOcupacional() {
    }

    public EscGrupoOcupacional(Short idGo) {
        this.idGo = idGo;
    }

    public EscGrupoOcupacional(Short idGo, String grupo) {
        this.idGo = idGo;
        this.grupo = grupo;
    }
    
}
