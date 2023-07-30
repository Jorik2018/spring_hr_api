package gob.regionancash.hr.escalafon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.isobit.directory.model.Dependency;

import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.model.EscTipoPersonal;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "esc_carreralaboral")
public class EscCarreraLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_percl")
    private Integer idPercl;
    @Column(name = "id_percl_de")
    private Integer idPerclDe;
    @Column(name = "id_cap")
    private Integer idCap;
    @Column(name = "tipo_cap")
    private Boolean tipoCap;
    @Basic(optional = false)
    @Column(name = "fecha_ini")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "cod_dtra")
    private String codDtra;
    @Basic(optional = false)
    @Column(name = "condicion")
    private short condicion;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "jefe")
    private boolean jefe;
    @Basic(optional = false)
    @Column(name = "plaza")
    private boolean plaza;
    @Basic(optional = false)
    @Column(name = "servicio")
    private boolean servicio;
    @Column(name = "horas")
    private Short horas;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private BigDecimal monto;
    
    @Column(name = "mencion")
    private String mencion;
    
    @Basic(optional = false)
    @Column(name = "migrado")
    private boolean migrado;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "escCarreraLaboral")
    //private List<EscLabordependencias> escLabordependenciasList;
    @JoinColumn(name = "id_desp", referencedColumnName = "id_desp")
    @ManyToOne(fetch = FetchType.LAZY)
    private EscDesplazamiento escDesplazamiento;
    @JoinColumn(name = "id_mov", referencedColumnName = "id_mov")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EscMovimiento escMovimiento;
    @JoinColumn(name = "id_movmod", referencedColumnName = "id_movmod")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EscMovmodalidad idMovmod;
    
    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Employee employee;
    
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
    @ManyToOne
    private EscServicio escServicio;
    
    @JoinColumn(name = "id_dep", referencedColumnName = "id_dep")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dependency orgDependencia;
    
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    @ManyToOne(optional = false)
    private EscTipoPersonal escTipoPersonal;
    
    @JoinColumn(name = "id_tp", referencedColumnName = "id_tp")
    @ManyToOne(fetch = FetchType.LAZY)
    private EscTipoPromocion idTp;

    /*@JoinColumns({
        @JoinColumn(name = "id_cap", referencedColumnName = "id_cap", insertable = false, updatable = false),
        @JoinColumn(name = "id_dep", referencedColumnName = "id_dep", insertable = false, updatable = false),
        @JoinColumn(name = "tipo_cap", referencedColumnName = "tipo_cap", insertable = false, updatable = false)
    })
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private EscCapUns escCapUns;*/
}