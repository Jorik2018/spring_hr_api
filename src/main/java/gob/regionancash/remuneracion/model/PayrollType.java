package gob.regionancash.remuneracion.model;

import gob.regionancash.hr.model.EmployeeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "per_tipo_planilla_0")
public class PayrollType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipopla")
    private Integer idTipopla;

    @Size(max = 100)
    @Column(name = "tipo_planilla")
    private String name;

    @Column(name = "id_fuente_financ")
    private Integer idFuenteFinanc;

    @JoinColumn(name = "id_tipo_trabajador", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EmployeeType employeeType;

    @Column(name = "id_modalidad")
    private Integer modalityId;

    @Column(name = "id_mod_pago")
    private Integer paymentModeId;

    @Column(name = "id_actividad")
    private Integer idActividad;

    @Column(name = "id_dep")
    private Integer idDep;

    @Size(max = 200)
    @Column(name = "descripcion")
    private String description;

    @Basic(optional = false)
    @NotNull
    @Column(name = "periodicidad")
    private int periodicity = 0;

    @Basic(optional = false)
    @NotNull
    @Column(name = "chd")
    private Character chd;
    //////////////////////////////////

    @Column(name = "dependency_id")
    private Integer dependencyId;

    private String abbreviation;

    @Transient
    private PayrollGroup mainGroup;

    @Transient
    private PayrollGroup group;

    @Column(name = "group_id")
    private Integer groupId;

    @Transient
    private List<PayrollTypePeople> persons;

}