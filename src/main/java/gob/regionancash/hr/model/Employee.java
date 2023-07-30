package gob.regionancash.hr.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.isobit.directory.model.Company;
import org.isobit.directory.model.People;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "rh_employee")
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_esc")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estado")
    private Short statusId = 1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_dir")
    private int peopleId;
    @Column(name = "id_dir", insertable = false, updatable = false)
    private long peopleIdLong;
    @Basic(optional = false)
    @NotNull
    @Column(name = "employee_type_id")
    private Short typeId = 1;
    @Basic(optional = false)
    @Column(name = "fecha_ing")
    @Temporal(TemporalType.DATE)
    private Date incomeDate;
    @Basic(optional = false)
    @Column(name = "condicion")
    private Short condicion;
    @Size(max = 25)
    @Column(name = "ruc")
    private String ruc;
    
    @Size(max = 100)
    @Column(name = "especialidad")
    private String especialidad;
    
    @Size(max = 20)
    @Column(name = "abrev_esp")
    private String abrevEsp;
    
    @Column(name = "fecha_cese")
    @Temporal(TemporalType.DATE)
    private Date fechaCese;
    
    @Size(max = 400)
    @Column(name = "observaciones")
    private String observations;
    
    @Size(max = 60)
    @Column(name = "id_dtra_cese")
    private String idDtraCese;
    
    @JoinColumn(name = "employee_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private EmployeeType type;
    
    @JoinColumn(name = "id_estado", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private EmployeeStatus status;
    
    @JoinColumn(name = "id_dir", referencedColumnName = "id_dir", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private People people;
    
    @Transient
    private Object ext;
    
    @JoinColumn(name = "pension_system_id", referencedColumnName = "id")
    @ManyToOne
    private PensionSystem pensionSystem;
    
    @Basic(optional = false)
    @Column(name = "f_afiliacion")
    @Temporal(TemporalType.DATE)
    private Date fAfiliacion;
    
    @Basic(optional = false)
    @Column(name = "tipo_doc")
    private Integer tipoDoc;
    
    @Basic(optional = false)
    private Short occupation;
    
    @Basic(optional = false)
    @Column(name = "n_plaza")
    private String nPlaza;
    
    @Basic(optional = false)
    @Column(name = "labor_regime_id")
    private Short laborRegimeId;
    
    @Transient
    private String laborRegimeName;
    @Column(name = "user_id")
    private Integer userId;
    
    private String cuspp;
    
    @Column(name = "nro_bank_account")
    private String nroBankAccount;
    
    private String autogenerado;

    @Column(name = "work_modality")
    private String workModality;
    
    private boolean canceled;
    
    @Column(name = "incentivo_laboral")
    private BigDecimal incentivoLaboral;
    
    @Column(name = "remuneracion")
    private BigDecimal remuneracion;

    @Transient
    private Company company;

}
