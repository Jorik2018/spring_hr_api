package gob.regionancash.hr.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "drt_pernatparent")
public class Relative implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pariente")
    private Integer idPariente;
    @Column(name = "id_dirperfam")
    private Integer idDirperfam;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_dirperuns")
    private int idDirperuns;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_pprn")
    private short idPprn;

    @Basic(optional = false)
    @NotNull
    @Column(name = "vive_parent")
    private boolean alive;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_pnec")
    private short idPnec;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_grdins")
    private short idGrdins;
    
    @Size(max = 120)
    @Column(name = "nombre_parent")
    private String nombreParent="";
    
    @Column(name = "fecha_nac")
    @Temporal(TemporalType.DATE)
    private Date fechaNac;
    @Size(max = 200)
    @Column(name = "ocupacion")
    private String ocupacion="";
    @Size(max = 60)
    @Column(name = "paterno_parent")
    private String paternoParent="";
    @Size(max = 60)
    @Column(name = "materno_parent")
    private String maternoParent="";
    @Basic(optional = false)
    @NotNull
    @Column(name = "discapacitado_parent")
    private boolean disabled;

}
