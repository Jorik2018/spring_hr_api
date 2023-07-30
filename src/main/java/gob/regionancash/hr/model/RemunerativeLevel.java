package gob.regionancash.hr.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "rh_remunerative_level")
@Data
public class RemunerativeLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "employee_type_id")
    private Integer employeeTypeId;
    
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    
    @Size(max = 15)
    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "periodicidad")
    private Character periodicidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_basico")
    private BigDecimal montoBasico;
    
    @Column(name = "monto_tope")
    private BigDecimal montoTope;
    
    private BigDecimal buc;
    
    @JoinColumn(name = "employee_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EmployeeType employeeType;

}
