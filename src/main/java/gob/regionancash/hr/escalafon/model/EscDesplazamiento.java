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
@Table(name = "esc_desplazamiento")
@Data
public class EscDesplazamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_desp")
    private Short idDesp;
    @Column(name = "desp_personal")
    private String despPersonal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDesp")
    private List<EscEncargatura> escEncargaturasList;
    /*@OneToMany(mappedBy = "escDesplazamiento")
    private List<EscCarreraLaboral> escCarreraLaboralList;*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDesp")
    private List<EscPersDep> escPersDepList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDesp")
    private List<EscRotacion> escRotacionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDesp")
    private List<EscRotacionCas> escRotacionesCasList;
}