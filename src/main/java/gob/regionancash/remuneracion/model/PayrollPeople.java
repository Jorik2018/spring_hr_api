package gob.regionancash.remuneracion.model;

import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.model.PensionSystem;
import gob.regionancash.remuneracion.model.PayrollConcept;
import gob.regionancash.remuneracion.model.PayrollPeoplePK;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.isobit.directory.model.Dependency;
import org.isobit.directory.model.People;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "per_detallepla_0")
public class PayrollPeople implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected PayrollPeoplePK id;

    public PayrollPeople(PayrollPeoplePK id){
        this.id = id;
    }
    
    @Column(name = "id_tipotrabajador")
    private Integer employeeTypeId;

    @Column(name = "id_tipopla")
    private Integer payrollTypeId;

    @Column(name = "id_nivel")
    private Integer remunerativeLevelId;

    @Size(max = 40)
    @Column(name = "nivel")
    private String remunerativeLevel;

    @Column(name = "horas")
    private Integer hours;

    @Size(max = 100)
    @Column(name = "cargo")
    private String position;

    @Size(max = 100)
    @Column(name = "encargatura")
    private String commission;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(name = "diaslaborados")
    private Float workedDays;

    @Column(name = "horasextra")
    private Float extraHours;

    @Column(name = "dominicales")
    private Float dominicales;

    @Column(name = "dependency_id")
    private Integer dependencyId;

    @Transient
    private Dependency dependency;

    private String dependencyName;

    //////////////////////////////

    @Transient
    private Payroll payroll;

    @Transient
    private String jurisdictionName;

    @Transient
    private Integer jurisdiction;

    @Transient
    private People people;

    @Transient
    private Date incomeDate;

    @Transient
    private Object ext;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Transient
    private Employee employee;

    @Column(name = "dias_falt")
    private Integer diasFalt = 0;

    @ManyToOne
    @JoinColumn(name = "pension_system_id")
    private PensionSystem pensionSystem;

    private String document;

    @Transient
    private String actiCodigo;

    @Column(name = "monto_rem")
    private BigDecimal montoRem;

    @Column(name = "total_ingr")
    private BigDecimal totalIngr;

    @Column(name = "total_desc")
    private BigDecimal totalDesc;

    @Transient
    private BigDecimal discountAssist;

    @Column(name = "total_apor")
    private BigDecimal totalApor;

    @Column(name = "rem_aseg")
    private BigDecimal remAseg;

    private String cuspp;

    private String comment;

    @Transient
    private Integer diasDom;

    @Column(name = "min_tard")
    private Integer minTard = 0;

    @Column(name = "attendance_discount")
    private BigDecimal attendanceDiscount = BigDecimal.ZERO;

    @Transient
    private Integer diasFer;

    @Transient
    private Integer diasFtrab;

    @Transient
    private Integer diasDm;

    @Transient
    private Integer diasVac;

    @Transient
    private Integer numHijos;

    @Transient
    private List<PayrollConcept> payrollConceptCollection;

}