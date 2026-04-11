package gob.regionancash.remuneracion.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import org.isobit.util.OptionMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@Entity
@Table(name = "per_planilla_0")
public class Payroll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_planilla")
    private Integer id;

    @JoinColumn(name = "id_tipopla", referencedColumnName = "id_tipopla")
    @ManyToOne(optional = false)
    private PayrollType type;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Size(max = 2147483647)
    @Column(name = "comentario")
    private String comments;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cierre")
    private short cierre;

    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private short period;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "dias")
    private BigDecimal days;

    @Basic(optional = false)
    @NotNull
    @Column(name = "dominicales")
    private BigDecimal dominicales;

    @Basic(optional = false)
    @NotNull
    @Column(name = "resultado")
    private float resultado;

    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private Character estado;

    /////////////////////////////////////////////////////////

    @Column(name = "id_fuente_financ")
    private Integer fuenteFinanc;

    @Transient
    private Integer fuenteFinancName;

    @Transient
    private String code;

    private boolean closed;

    @Column(name = "generate_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generateDate;

    @Transient
    private Object modLaboral;

    private boolean canceled;

    @Column(name = "prepared_by")
    private Integer preparedBy;

    @Transient
    private Object ext;

    @Transient
    private Character status;

    public static final Map<Character, String> ESTADO_MAP = new OptionMap();

    static {
        ESTADO_MAP.put('0', "BORRADOR");
        ESTADO_MAP.put('1', "TRAMITE");
        ESTADO_MAP.put('2', "ELABORACION");
        ESTADO_MAP.put('3', "EN PROCESO");
        ESTADO_MAP.put('4', "PROCESADO");
        ESTADO_MAP.put('5', "COMPROMETIDO");
        ESTADO_MAP.put('6', "GIRADO");
        ESTADO_MAP.put('9', "ANULADO");
    }

    @Transient
    private Collection<PayrollPeople> payrollPeopleCollection;

}
