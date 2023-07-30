package gob.regionancash.hr.escalafon.model;

import org.isobit.app.X;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "esc_historia_cap")
@Data
public class EscHistoriaCap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_hist_cap")
    private Integer idHistCap;
    @Basic(optional = false)
    @Column(name = "id_dtra")
    private String idDtra;
    @Column(name = "elaborada")
    private String elaborada;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "fecha_doc")
    @Temporal(TemporalType.DATE)
    private Date fechaDoc;
    @OneToMany(mappedBy = "idHistCap")
    private List<EscCap> escCapList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idHistCap")
    private List<EscCapDocente> escCapDocenteList;
    @OneToMany(mappedBy = "idHistCap")
    private List<EscCapUns> escCapUnsList;

}
