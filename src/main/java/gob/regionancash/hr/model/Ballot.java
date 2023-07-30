package gob.regionancash.hr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.isobit.directory.model.People;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "per_papeleta")
@Data
public class Ballot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_papeleta")
    private Integer id;
    @Column(name = "id_esc_autorizo")
    private Integer idEscAutorizo;
    
    @Basic(optional = false)
    @Column(name = "id_esc_servidor")
    private int employeeId;

    @JoinColumn(name = "id_dir_servidor", referencedColumnName = "id_dir")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private People people;

    @Basic(optional = false)
    @Column(name = "id_tm")
    private int idTm;
    
    private String especificar;
    @Basic(optional = false)
    @Column(name = "lugar")
    private String lugar = "";
    @Basic(optional = false)
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Basic(optional = false)
    @Column(name = "fecha_efectiva")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEfectiva;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Column(name = "h_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hDesde;
    @Column(name = "h_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hHasta;
    @Basic(optional = false)
    @Column(name = "retorno")
    private boolean retorno;
    @Column(name = "id_dep")
    private Integer idDep;
    @Column(name = "h_ing")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hIng;
    @Column(name = "h_sal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hSal;
    @Column(name = "id_depctr")
    private Integer idDepctr;
    @Basic(optional = false)
    @Column(name = "autorizado")
    private boolean autorizado = false;
    @Transient
    private Object ext;

    private static final Map REASON_TYPE = new HashMap<>(){{
        put(1, "COMISION OFICIAL DE SERVICIO");
        put(2, "PERMISO PARTICULAR");
        put(3, "SALUD");
    }};

    public Map getREASON_TYPE() {
        return REASON_TYPE;
    }

}
